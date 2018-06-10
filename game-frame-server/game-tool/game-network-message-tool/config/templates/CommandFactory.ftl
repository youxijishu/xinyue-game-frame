package ${packageName};

import com.game.codec.command.AbstractCommandFactory;

public class CommandFactory extends AbstractCommandFactory{

	private static CommandFactory instance = new CommandFactory();

	public static CommandFactory getInstance() {
		return instance;
	}
	@Override
	public void initCommand() {
		<#list commands as command>
		this.addGameCommand(${command}.class);
		</#list>
	}

}