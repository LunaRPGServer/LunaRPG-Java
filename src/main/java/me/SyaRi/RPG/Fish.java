package me.SyaRi.RPG;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.SyaRi.Util.MySQL;

public class Fish implements Listener {
	@EventHandler
	public void on(PlayerFishEvent e) {
		if (e.getState().equals(State.CAUGHT_FISH)) {
			List<ItemStack> item = sql.getItem(sql.getBiome(e.getHook().getLocation().getBlock().getBiome()));
			if(item != null) {
				e.getPlayer().getInventory().addItem(item.get(new Random().nextInt(item.size())));
			}
		}
	}

	private static class sql {
		/*
		 * OCEAN
		 * COLD_OCEAN
		 * WARM_OCEAN
		 * RIVER
		 * BEACH
		 * FOREST
		 * DESERT
		 * JUNGLE
		 * SAVANNA
		 * SWAMP
		 * MOUNTAINS
		 */
		private static String getBiome(Biome biome) {
			try {
				ResultSet get = MySQL.getStatement().executeQuery("SELECT Item FROM " + MySQL.getDatabase() +
						".Fish WHERE Biome = '" + biome + "' AND WHERE Value = '-1';");
				get.next();
				return get.getString("Item");
			} catch (SQLException e) {
				return biome.toString();
			}
		}

		private static List<ItemStack> getItem(String biome) {
			try {
				ResultSet get = MySQL.getStatement().executeQuery("SELECT Item FROM " + MySQL.getDatabase() +
						".Fish WHERE Biome = '" + biome + "' AND WHERE Value != '-1';");
				List<ItemStack> list = new ArrayList<ItemStack>();
				while(get.next()) {
					ItemStack item = MythicMobs.inst().getItemManager().getItemStack(get.getString("Item"));
					for(int i = 0; i < get.getInt("Value"); i++) {
						list.add(item);
					}
				}
				return list;
			} catch (SQLException e) {
				return null;
			}
		}
	}
}
