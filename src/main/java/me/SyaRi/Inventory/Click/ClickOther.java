package me.SyaRi.Inventory.Click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Teleport.Town;
import me.SyaRi.Util.Util;

public class ClickOther {

	public static void Trash(InventoryClickEvent e) {
		e.setCancelled(true);
		if(Get.equal(e, "戻る")) Inv.open((Player) e.getWhoClicked(), false);
	}

	public static void Townlist(InventoryClickEvent e) {
		e.setCancelled(true);
		if (Get.equal(e, "戻る")) Inv.open((Player) e.getWhoClicked(), true);
		else Town.TP(Util.get.uncolor(e.getCurrentItem().getItemMeta().getDisplayName()), (Player) e.getWhoClicked(), Util.get.console());
	}
}
