package me.SyaRi.Inventory.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Teleport.Town;

public class SetOther {
	public static void TPlist(Player p) {
		Inventory inv = Get.inv(6, "&0&lタウンテレポート");
		int i = 0;
		for(String name : Town.sql.get.list()) {
			inv.setItem(i, Get.Item("&6" + name, Material.MAP, ""));
			i ++;
		}
		inv = Inv.setBottom(inv, true);
		p.openInventory(inv);
	}
}
