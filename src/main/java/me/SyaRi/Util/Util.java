package me.SyaRi.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.SyaRi.LunaRPG;

public class Util {
	public static class run {
		public static void tp(Player p, Location loc) {
			for (Player of : Bukkit.getServer().getOnlinePlayers()) {
				of.hidePlayer(LunaRPG.getPlugin(), p);
			}
			p.teleport(loc);
			for (Player of : Bukkit.getServer().getOnlinePlayers()) {
				of.showPlayer(LunaRPG.getPlugin(),p);
			}
		}
	}

	public static class send {
		public static void msg(CommandSender sender, String msg) {
			sender.sendMessage(get.color(get.prefix.msg() + " " + msg));
		}

		public static void help(CommandSender sender, String ... msg) {
			sender.sendMessage(get.color("&6====== &7&lCommands &6======"));
			for(String c : msg) {
				sender.sendMessage(get.color("&7 ≫ &a" + c));
			}
		}

		public static void list(CommandSender sender, List<String> list) {
			for(String c : list) {
				sender.sendMessage(get.color(" &6> &7" + c));
			}
		}

		public static class error {
			public static void OnlyPlayer(CommandSender sender) {
				sender.sendMessage(Util.get.color(get.prefix.msg() + "&f コンソールから実行できないコマンドです"));
			}

			public static void OfflinePlayer(CommandSender sender) {
				sender.sendMessage(Util.get.color(get.prefix.msg() + "&f オフラインのプレイヤーです"));
			}
		}
	}

	public static class get {
		public static class prefix {
			public static String msg() {
				return "&7[&a#&7]&r";
			}
		}

		public static String color(String s) {
			return ChatColor.translateAlternateColorCodes('&', s);
		}

		public static String[] color(String ... ss) {
			List<String> ret = new ArrayList<>();
			for(String s : ss) {
				ret.add(color(s));
			}
			return (String[]) ret.toArray(new String[ret.size()]);
		}

		public static List<String> color(List<String> ss) {
			List<String> ret = new ArrayList<>();
			for(String s : ss) {
				ret.add(color(s));
			}
			return ret;
		}

		public static String uncolor(String s) {
			return ChatColor.stripColor(get.color(s));
		}

		public static String[] uncolor(String ... ss) {
			List<String> ret = new ArrayList<>();
			for(String s : ss) {
				ret.add(uncolor(s));
			}
			return (String[]) ret.toArray();
		}

		public static Player player(UUID uuid) {
			return Bukkit.getPlayer(uuid);
		}

		public static Player player(String s) {
			try {
				return Bukkit.getPlayer(s).getPlayer();
			} catch (Exception e) {
				return null;
			}
		}

		public static CommandSender console() {
			return Bukkit.getConsoleSender();
		}

		public static List<Player> online(){
			List<Player> list = new ArrayList<>();
			Bukkit.getServer().getOnlinePlayers().forEach(p -> list.add(p));
			return list;
		}
	}

	public static class check {
		public static boolean isPlayer(CommandSender sender) {
			if (sender instanceof Player) {
				return true;
			} else {
				send.error.OnlyPlayer(sender);
				return false;
			}
		}
	}

	public static class perm {
		public static void set(Player p, String perm, boolean bool) {
			p.addAttachment(LunaRPG.getPlugin()).setPermission(perm, bool);
		}

		public static void unset(Player p, String perm) {
			p.addAttachment(LunaRPG.getPlugin()).unsetPermission(perm);
		}
	}

	public static class item {
		public static void dropItem(Location loc, List<ItemStack> drops, List<Player> pp) {
			for(ItemStack drop : drops) {
				Item i = loc.getWorld().dropItemNaturally(loc, drop);
				pp.forEach(p -> i.addScoreboardTag(p.getUniqueId().toString()));
			}
		}

		public static void giveItem(List<Player> pp, List<ItemStack> items) {
			for(Player p : pp) {
				boolean drop = false;
				for(ItemStack item : items) {
					if(item != null) {
						int em = p.getInventory().firstEmpty();
						if(em == -1) {
							dropItem(p.getLocation(), Arrays.asList(item), Arrays.asList(p));
							if(!drop) drop = true;
						} else {
							p.getInventory().addItem(item);
						}
					}
				}
				if(drop) {
					p.sendMessage(Util.get.color(Util.get.prefix.msg() + " &cアイテムを全て受け取ることが出来ませんでした"));
				}
			}
		}
	}
}
