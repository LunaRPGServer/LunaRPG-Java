package me.SyaRi.Item.Equip;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.SyaRi.Util.Util;

public class EquipBuilder {

	public static ItemStack create(Material material, String name, List<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Util.get.color(name));
		meta.setLore(Util.get.color(lore));
		item.setItemMeta(meta);
		return item;
	}

	public static List<String> lore(String type, String rarity, int lvl) {
		List<String> lore = new ArrayList<>();
		lore.add(template.type(type));
		return lore;
	}

	private static class template {
		private static String type(String type) {
			String ret = "&7アイテムタイプ: ";
			return Util.get.color(ret + type);
		}
	}
}
