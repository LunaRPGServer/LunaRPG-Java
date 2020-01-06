package me.SyaRi.Discord;

import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;

import me.SyaRi.LunaRPG;
import me.SyaRi.Util.Util;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Discord extends ListenerAdapter{

	private static JDA bot;

	private class Bot {
		private final static int BuildDelay = 5;
		private final static String Token = "MzUxMzE0ODA4Njk3NzgyMjcz.Dr3XHg.iPAiKJaJh3EkHTjBpEPpc7QYpUg";
	}
	public static class Channel {
		private final static String Global =  "509405359698346021";
		private final static String Admin = "426066211957112844";
		private final static String Console = "507824260861919234";

		public static String getGlobal() {
			return Global;
		}

		public static String getAdmin() {
			return Admin;
		}

		public static String getConsole() {
			return Console;
		}
	}

	public static void setup(){
		try {
			TimeUnit.SECONDS.sleep(Bot.BuildDelay);
			bot = new JDABuilder(AccountType.BOT).setToken(Bot.Token).build();
			getBot().addEventListener(new Discord());
		} catch (InterruptedException | LoginException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if (e.getMessage() == null
				|| e.getAuthor().isBot()
				|| e.isFromType(ChannelType.PRIVATE)) return;
		if (e.getChannel().getId().equals(Channel.Global)) {

		} else if (e.getChannel().getId().equals(Channel.Admin)) {
			Bukkit.broadcastMessage(Util.get.color("&7[&5Discord&7] &f" + e.getAuthor().getName() + " &b≫ &f") + e.getMessage().getContentDisplay());
		} else if (e.getChannel().getId().equals(Channel.Console)) {
			if (e.getAuthor().equals(e.getGuild().getOwner().getUser()) || hasRole(e.getGuild(),e.getAuthor(), "Manager")) {
				Bukkit.getScheduler().runTask(LunaRPG.getPlugin(LunaRPG.class), ()
						-> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), e.getMessage().getContentDisplay()));
			}
		}
	}

	private boolean hasRole(Guild guild, User user, String role) {
		for(Role r : guild.getMember(user).getRoles()) {
			if(r.getName().equals(role)) return true;
		}
		return false;
	}

	public static JDA getBot() {
		return bot;
	}
}
