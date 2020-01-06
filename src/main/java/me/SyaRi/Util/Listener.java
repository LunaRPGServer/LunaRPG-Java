package me.SyaRi.Util;

import me.SyaRi.LunaRPG;
import me.SyaRi.Chat.ChatListener;
import me.SyaRi.Command.CommandListener;
import me.SyaRi.Command.TabComplete;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Item.DropListener;
import me.SyaRi.Item.Trade;
import me.SyaRi.Item.Equip.EquipListener;
import me.SyaRi.RPG.Fish;
import me.SyaRi.Shop.Sell;
import me.SyaRi.Teleport.Click;

public class Listener {

	public Listener() {
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new Fish(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new Inv(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new Click(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new Server(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new ChatListener(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new Sell(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new EquipListener(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new DropListener(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginManager().registerEvents(new Trade(), LunaRPG.getPlugin());
		LunaRPG.getPlugin().getServer().getPluginCommand("luna").setExecutor(new CommandListener());
		LunaRPG.getPlugin().getCommand("luna").setTabCompleter(new TabComplete());
	}
}
