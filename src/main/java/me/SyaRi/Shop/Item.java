package me.SyaRi.Shop;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import me.SyaRi.Util.MySQL;
import me.SyaRi.Util.Util;

public class Item {

	public static ItemStack getEmerald() {
		return MythicMobs.inst().getItemManager().getItemStack("Money_Emerald");
	}

	public static void run(CommandSender sender, String[] args) {
		if(args.length > 4 && args[1].equalsIgnoreCase("add")) {
			if(args[2].equalsIgnoreCase("mm")) {
				if(check.containMythicMobsItem(args[3])) {
					int price;
					ItemStack item = MythicMobs.inst().getItemManager().getItemStack(args[3]);
					try {
						price = Integer.parseInt(args[4]);
					} catch (NumberFormatException e) {
						send.help(sender);
						return;
					}
					Sell.sql.insert("MythicMobs", item.getItemMeta().getDisplayName(), args[3], price);
					send.add(sender, args[3]);
				} else {
					send.error.NotFound(sender);
				}
				return;
			}
		} else if(args.length > 2 && args[1].equalsIgnoreCase("edit")) {
			if(Sell.sql.containID(args[2])) {
				int price;
				try {
					price = Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					send.error.NotValue(sender);
					return;
				}
				try {
					MySQL.getStatement().executeUpdate("UPDATE " + MySQL.getDatabase() + ".Item SET Sell = '" + price + "' WHERE ID = '" + args[2] + "' ;" + "");
				} catch (SQLException e) {
					return;
				}
				send.edit(sender, args[2], price);
				return;
			}
		} else if(args.length > 2 && args[1].equalsIgnoreCase("delete")) {
			if(Sell.sql.containID(args[2])) {
				Sell.sql.delete(args[2]);
				send.delete(sender, args[2]);
				return;
			}
		} else if(args.length > 2 && args[1].equalsIgnoreCase("reset")) {
			if(args[2].equalsIgnoreCase("all")) {
				// TODO
				return;
			} else if(Sell.sql.containID(args[2])) {
				if(check.containMythicMobsItem(args[2])) {
					int price = get.price(args[2]);
					ItemStack item = MythicMobs.inst().getItemManager().getItemStack(args[3]);
					Sell.sql.insert("MythicMobs", item.getItemMeta().getDisplayName(), args[3], price);
				} else {
					Sell.sql.delete(args[2]);
				}
				return;
			}
		} else if(args.length > 2 && args[1].equals("get")) {
			if(!(sender instanceof Player)) {
				Util.send.error.OnlyPlayer(sender);
				return;
			}
			int cnt = 1;
			if(args.length > 3) {
				try {
					cnt = Integer.parseInt(args[3]);
				} catch(NumberFormatException e) {}
			}
			if(get.type(args[2]).equals("MythicMobs")) {
				ItemStack item = MythicMobs.inst().getItemManager().getItemStack(args[2]);
				item.setAmount(cnt);
				((Player) sender).getInventory().addItem(item);
			} else if(get.type(args[2]).equals("DivineItemsRPG")) {

			} else {
				send.error.NotFound(sender);
			}
		}
		send.help(sender);
	}

	private static class get {
		private static int price(String id) {
			try {
				ResultSet get = MySQL.getStatement().executeQuery("SELECT Sell FROM" + MySQL.getDatabase() + ".Item WHERE ID = '" + id + "' LIMIT 1;");
				get.next();
				return get.getInt("Sell");
			} catch (SQLException e) {
				return 0;
			}
		}

		private static String type(String id) {
			try {
				ResultSet get = MySQL.getStatement().executeQuery("SELECT Type FROM" + MySQL.getDatabase() + ".Item WHERE ID = '" + id + "' LIMIT 1;");
				get.next();
				return get.getString("Type");
			} catch (SQLException e) {
				return null;
			}
		}
	}

	public static class check {
		public static boolean containMythicMobsItem(String name) {
			for(MythicItem item : MythicMobs.inst().getItemManager().getItems()) {
				if(item.getDisplayName() != null && item.getInternalName().equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	public static class send {
		public static void help(CommandSender sender) {
			Util.send.help(sender,
					"/luna item add mm <ID> <price>",
					"/luna item edit <ID> <price>",
					"/luna item delete <ID>",
					"/luna item reset all",
					"/luna item reset <ID>");
		}



		private static void add(CommandSender sender, String name) {
			Util.send.msg(sender, "&6" + name + "&f をデータベースに追加しました");
		}

		private static void delete(CommandSender sender, String name) {
			Util.send.msg(sender, "&6" + name + "&f をデータベースから削除しました");
		}

		private static void edit(CommandSender sender, String name, int price) {
			Util.send.msg(sender, "&6" + name + "&f の買取価格を &6" + price + "&f に変更しました");
		}

		private static class error {
			private static void NotValue(CommandSender sender) {
				Util.send.msg(sender, "&f価格を入力してください");
			}

			private static void NotFound(CommandSender sender) {
				Util.send.msg(sender, "&f存在しないアイテムです");
			}
		}
	}
}
