package com.nicolas.comando.comandos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;
import com.nicolas.lavaplayer.GuildMusicManager;
import com.nicolas.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.AudioManager;

public class DisconnectCommand implements ICommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(DisconnectCommand.class);
	
	@Override
	public void handle(CommandContext ctx) {
		final Member self = ctx.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		User user = ctx.getAuthor();

		if (!selfVoiceState.inVoiceChannel()) {
			return;
		}

		final GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

		guildMusicManager.scheduler.player.stopTrack();
		guildMusicManager.scheduler.queue.clear();

		final AudioManager audioManager = ctx.getGuild().getAudioManager();
		audioManager.closeAudioConnection();

		LOGGER.info(String.format("Disconnect | Comando do usuario: %s", user.getName()));

	}

	@Override
	public String getName() {
		return "disconnect";
	}

	@Override
	public List<String> getAliases() {
		List<String> list = new ArrayList<>();

		list.add("f");

		return list;
	}

//	public static void disconnect(CommandContext ctx, boolean command) {
//		
//		if (command) {
//			
//			
//		} else {
//			if (PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler.queue.isEmpty()) {
//				final AudioManager audioManager = ctx.getGuild().getAudioManager();
//				audioManager.closeAudioConnection();
//			}
//		}
//
//	}

}
