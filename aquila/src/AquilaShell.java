import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import org.codehaus.groovy.runtime.MethodClosure;
import org.codehaus.groovy.tools.shell.Command;
import org.codehaus.groovy.tools.shell.CommandRegistry;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO.Verbosity;

import com.highgo.hgdbadmin.log.DerbyUtil;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

public final class AquilaShell {

	public final static HashSet<String> commandsToKeep;

	static {
		commandsToKeep = new HashSet<String>();
		commandsToKeep.add("exit");
		commandsToKeep.add("history");
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("groovysh.prompt", "aquila");
		Groovysh shell = new Groovysh();
		shell.setErrorHook(new MethodClosure(ThrowableDisplayer.class, "errorHook"));
		CommandRegistry registry = shell.getRegistry();
		@SuppressWarnings("unchecked")
		Iterator<Command> iterator = registry.iterator();
		while (iterator.hasNext()) {
			Command command = iterator.next();
			if (!commandsToKeep.contains(command.getName())) {
				iterator.remove();
				registry.remove(command);
			}
		}

		shell.register(new HelpCommand(shell));
		shell.register(new CreateCommand(shell));
		shell.register(new ShowCommand(shell));
		shell.register(new LsCommand(shell));
		shell.register(new StartCommand(shell));
		shell.register(new DeleteCommand(shell));

		
		try{
			DerbyUtil.createTable2();	
		}catch(SQLException e){
			
		}
		
		// Configure shared shell io object
		ShellEnvironment.setIo(shell.getIo());

		// We're running in batch mode by default
		ShellEnvironment.setInteractive(false);

		if (args.length == 0) {
			ShellEnvironment.getIo().setVerbosity(Verbosity.QUIET);
			ShellEnvironment.println("@|green Aquila Shell:|@ Type '@|bold help|@' or '@|bold \\h|@' for help.");
			ShellEnvironment.println();
			ShellEnvironment.setInteractive(true);
			shell.run(args);
		} else {
			File script = new File(args[0]);
			if (!script.isAbsolute()) {
				String userDir = System.getProperty(Constants.PROP_CURDIR);
				script = new File(userDir, args[0]);
			}
			interpretFileContent(script, shell);
		}
		
		
		
	}

	/**
	 * 执行脚本文件
	 * @param script
	 * @param shell
	 * @throws IOException
	 */
	private static void interpretFileContent(File script, Groovysh shell) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(script));
		String line;
		// Iterate over all lines and executed them one by one
		while ((line = in.readLine()) != null) {

			// Skip comments and empty lines as we don't need to interpret those
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			// Render shell and command to get user perception that it was run
			// as usual
			ShellEnvironment.print(shell.renderPrompt());
			ShellEnvironment.println(line);
			// Manually trigger command line parsing
			Object result = shell.execute(line);
			if (result != null) {
				ShellEnvironment.println(result);
			}
		}
	}

	private AquilaShell() {
		// Instantiation of this class is prohibited
	}
}
