package com.nicolas.comando.comandos;

import java.util.ArrayList;
import java.util.List;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;
import com.nicolas.lavaplayer.GuildMusicManager;
import com.nicolas.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final Member self = ctx.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if (!selfVoiceState.inVoiceChannel()) {
			channel.sendMessage("Eu preciso estar em um canal de voz para este comando funcionar").queue();
			return;
		}

		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			channel.sendMessage("Voce precisa estar em um canal de voz para esse comando funcionar").queue();
			return;
		}

		if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
			channel.sendMessage("Voce precisa estar no mesmo canal de voz que eu para este comando funcionar").queue();
			return;
		}
		
		final GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
		
		guildMusicManager.scheduler.player.stopTrack();
		guildMusicManager.scheduler.queue.clear();
		
		channel.sendMessage("Fila Limpa").queue();

	}

	@Override
	public String getName() {
		return "stop";
	}
	
	@Override
	public List<String> getAliases() {
		List<String> list = new ArrayList<>();

		list.add("clear");

		return list;
	}

}
