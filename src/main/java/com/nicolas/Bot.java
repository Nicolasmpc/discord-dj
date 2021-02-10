package com.nicolas;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {

	public Bot() throws LoginException {
		
		JDA jda = JDABuilder.createDefault(
							Config.get("token"),
							GatewayIntent.GUILD_MEMBERS,
							GatewayIntent.GUILD_MESSAGES,
							GatewayIntent.GUILD_VOICE_STATES,
							GatewayIntent.GUILD_PRESENCES
							)
							.disableCache(EnumSet.of(
									CacheFlag.CLIENT_STATUS,
									CacheFlag.ACTIVITY,
									CacheFlag.EMOTE
							))
				.build();
		jda.addEventListener(new Listener());
		
	}

	public static void main(String[] args) throws LoginException {
		new Bot();
	}
}
