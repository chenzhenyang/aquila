import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.highgo.hgdbadmin.myutil.ShellEnvironment;


@SuppressWarnings("serial")
abstract public class AquilaFunction extends Options {

	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printOptions(ShellEnvironment.getIo().out, formatter.getWidth(), this, 0, 4);
	}

	public abstract Object executeFunction(CommandLine line);

	public Object execute(List<String> args) {
		CommandLine line = parseOptions(this, 1, args);
		return executeFunction(line);
	}

	protected CommandLine parseOptions(Options options, int start, List<String> arglist) {
		Iterator<String> iterator = arglist.iterator();
		
		
		//i add it to handle the ls command which arg's size =0
		if(arglist.size()==0){
			return null;
		}
		
		int i = 0;
		for (; i < start; i++) {
			iterator.next();
		}
		
		String[] args = new String[arglist.size() - start];
		for (; i < arglist.size(); i++) {
			args[i - start] = iterator.next();
		}
		CommandLineParser parser = new GnuParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			throw new AquilaException(ShellError.SHELL_0003, e.getMessage(), e);
		}
		return line;
	}

	protected long getLong(CommandLine line, String parameterName) {
		return Long.parseLong(line.getOptionValue(parameterName));
	}
}
