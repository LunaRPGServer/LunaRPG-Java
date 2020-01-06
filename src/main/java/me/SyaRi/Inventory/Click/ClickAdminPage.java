package me.SyaRi.Inventory.Click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Inventory.Set.SetOther;
import me.SyaRi.Shop.Sell;

public class ClickAdminPage {

	public static void click(InventoryClickEvent e) {
		e.setCancelled(true);
		if(Get.equal(e, "タウンテレポート")) SetOther.TPlist((Player) e.getWhoClicked());
		else if(Get.equal(e, "アイテム売却")) Sell.open((Player) e.getWhoClicked());
		else if(Get.equal(e, "閉じる")) e.getWhoClicked().closeInventory();
		else if(Get.equal(e, "メインページを開く")) Inv.open((Player) e.getWhoClicked(), false);
	}
}
