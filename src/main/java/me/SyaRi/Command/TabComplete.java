package me.SyaRi.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.SyaRi.Teleport.Town;

public class TabComplete implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		String[] com;
		ArrayList<String> list = new ArrayList<String>();
		if (args.length == 1) {
			com = new String[]{"town", "menu", "click"};
			if(args[0].length() == 0) return Arrays.asList(com);
			else {
				for (String s : com) {
					if (s.startsWith(args[0])) list.add(s);
				}
			}
		} else if (args.length > 1) {
			switch (args[0]) {
				case "town":
					if (args.length == 2) {
						com = new String[]{"add", "remove", "list", "tp"};
						for (String s : com) {
							if (s.startsWith(args[1])) list.add(s);
						}
					} else if((args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("tp"))) {
						for(String s : Town.sql.get.list()) {
							if (s.startsWith(args[2])) list.add(s);
						}
					}
					break;
				case "click":
					if (args.length == 2) {
						com = new String[] { "from", "to", "set", "delete", "check" };
						for (String s : com) {
							if (s.startsWith(args[1])) list.add(s);
						}
					} else if (args[1].equalsIgnoreCase("check")) {
						com = new String[] { "block", "player" };
						for (String s : com) {
							if (s.startsWith(args[2])) list.add(s);
						}
					}
					break;
			}
		}
		Collections.sort(list);
		return list;
	}
}
