package com.nicolas.comando.comandos;

import java.util.ArrayList;
import java.util.List;

import com.nicolas.comando.CommandContext;
import com.nicolas.comando.ICommand;

public class TesteCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		ctx.getChannel().sendMessage("teste").queue();
	}

	@Override
	public String getName() {
		return "teste";
	}
	
	@Override
	public List<String> getAliases() {
		
		List<String> list = new ArrayList<>();
		
		list.add("test");
		list.add("t");
		
		return list;
	}

}
