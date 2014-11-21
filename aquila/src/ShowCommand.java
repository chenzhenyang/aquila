import java.util.List;

import org.codehaus.groovy.tools.shell.Shell;

import com.highgo.hgdbadmin.myutil.ShellEnvironment;

public class ShowCommand extends AquilaCommand {
	private ShowConnectionsFunction showConnections;
	private ShowConnectionFunction showConnection;

	protected ShowCommand(Shell shell) {
		super(shell, "show", "sh", new String[] { "connections", "connection" }, "Show", "Info");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object executeCommand(List args) {
		if (args.size() == 0) {
			ShellEnvironment.println("Show Command Usage!");
			return null;
		}
		String func = (String) args.get(0);

		if (func.equals("connections")) {
			if (showConnections == null) {
				showConnections = new ShowConnectionsFunction();
			}
			return showConnections.execute(args);
		} else if (func.equals("connection")) {
			if (showConnection == null) {
				showConnection = new ShowConnectionFunction();
			}
			return showConnection.execute(args);
		} else {
			ShellEnvironment.println("The specified function " + func + " is not recognized.");
			return null;
		}
	}
}
