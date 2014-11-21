

import java.util.List;

import org.codehaus.groovy.tools.shell.CommandSupport;
import org.codehaus.groovy.tools.shell.Shell;

public class HelpCommand extends CommandSupport{
	
	protected HelpCommand(Shell shell) {
		super(shell, "help", "\\h");
	}
	
	@Override
	public String getDescription() {
		return "help description";
	}

	@Override
	public String getUsage() {
		return "help usage";
	}

	@Override
	public String getHelp() {
		return "get help";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object execute(List arg0) {
		shell.getIo().out.println("help help!");
		return null;
	}
}
