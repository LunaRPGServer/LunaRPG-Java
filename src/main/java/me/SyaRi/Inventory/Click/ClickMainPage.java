package me.SyaRi.Inventory.Click;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.SyaRi.Chat.ChatListener;
import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Inventory.Trash;
import me.SyaRi.Party.Party;
import me.SyaRi.Util.Server.board;

public class ClickMainPage {

	public static void click(InventoryClickEvent e) {
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		if(Get.equal(e, "パーティー")) {
			if (Party.isBelong(p)){
				Party.set.list(p);
			} else {
				Party.set.join(p);
			}
		} else if(Get.equal(e, "チャット")) chat(e);
		else if(Get.equal(e, "ゴミ箱")) Trash.open((Player) e.getWhoClicked());
		else if(Get.equal(e, "アドミンページを開く")) Inv.open((Player) e.getWhoClicked(), true);
	}

	private static void chat(InventoryClickEvent e) {
		if(e.getWhoClicked().hasPermission("*")) {
			if(ChatListener.set.containsKey(e.getWhoClicked().getUniqueId())) {
				if(ChatListener.set.get(e.getWhoClicked().getUniqueId()).equals("Admin") && Party.isBelong((Player) e.getWhoClicked())) {
					ChatListener.set.put(e.getWhoClicked().getUniqueId(), "Party");
				} else {
					ChatListener.set.remove(e.getWhoClicked().getUniqueId());
				}
			} else {
				ChatListener.set.put(e.getWhoClicked().getUniqueId(), "Admin");
			}
		} else {
			if(ChatListener.set.containsKey(e.getWhoClicked().getUniqueId())) {
				ChatListener.set.remove(e.getWhoClicked().getUniqueId());
			} else {
				if(Party.isBelong((Player) e.getWhoClicked())) {
					ChatListener.set.put(e.getWhoClicked().getUniqueId(), "Party");
				}
			}
		}
		Inv.open((Player) e.getWhoClicked(), false);
		board.set(Arrays.asList((Player) e.getWhoClicked()));
	}
}
