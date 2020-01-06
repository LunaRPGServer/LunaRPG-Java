package me.SyaRi.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Trash {

	public static void open(Player p) {
		Inventory inv = Get.inv(6, "&0&lゴミ箱");
		Inv.setBottom(inv, true);
		p.openInventory(inv);
	}
}