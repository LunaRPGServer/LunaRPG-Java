package me.SyaRi.Shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.SyaRi.LunaRPG;
import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Util.MySQL;
import me.SyaRi.Util.Util;

public class Sell implements Listener {

	public static void open(Player p) {
		Inventory inv = Get.inv(6, "&0&l買取屋");
		Inv.setBottom(inv, false);
		inv.setItem(49, Get.Item("&6&l買取額 &a0", Material.LIGHT_BLUE_STAINED_GLASS_PANE, "&a&l&nクリックで売却"));
		p.openInventory(inv);
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if(e.getInventory() == null) return;
		if(Util.get.uncolor(e.getWhoClicked().getOpenInventory().getTopInventory().getName()).equals("買取屋")) {
			if((e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && !sql.containName(e.getCurrentItem().getItemMeta().getDisplayName())) || e.getClick().isKeyboardClick()){
				e.setCancelled(true);
				if(e.getCurrentItem().getItemMeta().getLore() != null
						&& e.getCurrentItem().getItemMeta().getLore().contains(Util.get.color("&a&l&nクリックで売却"))) {
					e.getWhoClicked().closeInventory();
					return;
				}
			}
			new BukkitRunnable() {
			    @Override
			    public void run () {
			    	int price = 0;
					for(ItemStack item : e.getWhoClicked().getOpenInventory().getTopInventory().getContents()) {
						if(item != null) {
							price += item.getAmount() * sql.getSell(item.getItemMeta().getDisplayName());
						}
					}
					e.getWhoClicked().getOpenInventory().getTopInventory().setItem(49, Get.Item("&6&l買取額 &a" + price, Material.LIGHT_BLUE_STAINED_GLASS_PANE, "&a&l&nクリックで売却"));
			    }
			}.runTaskLater(LunaRPG.getPlugin(), 1);
		}
	}

	@EventHandler
	public void on(InventoryCloseEvent e) {
		if(e.getInventory() == null) return;
		if(Util.get.uncolor(e.getInventory().getName()).equals("買取屋")) {
			int price = 0;
			for(ItemStack item : e.getInventory()) {
				if(item == null) continue;
				price += item.getAmount() * sql.getSell(item.getItemMeta().getDisplayName());
			}
			ItemStack item = Item.getEmerald();
			item.setAmount(price);
			Util.item.giveItem(Arrays.asList((Player)e.getPlayer()), Arrays.asList(item));
		}
	}

	static class sql {
		static boolean containName(String name) {
			return (getSell(name) != 0);
		}

		static boolean containID(String id) {
			return (getName(id) != null);
		}

		static void insert(String type, String name, String id, int sell) {
			try {
				delete(id);
				MySQL.getStatement().executeUpdate("INSERT INTO " + MySQL.getDatabase() + ".Item VALUES ('" + type + "', '" + name + "', '" + id + "', " + sell + ");");
			} catch (SQLException e) {}
		}

		static void delete(String id) {
			try {
				MySQL.getStatement().executeUpdate("DELETE FROM " + MySQL.getDatabase() + ".Item WHERE ID = '" + id + "' LIMIT 1;");
			} catch (SQLException e) {}
		}

		private static String getName(String id) {
			try {
				ResultSet get = MySQL.getStatement().executeQuery("SELECT Name FROM " + MySQL.getDatabase() + ".Item WHERE ID = '" + id + "' LIMIT 1;");
				get.next();
				return get.getString("Name");
			} catch (SQLException e) {
				return null;
			}
		}

		private static int getSell(String name) {
			try {
				ResultSet get = MySQL.getStatement().executeQuery("SELECT Sell FROM " + MySQL.getDatabase() + ".Item WHERE Name = '" + name + "' LIMIT 1;");
				get.next();
				return get.getInt("Sell");
			} catch (SQLException e) {
				return 0;
			}
		}
	}
}
