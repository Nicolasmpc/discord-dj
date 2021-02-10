package com.nicolas.lavaplayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
	private static PlayerManager INSTANCE;

	private final Map<Long, GuildMusicManager> musicManagers;
	private final AudioPlayerManager audioPlayerManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

	public PlayerManager() {
		this.musicManagers = new HashMap<>();
		this.audioPlayerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
	}

	public GuildMusicManager getMusicManager(Guild guild) {
		return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

			return guildMusicManager;
		});
	}

	public void loadAndPlay(TextChannel channel, String url) {
		final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
		
		this.audioPlayerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack track) {
				musicManager.scheduler.queue(track);

				LOGGER.info("Adicionando a fila | url: " + track.getInfo().uri + "| titulo: " + track.getInfo().title);

				channel.sendMessage("Adicionando à fila: `").append(track.getInfo().title + "`").queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				
				if (!playlist.isSearchResult()) {
					final List<AudioTrack> tracks = playlist.getTracks();

					LOGGER.info("Adicionando a playlist | qtd: " + tracks.size() + "| titulo: " + playlist.getName());

					channel.sendMessage("Adicionando à fila: `").append(String.valueOf(tracks.size()) + "`")
							.append("músicas da playlist `" + playlist.getName() + "`").queue();

					for (final AudioTrack track : tracks) {
						musicManager.scheduler.queue(track);
					}
				} else {
					final AudioTrack track = playlist.getTracks().get(0);
					musicManager.scheduler.queue(track);
					
					LOGGER.info("Adicionando a fila | url: " + track.getInfo().uri + "| titulo: " + track.getInfo().title);
					
					channel.sendMessage("Adicionando à fila: `").append(track.getInfo().title + "`").queue();
				}

			}

			@Override
			public void noMatches() {
				// TODO Auto-generated method stub

			}

			@Override
			public void loadFailed(FriendlyException exception) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static PlayerManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}
		return INSTANCE;
	}

}
