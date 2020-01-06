package me.SyaRi.Item.Equip;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerManaGainEvent;

import me.SyaRi.LunaRPG;
import me.SyaRi.Item.DropListener;
import me.SyaRi.Util.Util;

public class EquipListener implements Listener {

	private static Map<Player, EquipData> data = new HashMap<>();
	private static Map<Arrow, ProjectileData> arrows = new HashMap<>();
	private static Map<Trident, Entry<ProjectileData, ItemStack>> tridents = new HashMap<>();

	@EventHandler
	public void on(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof LivingEntity && e.getDamager() instanceof LivingEntity)) return;
		double dmg = e.getDamage();
		double def = 0;
		if(e.getEntity() instanceof Player) {
			Player victim = (Player) e.getEntity();
			EquipData armor = data.get(victim);
			def = armor.getDefense();
			if(armor.getDodgeRate() * (victim.isSprinting()? 1.1 : 1) > random()) {
				e.setCancelled(true);
				victim.getWorld().spawnParticle(Particle.SMOKE_NORMAL, victim.getLocation(), 100, 1, 2, 1);
				return;
			}
			if(armor.getBlockRate() * (victim.isBlocking()? 1.1 : 1) > random()) {
				e.setDamage(0);
				victim.getWorld().spawnParticle(Particle.CRIT_MAGIC, victim.getLocation(), 100, 1, 2, 1);
				return;
			}
		}
		if(e.getDamager() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			LivingEntity victim = (LivingEntity) e.getEntity();
			if(!EquipLoad.canUseLevel(attacker, attacker.getInventory().getItemInMainHand())) {
				e.setCancelled(true);
				return;
			}
			EquipData weapon = data.get(attacker);
			if(weapon.isBow()) {
				dmg = 0;
			} else {
				dmg = weapon.getDamage() * getCharge(e);
				if(weapon.getHitRate() * (attacker.isSneaking()? 1.2 : 1) < random() && weapon.getHitRate() > 0) {
					e.setCancelled(true);
					victim.getWorld().spawnParticle(Particle.SMOKE_NORMAL, victim.getLocation(), 100, 1, 2, 1);
					return;
				}
				if(weapon.getCritRate() * (isCrit(attacker)? 1.2 : 1 ) > random()) {
					dmg = dmg * (1 + (weapon.getCritDmg() / 100));
					victim.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, victim.getLocation(), 100, 1, 2, 1);
				}
			}
			DropListener.damage(dmg - def, attacker, victim);
		} else if(e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
			Arrow arrow = (Arrow) e.getDamager();
			Player attacker = (Player) arrow.getShooter();
			LivingEntity victim = (LivingEntity) e.getEntity();
			ProjectileData weapon = arrows.get(arrow);
			dmg = weapon.getDamage() * (e.getDamage() / 10);
			if(weapon.getCritRate() > random()) {
				dmg = dmg * (1 + (weapon.getCritDmg() / 100));
				victim.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, victim.getLocation(), 100, 1, 2, 1);
			}
			DropListener.damage(dmg - def, attacker, victim);
		} else if(e.getDamager() instanceof Trident && ((Trident) e.getDamager()).getShooter() instanceof Player) {
			Trident trident = (Trident) e.getDamager();
			Player attacker = (Player) trident.getShooter();
			LivingEntity victim = (LivingEntity) e.getEntity();
			ProjectileData weapon = tridents.get(trident).getKey();
			dmg = weapon.getDamage() * 0.9;
			if(weapon.getCritRate() > random()) {
				dmg = dmg * (1 + (weapon.getCritDmg() / 100));
				victim.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, victim.getLocation(), 100, 1, 2, 1);
			}
			DropListener.damage(dmg - def, attacker, victim);
			tridents.remove(trident);
		}
		e.setDamage((dmg - def < 0)? 0 : dmg - def);
	}

	@EventHandler
	public void on(EntityShootBowEvent e) {
		if(e.getProjectile() instanceof Arrow && e.getEntity() instanceof Player) {
			if(!EquipLoad.canUseLevel((Player) e.getEntity(), e.getBow())){
				e.setCancelled(true);
				return;
			}
			Arrow arrow = (Arrow) e.getProjectile();
			arrows.put(arrow, ProjectileData.load(e.getBow()));
		}
	}

	@EventHandler
	public void on(ProjectileLaunchEvent e) {
		if(e.getEntity() instanceof Trident) {
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				ItemStack t = p.getInventory().getItemInMainHand();
				if(!t.getType().equals(Material.TRIDENT)) t = p.getInventory().getItemInOffHand();
				if(!EquipLoad.canUseLevel(p, t)) {
					e.setCancelled(true);
					return;
				}
				Trident trident = (Trident) e.getEntity();
				tridents.put(trident, ProjectileData.get(t));
			}
		}
	}

	@EventHandler
	public void on(ProjectileHitEvent e) {
		if(e.getEntity() instanceof Arrow) {
			e.getEntity().remove();
			Arrow a = (Arrow) e.getEntity();
			arrows.remove(a);
		} else if(e.getEntity() instanceof Trident) {
			Trident t = (Trident) e.getEntity();
			ItemStack i = tridents.get(t).getValue();
			Util.item.giveItem(Arrays.asList((Player) t.getShooter()), Arrays.asList(i));
			tridents.remove(t);
		}
	}

	private boolean isCrit(Player p) {
		if(p.getFallDistance() > 0.0F && !p.isOnGround() && !p.hasPotionEffect(PotionEffectType.BLINDNESS) && p.getVehicle() == null) return true;
		else return false;
	}

	private double getCharge(EntityDamageByEntityEvent e) {
		Player p = (Player) e.getDamager();
		double dmg = e.getDamage();
		if(isCrit(p)) dmg /= 1.5;
		double base = getBaseDamage(p.getInventory().getItemInMainHand().getType());
		return (base > 0)? dmg / base : 0;
	}

	private static Map<Double, List<Material>> base_dmg = new HashMap<Double, List<Material>>() {
		{
			put(9.0D, Arrays.asList(Material.DIAMOND_AXE, Material.IRON_AXE, Material.STONE_AXE, Material.TRIDENT));
			put(7.0D, Arrays.asList(Material.DIAMOND_SWORD, Material.GOLDEN_AXE, Material.WOODEN_AXE));
			put(6.0D, Arrays.asList(Material.IRON_SWORD));
			put(5.5D, Arrays.asList(Material.DIAMOND_AXE));
			put(5.0D, Arrays.asList(Material.STONE_SWORD, Material.DIAMOND_PICKAXE));
			put(4.5D, Arrays.asList(Material.IRON_SHOVEL));
			put(4.0D, Arrays.asList(Material.WOODEN_SWORD, Material.GOLDEN_SWORD, Material.IRON_PICKAXE));
			put(3.5D, Arrays.asList(Material.STONE_SHOVEL));
			put(3.0D, Arrays.asList(Material.STONE_PICKAXE));
			put(2.5D, Arrays.asList(Material.WOODEN_SHOVEL, Material.GOLDEN_SHOVEL));
			put(2.0D, Arrays.asList(Material.WOODEN_PICKAXE, Material.GOLDEN_PICKAXE));
			put(1.0D, Arrays.asList(Material.DIAMOND_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.WOODEN_HOE));
		}
	};
	private double getBaseDamage(Material m) {
		for(Entry<Double, List<Material>> b : base_dmg.entrySet()) {
			if(b.getValue().contains(m)) return b.getKey();
		}
		return 0D;
	}

	private static Map<Double, List<Material>> base_speed = new HashMap<Double, List<Material>>() {
		{
			put(-1.0D, Arrays.asList(Material.DIAMOND_HOE));
			put( 0.0D, Arrays.asList(Material.IRON_HOE));
			put( 1.0D, Arrays.asList(Material.STONE_HOE));
			put( 1.4D, Arrays.asList(Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD));
			put( 1.8D, Arrays.asList(Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE));
			put( 1.9D, Arrays.asList(Material.TRIDENT));
			put( 2.0D, Arrays.asList(Material.DIAMOND_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.GOLDEN_HOE, Material.WOODEN_HOE));
			put( 2.1D, Arrays.asList(Material.IRON_AXE));
			put( 2.2D, Arrays.asList(Material.STONE_AXE, Material.WOODEN_AXE));
		}
	};
	private static double getBaseSpeed(Material m) {
		for(Entry<Double, List<Material>> b : base_speed.entrySet()) {
			if(b.getValue().contains(m)) return b.getKey();
		}
		return 0D;
	}

	private float random() {
		return new Random().nextFloat() * 100;
	}

	@EventHandler
	public void on(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.FALL)) {
			if(e.getEntity() instanceof LivingEntity) {
				LivingEntity l = (LivingEntity) e.getEntity();
				double dmg = (l.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 25) * e.getDamage();
				e.setDamage(dmg);
			}
		} else if(e.getCause().equals(DamageCause.DROWNING)) {
			if(e.getEntity() instanceof LivingEntity) {
				LivingEntity l = (LivingEntity) e.getEntity();
				e.setDamage(l.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 10);
			}
/*
		} else if(e.getCause().equals(DamageCause.POISON)) {
			LivingEntity l = (LivingEntity) e.getEntity();
			double dmg = 0;
			e.setDamage(dmg);
		} else if(e.getCause().equals(DamageCause.WITHER)) {
			LivingEntity l = (LivingEntity) e.getEntity();
			double dmg = 0;
			e.setDamage(dmg);
		} else if(e.getCause().equals(DamageCause.FIRE)) {
			LivingEntity l = (LivingEntity) e.getEntity();
			double dmg = 0;
			e.setDamage(dmg);
*/
		} else if(e.getCause().equals(DamageCause.VOID)) {
			if(e.getEntity() instanceof LivingEntity) {
				LivingEntity l = (LivingEntity) e.getEntity();
				e.setDamage(l.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			}
		}
	}

	@EventHandler
	public void on(PlayerManaGainEvent e) {
		Player p = e.getPlayerData().getPlayer();
		if(data.get(p) != null) e.setAmount(e.getAmount() + data.get(p).getRegMana());
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		int slot;
		if(e.getClick().equals(ClickType.NUMBER_KEY)) slot = e.getRawSlot();
		else slot = e.getSlot();
		Player p = (Player) e.getWhoClicked();
		if((0 <= slot && slot <= 9) || (36 <= slot && slot <= 40)) {
			if((36 <= slot && slot <= 40) && !(EquipLoad.canUseLevel(p, e.getCursor()) && EquipLoad.canUseLevel(p, e.getCurrentItem()))) {
				e.setCancelled(true);
				return;
			}
			put(p);
		}
	}

	@EventHandler
	public void on(PlayerSwapHandItemsEvent e) {
		put(e.getPlayer());
	}

	@EventHandler
	public void on(PlayerDropItemEvent e) {
		put(e.getPlayer());
	}

	@EventHandler
	public void on(PlayerItemHeldEvent e) {
		put(e.getPlayer());
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		data.remove(e.getPlayer());
	}

	private static void put(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				ItemStack held = p.getInventory().getItemInMainHand();
				if(held != null) {
					data.put(p, EquipLoad.load(p, held));
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(data.get(p).getAttSpeed() + getBaseSpeed(held.getType()));
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(SkillAPI.getPlayerAccountData(p).getActiveData().getMainClass().getHealth() + data.get(p).getMaxHealth());
					if(p.getHealth() > p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
//					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue();
//					p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue();
					p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
					SkillAPI.getPlayerAccountData(p).getActiveData().addMaxMana(data.get(p).getMaxMana());
				}
			}
		}.runTaskLater(LunaRPG.getPlugin(), 1);
	}
}
