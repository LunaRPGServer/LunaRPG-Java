package me.SyaRi.Teleport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.SyaRi.LunaRPG;
import me.SyaRi.Util.MySQL;
import me.SyaRi.Util.Util;

public class Click implements Listener{

	private static Map<UUID, ClickData> list = new HashMap<>();

	public static void run(Player p, String[] args) {
		if(!list.containsKey(p.getUniqueId())) list.put(p.getUniqueId(), new ClickData());
		if(args.length == 2 && args[1].equalsIgnoreCase("from")) {
			Location loc = p.getTargetBlock(null, 32).getLocation();
			list.get(p.getUniqueId()).setFrom(loc);
			send.SetFrom(p, loc);
		} else if(args.length == 2 && args[1].equalsIgnoreCase("to")) {
			Location loc = p.getLocation();
			list.get(p.getUniqueId()).setTo(loc);
			send.SetTo(p, loc);
		} else if(args.length == 2 && args[1].equalsIgnoreCase("set")) {
			if(check(p)) {
				Location F = list.get(p.getUniqueId()).getFrom();
				Location T = list.get(p.getUniqueId()).getTo();
				sql.insert(F.getWorld().getName(), F.getX(), F.getY(), F.getZ(),
						T.getWorld().getName(), T.getX(), T.getY(), T.getZ());
				list.remove(p.getUniqueId());
				send.Set(p);
			}
		} else if(args.length == 2 && args[1].equalsIgnoreCase("delete")) {
			if(list.get(p.getUniqueId()).isSetFrom()) {
				String FW = list.get(p.getUniqueId()).getFrom().getWorld().getName();
				double FX = list.get(p.getUniqueId()).getFrom().getX();
				double FY = list.get(p.getUniqueId()).getFrom().getY();
				double FZ = list.get(p.getUniqueId()).getFrom().getZ();
				sql.delete(FW, FX, FY, FZ);
				send.Delete(p);
			} else {
				String FW = p.getTargetBlock(null, 32).getWorld().getName();
				double FX = p.getTargetBlock(null, 32).getLocation().getX();
				double FY = p.getTargetBlock(null, 32).getLocation().getY();
				double FZ = p.getTargetBlock(null, 32).getLocation().getZ();
				sql.delete(FW, FX, FY, FZ);
				send.Delete(p);
			}
		} else if(args.length == 3 && args[1].equalsIgnoreCase("check") && args[2].equalsIgnoreCase("block")) {
			Block block = p.getTargetBlock(null, 32);
			if(block.isEmpty()) {
				send.CheckBlockIsAirError(p);
			} else {
				send.CheckBlock(p, block);
			}
		} else if(args.length == 3 && args[1].equalsIgnoreCase("check") && args[2].equalsIgnoreCase("player")) {
			send.CheckPlayer(p);
		} else {
			send.Help(p);
		}
	}

	private static boolean check(Player p) {
		if(list.get(p.getUniqueId()).isSetFrom() && list.get(p.getUniqueId()).isSetTo()) {
			return true;
		} else {
			if(!list.get(p.getUniqueId()).isSetFrom()) send.NotSetFromError(p);
			if(!list.get(p.getUniqueId()).isSetTo()) send.NotSetToError(p);
			return false;
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		if (!e.getPlayer().isSneaking() && e.getClickedBlock() != null) {
			String FW = e.getClickedBlock().getWorld().getName();
			double FX = e.getClickedBlock().getLocation().getX();
			double FY = e.getClickedBlock().getLocation().getY();
			double FZ = e.getClickedBlock().getLocation().getZ();
			String TW = sql.get.ToWorld(FW, FX, FY, FZ);
			if(TW != null) {
				double TX = sql.get.ToX(FW, FX, FY, FZ);
				double TY = sql.get.ToY(FW, FX, FY, FZ);
				double TZ = sql.get.ToZ(FW, FX, FY, FZ);
				Float yaw = e.getPlayer().getLocation().getYaw();
				Float pitch = e.getPlayer().getLocation().getPitch();
				e.setCancelled(true);
				Location loc = new Location(LunaRPG.getPlugin().getServer().getWorld(TW), TX, TY, TZ, yaw, pitch);
				Util.run.tp(e.getPlayer(), loc);
			}
		}
	}

	private static class send{
		private static void Help(CommandSender sender) {
			Util.send.help(sender,
					"/luna click from",
					"/luna click to",
					"/luna click set",
					"/luna click check block",
					"/luna click check player"
					);
		}

		private static void SetFrom(Player p, Location loc) {
			Util.send.msg(p, "&fブロックが選択しました &7(%w%, %x%, %y%, %z%)"
					.replace("%w%", String.valueOf(loc.getWorld().getName()))
					.replace("%x%", String.valueOf(loc.getX()))
					.replace("%y%", String.valueOf(loc.getY()))
					.replace("%z%", String.valueOf(loc.getZ())));
		}

		private static void SetTo(Player p, Location loc) {
			Util.send.msg(p, "&fテレポート先が選択しました &7(%w%, %x%, %y%, %z%)"
					.replace("%w%", String.valueOf(loc.getWorld().getName()))
					.replace("%x%", String.valueOf(loc.getX()))
					.replace("%y%", String.valueOf(loc.getY()))
					.replace("%z%", String.valueOf(loc.getZ())));
		}

		private static void Set(Player p) {
			Util.send.msg(p, "&fテレポートを追加しました");
		}

		private static void Delete(Player p) {
			Util.send.msg(p, "&fテレポートが削除しました");
		}

		private static void NotSetFromError(Player p) {
			Util.send.msg(p, "&fブロックが選択していません");
		}

		private static void NotSetToError(Player p) {
			Util.send.msg(p, "&fテレポート先が選択していません");
		}

		private static void CheckBlock(Player p, Block block) {
			Util.send.msg(p, "&fBlock &6World&7: &f%w% &7/ &6Location&7: &f%x%, %y%, %z%"
					.replace("%x%", String.valueOf(block.getLocation().getX()))
					.replace("%y%", String.valueOf(block.getLocation().getY()))
					.replace("%z%", String.valueOf(block.getLocation().getZ()))
					.replace("%w%", String.valueOf(block.getWorld().getName())));
		}

		private static void CheckBlockIsAirError(Player p) {
			Util.send.msg(p, "&fブロックがありません");
		}

		private static void CheckPlayer(Player p) {
			Util.send.msg(p, "&fPlayer &6World&7: &f%w% &7/ &6Location&7: &f%x%, %y%, %z%"
					.replace("%x%", String.valueOf(p.getLocation().getX()))
					.replace("%y%", String.valueOf(p.getLocation().getY()))
					.replace("%z%", String.valueOf(p.getLocation().getZ()))
					.replace("%w%", String.valueOf(p.getWorld().getName())));
		}
	}

	private static class sql {
		private static void insert(String FW, double FX, double FY, double FZ, String TW, double TX, double TY, double TZ) {
			try {
				delete(FW, FX, FY, FZ);
				MySQL.getStatement().executeUpdate("INSERT INTO " + MySQL.getDatabase() + ".Click " +
					"VALUES ('" + FW + "', " + FX + ", " + FY + ", " + FZ + ", '" + TW + "', " + TX + ", " + TY + ", " + TZ + ");");
			} catch (SQLException e) {}
		}

		private static void delete(String FW, double FX, double FY, double FZ) {
			try {
				MySQL.getStatement().executeUpdate("DELETE FROM " + MySQL.getDatabase() + ".Click " +
					"WHERE FromWorld = '" + FW + "'" + " AND FromX = " + FX + " AND FromY = " + FY + " AND FromZ = " + FZ + " LIMIT 1;");
			} catch (SQLException e) {}
		}

		private static class get {
			private static String ToWorld(String FW, double FX, double FY, double FZ) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT ToWorld FROM " + MySQL.getDatabase() + ".Click " +
						"WHERE FromWorld = '" + FW + "'" + " AND FromX = " + FX + " AND FromY = " + FY + " AND FromZ = " + FZ + " LIMIT 1;");
					get.next();
					return get.getString(1);
				} catch (SQLException e) {
					return null;
				}
			}

			private static double ToX(String FW, double FX, double FY, double FZ) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT ToX FROM " + MySQL.getDatabase() + ".Click " +
						"WHERE FromWorld = '" + FW + "'" + " AND FromX = " + FX + " AND FromY = " + FY + " AND FromZ = " + FZ + " LIMIT 1;");
					get.next();
					return get.getDouble(1);
				} catch (SQLException e) {
					return 0;
				}
			}

			private static double ToY(String FW, double FX, double FY, double FZ) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT ToY FROM " + MySQL.getDatabase() + ".Click " +
						"WHERE FromWorld = '" + FW + "'" + " AND FromX = " + FX + " AND FromY = " + FY + " AND FromZ = " + FZ + " LIMIT 1;");
					get.next();
					return get.getDouble(1);
				} catch (SQLException e) {
					return 0;
				}
			}

			private static double ToZ(String FW, double FX, double FY, double FZ) {
				try {
					ResultSet get = MySQL.getStatement().executeQuery("SELECT ToZ FROM " + MySQL.getDatabase() + ".Click " +
						"WHERE FromWorld = '" + FW + "'" + "AND FromX = " + FX + "AND FromY = " + FY + "AND FromZ = " + FZ + " LIMIT 1;");
					get.next();
					return get.getDouble(1);
				} catch (SQLException e) {
					return 0;
				}
			}
		}
	}
}
