package me.SyaRi.Teleport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.SyaRi.LunaRPG;
import me.SyaRi.Util.MySQL;
import me.SyaRi.Util.Util;

public class Town {

	public static void run(CommandSender sender, String[] args) {
		if(args.length == 3 && args[1].equalsIgnoreCase("add")) {
			if (Util.check.isPlayer(sender)) {
				String w = ((Player) sender).getWorld().getName();
				double x = ((Player) sender).getLocation().getX();
				double y = ((Player) sender).getLocation().getY();
				double z = ((Player) sender).getLocation().getZ();
				sql.insert(args[2], w, x, y, z);
				send.AddSuccess(sender, args[2]);
			}
		} else if(args.length == 3 && args[1].equalsIgnoreCase("remove")) {
			sql.delete(args[2]);
			send.RemoveSuccess(sender, args[2]);
		} else if(args.length == 2 && args[1].equalsIgnoreCase("list")) {
			send.List(sender);
		} else if(args.length == 3 && args[1].equalsIgnoreCase("tp")) {
			if (Util.check.isPlayer(sender)) {
				TP(args[2], (Player)sender, (Player)sender);
			}
		} else if(args.length == 4 && args[1].equalsIgnoreCase("tp")) {
			Player target = LunaRPG.getPlugin().getServer().getPlayer(args[2]);
			if (target != null) TP(args[3], target, sender);
		} else {
			send.Help(sender);
		}
	}

	public static void TP(String name, Player target, CommandSender sender) {
		String w = sql.get.World(name);
		if(w != null) {
			if ((target.equals(sender)) || (target.getWorld().equals(LunaRPG.getPlugin().getServer().getWorld(w)))) {
				Double x = sql.get.X(name);
				Double y = sql.get.Y(name);
				Double z = sql.get.Z(name);
				float pitch = target.getLocation().getPitch();
				float yaw = target.getLocation().getYaw();
				Location loc = new Location(LunaRPG.getPlugin().getServer().getWorld(w), x, y, z, yaw, pitch);
				send.TPSuccess(sender);
				Util.run.tp(target, loc);
			} else {
				send.TPnotPerm(sender);
			}
		} else {
			send.NotFoundError();
		}
	}

	private static class send{
		private static void Help(CommandSender sender) {
			Util.send.help(sender,
					"/luna town add <name>",
					"/luna town remove <name>",
					"/luna town list",
					"/luna town tp <name>",
					"/luna town tp <player> <name>");
		}

		public static void List(CommandSender sender) {
			Util.send.msg(sender, "&fタウンリスト");
			Util.send.list(sender, sql.get.list());
		}

		private static void AddSuccess(CommandSender sender, String name) {
			Util.send.msg(sender, "&f" + name + "を追加しました");
		}

		private static void RemoveSuccess(CommandSender sender, String name) {
			Util.send.msg(sender, "&f" + name + "を削除しました");
		}

		private static void TPSuccess(CommandSender sender) {
			Util.send.msg(sender, "&fテレポートしました");
		}

		private static void TPnotPerm(CommandSender sender) {
			Util.send.msg(sender, "&fテレポートが許可されていません");
		}

		private static void NotFoundError() {
			Util.send.msg(Util.get.console(), "&f存在しない街です");
		}
	}

	public static class sql {
		private static void insert(String Name, String World, double x, double y, double z) {
			try {
				delete(Name);
				MySQL.getStatement().executeUpdate("INSERT INTO " + MySQL.getDatabase() + ".Town VALUES ('" + Name + "', '" + World + "', " + x + ", " + y + ", " + z + ");");
			} catch (SQLException e) {}
		}

		private static void delete(String Name) {
			try {
				MySQL.getStatement().executeUpdate("DELETE FROM " + MySQL.getDatabase() + ".Town WHERE Name = '" + Name + "' LIMIT 1;");
			} catch (SQLException e) {}
		}

		public static class get {
			public static List<String> list(){
				List<String> list = new ArrayList<>();
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT Name FROM " + MySQL.getDatabase() + ".Town;");
					while (get.next()) {
						list.add(get.getString(1));
					}
				} catch (SQLException e) {}
				return list;
			}

			private static String World(String Name) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT World FROM " + MySQL.getDatabase() + ".Town WHERE Name = '" + Name + "' LIMIT 1;");
					get.next();
					return get.getString(1);
				} catch (SQLException e) {
					return null;
				}
			}
			private static double X(String Name) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT X FROM " + MySQL.getDatabase() + ".Town WHERE Name = '" + Name + "' LIMIT 1;");
					get.next();
					return get.getDouble(1);
				} catch (SQLException e) {
					return 0;
				}
			}
			private static double Y(String Name) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT Y FROM " + MySQL.getDatabase() + ".Town WHERE Name = '" + Name + "' LIMIT 1;");
					get.next();
					return get.getDouble(1);
				} catch (SQLException e) {
					return 0;
				}
			}
			private static double Z(String Name) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT Z FROM " + MySQL.getDatabase() + ".Town WHERE Name = '" + Name + "' LIMIT 1;");
					get.next();
					return get.getDouble(1);
				} catch (SQLException e) {
					return 0;
				}
			}
		}
	}
}
