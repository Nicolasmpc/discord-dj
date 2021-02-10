package com.nicolas.comando.comandos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;
import com.nicolas.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final Member self = ctx.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();
		
		if (selfVoiceState.inVoiceChannel()) {
			LOGGER.error("Ja estou em um canal de voz");
			channel.sendMessage("Ja estou em um canal de voz").queue();
			return;
		}
		
		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();
		
		if (!memberVoiceState.inVoiceChannel()) {
			LOGGER.error("Voce precisa estar em um canal de voz para esse comando funcionar");
			channel.sendMessage("Voce precisa estar em um canal de voz para esse comando funcionar").queue();
			return;
		}
		
		final AudioManager audioManager = ctx.getGuild().getAudioManager();
		final VoiceChannel memberChannel = memberVoiceState.getChannel();
		
		audioManager.openAudioConnection(memberChannel);
		
		LOGGER.info("Entrando em um canal de voz");
	}

	@Override
	public String getName() {
		return "join";
	}

}
