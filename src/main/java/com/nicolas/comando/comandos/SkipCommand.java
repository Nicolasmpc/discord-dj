package com.nicolas.comando.comandos;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;
import com.nicolas.lavaplayer.GuildMusicManager;
import com.nicolas.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand{

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
		final AudioPlayer audioPlayer = guildMusicManager.player;
		
		if (audioPlayer.getPlayingTrack() == null) {
			return;
		}
		
		guildMusicManager.scheduler.nextTrack();
		
	}

	@Override
	public String getName() {
		return "skip";
	}

}
