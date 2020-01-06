package me.SyaRi.Inventory.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.SyaRi.Chat.ChatListener;
import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Party.Party;

public class SetMainPage {

	public static Inventory set(Player p, Inventory inv) {
		inv = chat(p, inv, 0);
		inv = party(p, inv, 2);
		inv.setItem(44, Get.Item("&6ゴミ箱", Material.LAVA_BUCKET, "&f >> &aアイテムを捨てるゴミ箱を開きます "));
		inv = Inv.setBottom(inv, false);
		if(p.hasPermission("*")) {
			inv.setItem(45, Get.Item("&6アドミンページを開く", Material.WHITE_STAINED_GLASS_PANE, ""));
		}
		return inv;
	}

	private static Inventory party(Player p, Inventory inv, int num) {
		if (!Party.isBelong(p)) {
			inv.setItem(num, Get.Item("&6パーティー", Material.WRITABLE_BOOK, "&f>> &aパーティーに所属していません"));
		} else {
			inv.setItem(num, Get.Item("&6パーティー", Material.BOOK, "&f>> &aパーティーに所属しています "));
		}
		return inv;
	}

	private static Inventory chat(Player p, Inventory inv, int num) {
		String now = "&6グローバル";
		if(ChatListener.set.containsKey(p.getUniqueId())) {
			if(ChatListener.set.get(p.getUniqueId()).equals("Party")) now = "&2パーティー";
			else if(ChatListener.set.get(p.getUniqueId()).equals("Admin")) now = "&9アドミン";
		}
		inv.setItem(num, Get.Item("&6チャット", Material.FEATHER, "&7現在の設定 : " + now));
		return inv;
	}
}
