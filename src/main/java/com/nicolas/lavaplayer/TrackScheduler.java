package com.nicolas.lavaplayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter{

	public final AudioPlayer player;
	public final BlockingQueue<AudioTrack> queue;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);


	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}
	
	public void queue(AudioTrack track) {
		if (!this.player.startTrack(track, true)) {
			this.queue.offer(track);
		}
	}
	
	public void nextTrack() {
		if (!this.player.startTrack(this.queue.poll(), false)) {
			LOGGER.info("Fila vazia | disconnect");
			final AudioManager audioManager = GuildMusicManager.getGuild().getAudioManager();
			audioManager.closeAudioConnection();
		}
	}
	
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}
}
