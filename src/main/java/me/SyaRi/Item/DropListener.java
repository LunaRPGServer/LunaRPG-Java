package me.SyaRi.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.SyaRi.Party.Party;
import me.SyaRi.Util.Util;

public class DropListener implements Listener {

	private static final Double DROP_MIN = 0.2;

	private static Map<String, Map<Player, Double>> data = new HashMap<>();
	private static Map<Player, Float> bonus = new HashMap<>();
	private static double g_bonus = 1.0;
	private static long num = 0;

	private static void addTag(LivingEntity l) {
		l.addScoreboardTag("No." + num);
		num ++;
	}

	private static String getID(LivingEntity l) {
		for(String ss : l.getScoreboardTags()) {
			if(ss.startsWith("No.")) {
				return ss.substring(3);
			}
		}
		return "";
	}

	private static void loadData(LivingEntity l) {
		if(getID(l) == "") {
			addTag(l);
			data.put(getID(l), new HashMap<>());
		}
	}

	private static void addData(double v, Player p, LivingEntity l) {
		if(data.get(getID(l)).containsKey(p)) {
			data.get(getID(l)).put(p, data.get(getID(l)).get(p) + v);
		} else {
			data.get(getID(l)).put(p, v);
		}
	}

	private static Set<Entry<Player, Double>> getData(LivingEntity l) {
		if(data.get(getID(l)) == null) return null;
		else return data.get(getID(l)).entrySet();
	}

	private static Player getMax(LivingEntity l) {
		if(getData(l) == null) return null;
		Player max_p = null;
		double max_v = -1;
		for(Entry<Player, Double> e : getData(l)) {
			if(max_v < e.getValue()) {
				if(e.getValue() >= DROP_MIN) {
					return null;
				} else {
					max_v = e.getValue();
					max_p = e.getKey();
				}
			}
		}
		return max_p;
	}

	public static void damage(double d, Player attacker, LivingEntity victim) {
		if(victim instanceof Player || victim.getName() == null || d <= 0) return;
		loadData(victim);
		if(victim.getHealth() < d) {
			addData(victim.getHealth() * getBonus(attacker), attacker, victim);
			double max_health = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			getData(victim).forEach(c -> {
				if(c.getKey() != null && c.getValue() != null) {
					Player p = c.getKey();
					double mult = c.getValue() / max_health;
					if(p.isOnline()) {
						addExp(p, victim, mult);
						if(Party.isBelong(p)) {
							Party.getParty(p).forEach(m -> {
								if(!p.equals(m)) addExp(m, victim, mult / 5);
							});
						}
					}
				}
			});
		} else {
			addData(d * getBonus(attacker), attacker, victim);
		}
	}

	private static void addExp(Player p, LivingEntity victim, double mult) {
		int m_lv = getLevel(Util.get.uncolor(victim.getName()));
		if(SkillAPI.getPlayerAccountData(p).getActiveData().getMainClass() == null) return;
		int p_lv = SkillAPI.getPlayerAccountData(p).getActiveData().getMainClass().getLevel();
		double rarity = getRarity(victim.getName());
		double exp = getExp(m_lv, p_lv, rarity);
		SkillAPI.getPlayerAccountData(p).getActiveData().getMainClass().giveExp(exp * mult, ExpSource.SPECIAL);
	}

	private static int getLevel(String s) {
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(s);
		if(m.find()) {
			return Integer.parseInt(m.group(0));
		} else {
			return 0;
		}
	}

	private static double getRarity(String s) {
        Pattern p = Pattern.compile(".$");
        Matcher m = p.matcher(s);
		if(m.find()) {
			switch(m.group(0)){
				case "s": case "S": return 3.0;
				case "a": case "A": return 1.5;
				case "b": case "B": return 1.3;
				case "c": case "C": return 1.0;
				case "d": case "D": return 0.7;
			}
		}
		return 1.0;
	}

	private static double getBonus(Player p) {
		if(bonus.containsKey(p)) {
			if(bonus.get(p) > g_bonus) {
				return bonus.get(p);
			}
		}
		return g_bonus;
	}

	// TODO どこかに実装
	public static boolean setBonus(Player p, float v, long sec) {
		if(bonus.containsKey(p) && bonus.get(p) >= v) {
			return false;
		} else {
			// TODO scheduler実装
			bonus.put(p, v);
			return true;
		}
	}

	private static double getExp(int m_lv, int p_lv, double rarity) {
		if(m_lv < 1 || p_lv < 1 || (m_lv - p_lv) > 4 || (p_lv - m_lv) > 9) return 0;
		return Math.round((0.9 - 0.1 * Math.abs(m_lv - p_lv)) * (rarity * (Math.pow(1.03, m_lv) + 5 * m_lv) + 2 * m_lv));
	}

	@EventHandler
	public void on(MythicMobDeathEvent e) {
		if(e.getEntity() instanceof LivingEntity && !e.getDrops().isEmpty()) {
			LivingEntity l = (LivingEntity) e.getEntity();
			double max_health = l.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			if(getData(l) == null || getData(l).isEmpty()) return;
			List<Player> pp = new ArrayList<>();
			if(getMax(l) != null) {
				pp.add(getMax(l));
			} else {
				getData(l).forEach(c -> {
					double mult = c.getValue() / max_health;
					if(mult >= DROP_MIN) {
						pp.add(c.getKey());
					}
				});

			}
			Util.item.dropItem(e.getEntity().getLocation(), e.getDrops(), pp);
		}
	}

	@EventHandler
	public void on(PlayerDropItemEvent e) {
		Item i = e.getItemDrop();
		i.addScoreboardTag(e.getPlayer().getUniqueId().toString());
	}

	@EventHandler
	public void on(ItemMergeEvent e) {
		Item t = e.getTarget();
		Item b = e.getEntity();
		if(!t.getScoreboardTags().equals(b.getScoreboardTags())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if((e.getItem().getScoreboardTags() != null && e.getItem().getScoreboardTags().contains(p.getUniqueId().toString()))
					|| (p.isOp() && p.isSneaking())) {
				return;
			}
		}
		e.setCancelled(true);
	}
}
