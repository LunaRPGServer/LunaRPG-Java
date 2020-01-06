package me.SyaRi.Util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.vexsoftware.votifier.model.VotifierEvent;

import me.SyaRi.Chat.ChatListener;
import me.SyaRi.Party.Party;

public class Server implements Listener{

	private static String motd =
			Util.get.color("&6&l  LunaRPG   &a&lVersion &7: &a&l1.13.2   &a&lPlayer &7: &a&l%online% &7/ &a&l%max%\n"
						+ "&e&l        Website &7: &e&lhttps://luna-rpg.net");

	@EventHandler
	public static void on(ServerListPingEvent e) {
		e.setMotd(motd
				.replace("%online%", String.valueOf(e.getNumPlayers()))
				.replace("%max%", String.valueOf(e.getMaxPlayers())));
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		String msg = Util.get.color("&7- &a&lJoin &7&l%player%").replace("%player%", e.getPlayer().getName());
		e.setJoinMessage(msg);
		if(!e.getPlayer().isOp()) {
			Util.perm.set(e.getPlayer(), "*", false);
		} else {
			//TODO 公開前に削除
			ChatListener.set.put(e.getPlayer().getUniqueId(), "Admin");
		}
		board.set(Util.get.online());
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		String msg = Util.get.color("&7- &c&lQuit &7&l%player%").replace("%player%", e.getPlayer().getName());
		e.setQuitMessage(msg);
		if (e.getPlayer().hasPermission("debug")) {
			e.getPlayer().setOp(true);
		}
		board.set(Util.get.online());
	}

	@EventHandler
	public void on(PlayerLevelUpEvent e) {
		board.set(e.getPlayerData().getPlayer());
	}

	public static class board {
		public static void set(Player p) {
			int i;
			String s;
			ScoreboardManager m = Bukkit.getScoreboardManager();
			Scoreboard b = m.getNewScoreboard();

			@SuppressWarnings("deprecation")
			Objective o = b.registerNewObjective("Sidebar", "");
			o.setDisplayName(Util.get.color("&e★&6&l LunaRPG &e★   "));
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			o.getScore(Util.get.color("&a&m----------------------&f")).setScore(15);

			o.getScore(Util.get.color("&e - &6&lName")).setScore(14);
			o.getScore(Util.get.color("&e&l  " + p.getName())).setScore(13);

			o.getScore(Util.get.color("&e - &6&lOnline")).setScore(12);
			o.getScore(Util.get.color("&e&l  " + Bukkit.getOnlinePlayers().size() + "&7 / &e&l" + Bukkit.getMaxPlayers())).setScore(11);

			o.getScore(Util.get.color("&e - &6&lParty")).setScore(10);
			try { i = Party.Partysize(p); } catch(Exception e) { i = 0; }
			o.getScore(Util.get.color("&e&l  " + i + "&7 / &e&l5")).setScore(9);

			o.getScore(Util.get.color("&e - &6&lClass")).setScore(8);
			try { i = SkillAPI.getPlayerData(p).getMainClass().getLevel(); } catch(Exception e) { i = 0; }
			o.getScore(Util.get.color("&e&l  " + "Lv. " + i)).setScore(7);
			o.getScore(Util.get.color("&e - &6&lChat")).setScore(6);
			if(ChatListener.set.containsKey(p.getUniqueId())) s = ChatListener.set.get(p.getUniqueId());
			else s = "Global";
			o.getScore(Util.get.color("&e&l  " + s)).setScore(5);
//			o.getScore(Util.get.color("")).setScore(4);
//			o.getScore(Util.get.color("")).setScore(3);
//			o.getScore(Util.get.color("")).setScore(2);
//			o.getScore(Util.get.color("")).setScore(1);
			p.setScoreboard(b);
		}

		public static void set(List<Player> list) {
			list.forEach(p -> set(p));
		}
	}

	public static void tab() {
		for(Player p : Bukkit.getOnlinePlayers()) {

		}
	}

	private String[] log = Util.get.color(
			"&b&m-------------------------------",
			"&r   &7[&b*&7] &b%player% &fさんが",
			"&r          投票しました",
			"&r     &bhttps://vote.luna-rpg.net",
			"&b&m-------------------------------");

	@EventHandler
	public void on(VotifierEvent e) {
		for(String br : log) {
			Bukkit.broadcastMessage(br.replace("%player%", e.getVote().getUsername()));
		}
	}
}
