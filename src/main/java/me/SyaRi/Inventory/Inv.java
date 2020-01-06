package me.SyaRi.Inventory;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;

import me.SyaRi.Inventory.Click.ClickAdminPage;
import me.SyaRi.Inventory.Click.ClickMainPage;
import me.SyaRi.Inventory.Click.ClickOther;
import me.SyaRi.Inventory.Set.SetAdminPage;
import me.SyaRi.Inventory.Set.SetMainPage;
import me.SyaRi.Party.Party;
import me.SyaRi.Util.Util;

public class Inv implements Listener{

	public static void open(Player p, boolean Admin) {
		if(Admin && p.hasPermission("*")) {
			Inventory inv = Get.inv(6, "&0&lアドミンメニュー");
			SetAdminPage.set(p, inv);
			p.openInventory(inv);
		} else {
			Inventory inv = Get.inv(6, "&0&lメインメニュー");
			SetMainPage.set(p, inv);
			p.openInventory(inv);
		}
	}

	@EventHandler
	public void onMenu(InventoryClickEvent e) {
		if (e.getInventory() == null
				|| e.getInventory().getName().equals("container.inventory")
				|| e.getInventory().getName().equals("container.crafting")
				|| e.getCurrentItem() == null
				|| e.getCurrentItem().getType().equals(Material.VOID_AIR)
				|| e.getCurrentItem().getType().equals(Material.AIR)
				|| !e.getCurrentItem().hasItemMeta()) {
			return;
		}
		switch (Util.get.uncolor(e.getClickedInventory().getName())) {
		case "アドミンメニュー":
			ClickAdminPage.click(e);
			break;
		case "メインメニュー":
			ClickMainPage.click(e);
			break;
		case "ゴミ箱":
			ClickOther.Trash(e);
			break;
		case "タウンテレポート":
			ClickOther.Townlist(e);
			break;
		case "パーティー加入":
			Party.click.join(e);
			break;
		case "パーティー一覧":
			Party.click.list(e);
			break;
		case "パーティー招待":
			Party.click.invite(e);
			break;
		case "パーティーアイコン":
			Party.click.icon(e);
			break;
		}
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		e.getPlayer().getInventory().setItem(8, Get.Item("&6&lメニュー", Material.BOOK, "&f >> &a右クリックするとメニューが開きます "));
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getClick().equals(ClickType.CREATIVE)) return;
		if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(Util.get.color("&6&lメニュー"))) {
			if(!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
				((Player) e.getWhoClicked()).updateInventory();
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equals(Util.get.color("&6&lメニュー"))){
			e.setCancelled(true);
			open(e.getPlayer(), false);
		}
	}

	@EventHandler
	public void on(PlayerSwapHandItemsEvent e) {
		if (e.getMainHandItem() != null && e.getMainHandItem().getItemMeta().getDisplayName().equals(Util.get.color("&6&lメニュー"))) {
			e.setCancelled(true);
		}
	}

	public static Inventory setBottom(Inventory inv, boolean ret) {
		for (int i = 0; i < 9; i++) {
			inv.setItem((45 + i), Get.Item("&0", Material.GRAY_STAINED_GLASS_PANE, ""));
		}
		if (ret) {
			inv.setItem(49, Get.Item("&f&l戻る", Material.RED_STAINED_GLASS_PANE, ""));
		} else {
			inv.setItem(49, Get.Item("&f&l閉じる", Material.RED_STAINED_GLASS_PANE, ""));
		}
		return inv;
	}
}
