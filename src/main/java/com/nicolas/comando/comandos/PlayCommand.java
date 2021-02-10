package com.nicolas.comando.comandos;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;
import com.nicolas.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (ctx.getArgs().isEmpty()) {
			channel.sendMessage("insira um link do YouTube").queue();
			return;
		}

		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			channel.sendMessage("Voce precisa estar em um canal de voz para esse comando funcionar").queue();
			return;
		}

		String link = String.join(" ", ctx.getArgs());

		if (!isUrl(link)) {
			link = "ytsearch:" + link;
		}

		PlayerManager.getInstance().loadAndPlay(channel, link);
		
		final AudioManager audioManager = ctx.getGuild().getAudioManager();
		final VoiceChannel memberChannel = memberVoiceState.getChannel();
		
		audioManager.openAudioConnection(memberChannel);

	}

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public List<String> getAliases() {
		List<String> list = new ArrayList<>();

		list.add("p");

		return list;
	}

	private boolean isUrl(String link) {
		try {
			new URI(link);
			return true;
		} catch (URISyntaxException e) {
			return false;
		}
	}
}
