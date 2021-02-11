package com.nicolas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;
import com.nicolas.comando.comandos.DisconnectCommand;
import com.nicolas.comando.comandos.JoinCommand;
import com.nicolas.comando.comandos.PlayCommand;
import com.nicolas.comando.comandos.SkipCommand;
import com.nicolas.comando.comandos.StopCommand;
import com.nicolas.comando.comandos.TesteCommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager {
	private final List<ICommand> comandos = new ArrayList<>();
	
	public CommandManager() {
		addComando(new TesteCommand());
		addComando(new JoinCommand());
		addComando(new PlayCommand());
		addComando(new StopCommand());
		addComando(new SkipCommand());
		addComando(new DisconnectCommand());
	}

	private void addComando(ICommand cmd) {
		boolean nameFound = this.comandos.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
		
		if (nameFound) {
			throw new IllegalArgumentException("Comando com esse nome ja esta presente");
		}
		
		comandos.add(cmd);
	}
	
	public List<ICommand> getComandos(){
		return comandos;
	}
	
	@Nullable
	public ICommand getCommand(String search) {
		String searchLower = search.toLowerCase();
		
		for (ICommand cmd : this.comandos) {
			if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}
		
		return null;
	}
	
	void handle(GuildMessageReceivedEvent event) {
		String[] split = event.getMessage().getContentRaw()
				.replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
				.split("\\s+");
		
		String invoke = split[0].toLowerCase();
		ICommand cmd = this.getCommand(invoke);
		
		if (cmd != null) {
			List<String> args = Arrays.asList(split).subList(1, split.length);
			
			CommandContext ctx = new CommandContext(event, args);
			
			cmd.handle(ctx);
 		}
	}
	
	 
}
