package me.SyaRi.Inventory.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;

public class SetAdminPage {

	public static Inventory set(Player p, Inventory inv) {
		inv.setItem(0, Get.Item("&6タウンテレポート", Material.MAP, "&f >> &aタウンの一覧を表示します "));
		inv.setItem(2, Get.Item("&6アイテム売却", Material.EMERALD, "&f >> &aアイテムの売却ページを開きます "));
		Inv.setBottom(inv, false);
		inv.setItem(53, Get.Item("&6メインページを開く", Material.WHITE_STAINED_GLASS_PANE, ""));
		return inv;
	}
}
