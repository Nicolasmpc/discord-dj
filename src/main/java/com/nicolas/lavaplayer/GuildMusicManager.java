package com.nicolas.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.api.entities.Guild;

public class GuildMusicManager {
	
	public final AudioPlayer player;
	public final TrackScheduler scheduler;
	public static Guild guild;
	private final AudioPlayerSendHandler sendHandler;
	
	public GuildMusicManager(AudioPlayerManager manager, Guild guild) {
		GuildMusicManager.guild = guild;
		this.player = manager.createPlayer();
		this.scheduler = new TrackScheduler(this.player);
		this.player.addListener(this.scheduler);
		this.sendHandler = new AudioPlayerSendHandler(this.player);
	}
	
	public AudioPlayerSendHandler getSendHandler() {
		return sendHandler;
	}
	
	public static Guild getGuild() {
		return guild;
	}

}
