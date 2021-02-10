package com.nicolas;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
	private final CommandManager manager = new CommandManager();

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
	}
	
	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		
		if (user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		String prefix = Config.get("prefix");
		String raw = event.getMessage().getContentRaw();
		
		
		if (raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Config.get("owner_id"))) {
			LOGGER.info("shutting down");
			event.getJDA().shutdown();
			BotCommons.shutdown(event.getJDA());
			
			return;
		}
		
		if (raw.startsWith(prefix)) {
			manager.handle(event);
		}
	}
}
