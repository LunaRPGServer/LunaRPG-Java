package me.SyaRi.Discord;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LogServerAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class LogServerAppender extends AbstractAppender {

	public LogServerAppender() {
		super("LuanRPG", null, null);
	}

	private Date date = new Date();
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	@Override
	public void append(LogEvent e) {
		date.setTime(System.currentTimeMillis());
		String msg = "[" + format.format(date) + " " + e.getLevel().name() + "] " + e.getMessage().getFormattedMessage().replaceAll("\\u001b\\[[^m.]*m", "");
		Discord.getBot().getTextChannelById(Discord.Channel.getConsole()).sendMessage((msg)).queue();
	}
}