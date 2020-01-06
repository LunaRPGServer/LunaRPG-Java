package me.SyaRi.Inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.SyaRi.Util.Util;

public class Get {

	public static ItemStack Item(String name, Material mat, String ... desc) {
		ItemStack i = new ItemStack(mat, 1);
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(Util.get.color(name));
		iMeta.setLore(Arrays.asList(Util.get.color(desc)));
		i.setItemMeta(iMeta);
		return i;
	}

	public static ItemStack ItemHead(String name, Player head, String ... desc) {
		ItemStack i =Item(name, Material.PLAYER_HEAD, desc);
		SkullMeta sk = (SkullMeta) i.getItemMeta();
		sk.setOwningPlayer(head);
		i.setItemMeta(sk);
		return i;
	}

	public static boolean equal(InventoryClickEvent e, String s) {
		return Util.get.uncolor(e.getCurrentItem().getItemMeta().getDisplayName()).equals(s);
	}

	public static Inventory inv(int line, String name) {
		return Bukkit.createInventory(null, line*9, Util.get.color(name));
	}
}
