package me.SyaRi.Chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.SyaRi.Discord.Discord;
import me.SyaRi.Party.Party;
import me.SyaRi.Util.Util;

public class ChatListener implements Listener {

	public static Map<UUID, String> set = new HashMap<>();

	@EventHandler
	public void on(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		String c, msg;
		Player p = e.getPlayer();
		if (e.getMessage().startsWith("@")) {
			// Private Message
		}
		if (e.getMessage().startsWith(".")) {
			if(e.getMessage().equals(".")) return;
			msg = e.getMessage().substring(1, e.getMessage().length());
		} else if (Kana.conv(e.getMessage()).equals(e.getMessage()) || !e.getMessage().matches("^[!-~]+$") || e.getMessage().matches("^[^a-zA-Z]+$")) {
			msg = e.getMessage();
		} else {
			msg = getMessage(e);
		}
		if (set.containsKey(p.getUniqueId())) {
			if(set.get(p.getUniqueId()).equals("Admin") && p.hasPermission("*")) {
				c = getTemplete("&9&lAdmin", p, msg);
				Bukkit.getOperators().stream().filter(OfflinePlayer::isOnline).forEach(op -> op.getPlayer().sendMessage(c));
				Bukkit.getConsoleSender().sendMessage(c);
				Discord.getBot().getTextChannelById(Discord.Channel.getAdmin()).sendMessage(Util.get.uncolor("**" + p.getName() + "** ≫ " + msg)).queue();
				return;
			} else if (set.get(p.getUniqueId()).equals("Party") && Party.isBelong(p)) {
				c = getTemplete("&2&lParty", p, msg);
				Party.SendinParty(p.getUniqueId(), c);
				Bukkit.getConsoleSender().sendMessage(c);
				return;
			}
		}
		c = getTemplete("&6&lGlobal", p, msg);
		Bukkit.broadcastMessage(c);
		Discord.getBot().getTextChannelById(Discord.Channel.getGlobal()).sendMessage(Util.get.uncolor("**" + p.getName() + "** ≫ " + msg)).queue();
		return;
	}

	private String getMessage(AsyncPlayerChatEvent e) {
		String msg = Util.get.uncolor(e.getMessage());
		return Kana.conv(msg) + " &b(" + msg + ")";
	}

	private String getTemplete(String prefix, Player p, String msg) {
		return Util.get.color(prefix + " &f" + p.getName() + "&r &b≫ &r" + msg);
	}
}
