package me.SyaRi;

/*
   ■■■                        ■■■■■   ■■■■■    ■■■■ 
    ■                          ■  ■■   ■  ■■  ■   ■ 
    ■     ■   ■                ■   ■   ■   ■ ■      
    ■     ■   ■  ■■■■■   ■■■   ■  ■■   ■  ■■ ■      
    ■     ■   ■  ■   ■  ■  ■   ■■■■    ■■■■  ■    ■■
    ■     ■   ■  ■   ■  ■  ■   ■  ■    ■     ■    ■ 
    ■   ■ ■   ■  ■   ■ ■   ■   ■   ■   ■      ■   ■ 
   ■■■■■■ ■■■■■  ■   ■ ■■■■■■  ■   ■   ■       ■■■■ 
*/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.SyaRi.Discord.Discord;
import me.SyaRi.Discord.LogServerAppender;
import me.SyaRi.Util.Listener;
import me.SyaRi.Util.MySQL;
import me.SyaRi.Util.Server;
import me.SyaRi.Util.Util;

public class LunaRPG extends JavaPlugin{

	private static LunaRPG plugin;

	private Logger logger = (Logger) LogManager.getRootLogger();

	@Override
	public void onEnable() {
		plugin = this;
		new Listener();
		Discord.setup();
		logger.addAppender(new LogServerAppender());
		new MySQL().setup();

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Server.tab();
			}
		}, 0L, 5L);

	}

	@Override
	public void onDisable() {
		Bukkit.getServer().getOnlinePlayers().forEach(p -> p.kickPlayer(Util.get.color("&6&lサーバーを更新しています...\n&6&l更新後に再度ログインしてください")));
		Discord.getBot().shutdownNow();
	}
	public static LunaRPG getPlugin() {
		return plugin;
	}
}