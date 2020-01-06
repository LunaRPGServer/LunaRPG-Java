package me.SyaRi.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.SyaRi.Inventory.Inv;
import me.SyaRi.Shop.Item;
import me.SyaRi.Teleport.Click;
import me.SyaRi.Teleport.Town;
import me.SyaRi.Util.Util;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("luna") && sender.hasPermission("*")) {
			if (args.length > 0 && args[0].equalsIgnoreCase("town")) {
				Town.run(sender, args);
			} else if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
				if(Util.check.isPlayer(sender)) Inv.open((Player) sender, false);
			} else if (args.length > 0 && args[0].equalsIgnoreCase("click")) {
				if(Util.check.isPlayer(sender)) Click.run((Player) sender, args);
			} else if (args.length > 0 && args[0].equalsIgnoreCase("item")) {
				Item.run(sender, args);
			} else {
				Util.send.help(sender,
						"/luna town",
						"/luna click");
			}
		}
		return true;
	}
}
