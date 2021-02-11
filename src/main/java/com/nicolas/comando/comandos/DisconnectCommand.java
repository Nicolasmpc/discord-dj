package com.nicolas.comando.comandos;

import java.util.ArrayList;
import java.util.List;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

public class DisconnectCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
//		final TextChannel channel = ctx.getChannel();
		final Member self = ctx.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();
		
		if (!selfVoiceState.inVoiceChannel()) {
			return;
		}
		
		ctx.getGuild().kick(self);
		
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

}
