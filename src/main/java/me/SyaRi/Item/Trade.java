package me.SyaRi.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.SyaRi.LunaRPG;
import me.SyaRi.Inventory.Get;
import me.SyaRi.Item.Equip.EquipLoad;
import me.SyaRi.Util.Util;

public class Trade implements Listener {

	private static Map<Player, Map<Player, Boolean>> trade = new HashMap<>();

	@EventHandler
	public void on(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Player && e.getPlayer().isSneaking()) {
			Player t = (Player) e.getRightClicked();
			Player p = e.getPlayer();
			if(hasInvite(t, p)) {
				if(hasInvite(p, t)) {
					startTrade(t, p);
					startTrade(p, t);
				}
			} else {
				sendInvite(t, p);
			}
		}
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if(e.getInventory() == null || e.getInventory().getName() == null) return;
		if(Util.get.uncolor(e.getInventory().getName()).equals("トレード")) {
			Player p = (Player) e.getWhoClicked();
			Player t = getTradePartner(p);
			if(isWait(p) || e.getClick().isShiftClick() || e.getClick().equals(ClickType.DOUBLE_CLICK)
					|| !(EquipLoad.canTrade(e.getCurrentItem()) && EquipLoad.canTrade(e.getCursor()))) {
				e.setCancelled(true);
			}
			if(e.getClickedInventory().equals(e.getInventory())) {
				int slot = e.getSlot();
				if(e.getClick().equals(ClickType.NUMBER_KEY)) slot = e.getRawSlot();
				if(isMySelfTradeSlot(slot)) {
					setItem((Player) e.getWhoClicked(), slot);
				} else {
					e.setCancelled(true);
					if(slot == 12) {
						setWait(p);
						if(isWait(p) && isWait(t)) {
							stopTrade(p, false);
							stopTrade(t, false);
						}
					}
					else if(slot == 21) {
						stopTrade(p, true);
						stopTrade(t, true);
					}
				}
			}
		}
	}

	@EventHandler
	public void on(InventoryCloseEvent e) {
		if(e.getInventory() == null || e.getInventory().getName() == null) return;
		if(Util.get.uncolor(e.getInventory().getName()).equals("トレード")) {
			Player p = (Player) e.getPlayer();
			Player t = getTradePartner(p);
			if(t != null) {
				stopTrade(p, true);
				stopTrade(t, true);
			}
		}
	}

	private static boolean hasInvite(Player t, Player p) {
		if(trade.containsKey(t) && trade.get(t).containsKey(p) && !trade.get(t).containsValue(true)) return true;
		else return false;
	}

	private static void sendInvite(Player t, Player p) {
		trade.put(t, new HashMap<>());
		trade.get(t).put(p, false);
		t.sendMessage(Util.get.color(Util.get.prefix.msg() + " &6" + p.getDisplayName() + " &fからトレード申請が来ました"));
		p.sendMessage(Util.get.color(Util.get.prefix.msg() + " &6" + t.getDisplayName() + " &fにトレード申請を出しました"));
		new BukkitRunnable() {
			@Override
			public void run() {
				if(trade.containsKey(t) && trade.get(t).containsKey(p) && !trade.get(t).get(p)) {
					deleteTrade(t, p);
					p.sendMessage(Util.get.color(Util.get.prefix.msg() + " &6" + t.getDisplayName() + " &fへのトレード申請がキャンセルされました"));
				}
			}
		}.runTaskLater(LunaRPG.getPlugin(), 3600);
	}

	private static void deleteTrade(Player t, Player p) {
		if(!trade.containsKey(t)) return;
		trade.get(t).remove(p);
		if(trade.get(t).isEmpty()) trade.remove(t);
	}

	private void startTrade(Player t, Player p) {
		trade.get(t).put(p, true);
		Inventory inv = Get.inv(3, "&0&lトレード");
		inv.setItem(3, Get.ItemHead("&6" + p.getDisplayName(), p, ""));
		inv.setItem(12, Get.Item("&a設定中", Material.GRAY_DYE, "&6クリックで決定"));
		inv.setItem(21, Get.Item("&cキャンセル", Material.BARRIER, ""));
		for(int i = 0; i < 3; i++) inv.setItem((i*9)+4, Get.Item("&0", Material.LIGHT_GRAY_STAINED_GLASS_PANE, ""));
		inv.setItem(5, Get.ItemHead("&6" + t.getDisplayName(), t, ""));
		inv.setItem(14, Get.Item("&a設定中", Material.GRAY_DYE, "&6クリックで決定"));
		inv.setItem(23, Get.Item("&cキャンセル", Material.BARRIER, ""));
		p.openInventory(inv);
	}

	private Player getTradePartner(Player p) {
		if(trade.containsKey(p) && !trade.get(p).isEmpty()) {
			for(Entry<Player, Boolean> e : trade.get(p).entrySet()) {
				if(e.getValue()) return e.getKey();
			}
		}
		return null;
	}

	private void stopTrade(Player p, boolean cancel) {
		Inventory inv = p.getOpenInventory().getTopInventory();
		if(cancel) {
			p.sendMessage(Util.get.color(Util.get.prefix.msg() + " &fトレードがキャンセルされました"));
			List<ItemStack> items = new ArrayList<>();
			getMySelfTradeSlot().forEach(s -> items.add(inv.getItem(s)));
			Util.item.giveItem(Arrays.asList(p), items);
		} else {
			List<ItemStack> items = new ArrayList<>();
			getPartnerTradeSlot().forEach(s -> items.add(inv.getItem(s)));
			Util.item.giveItem(Arrays.asList(p), items);
		}
		deleteTrade(p, getTradePartner(p));
		p.closeInventory();
	}

	private List<Integer> getMySelfTradeSlot() {
		return Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
	}

	private List<Integer> getPartnerTradeSlot(){
		return Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26);
	}

	private boolean isMySelfTradeSlot(int slot) {
		if(getMySelfTradeSlot().contains(slot)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isWait(Player p) {
		return p.getOpenInventory().getTopInventory().getItem(12).getType().equals(Material.LIME_DYE);
	}

	private void setWait(Player p) {
		Inventory p_inv = p.getOpenInventory().getTopInventory();
		Inventory t_inv = getTradePartner(p).getOpenInventory().getTopInventory();
		if(isWait(p)) {
			p_inv.setItem(12, Get.Item("&a設定中", Material.GRAY_DYE, "&6クリックで決定"));
			t_inv.setItem(14, Get.Item("&a設定中", Material.GRAY_DYE, "&6クリックで決定"));
		} else {
			p_inv.setItem(12, Get.Item("&a待機中", Material.LIME_DYE, "&6クリックで設定"));
			t_inv.setItem(14, Get.Item("&a待機中", Material.LIME_DYE, "&6クリックで設定"));
		}
	}

	private void setItem(Player p, int slot) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player t = getTradePartner(p);
				ItemStack item = p.getOpenInventory().getItem(slot);
				if(item != null && t != null) {
					Inventory t_inv = t.getOpenInventory().getTopInventory();
					t_inv.setItem(slot + 6, item);
				}
			}
		}.runTaskLater(LunaRPG.getPlugin(), 1);
	}
}
