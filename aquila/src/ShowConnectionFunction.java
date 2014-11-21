import java.io.IOException;
import java.util.List;

import jline.ConsoleReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class ShowConnectionFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public ShowConnectionFunction() {
		this.addOption(OptionBuilder.withDescription("show all connections.").withLongOpt("cn").hasArg().create("cn"));
	}

	public Object executeFunction(CommandLine line) {
		try {
			String cn = line.getOptionValue("cn");
			getAllConnection(cn);
		} catch (IOException ex) {
			throw new AquilaException(ShellError.SHELL_0007, "show");
		}
		return null;
	}

	private void getAllConnection(String cn) throws IOException {
		ConsoleReader reader = new ConsoleReader();
		AConnection ac = XMLUtil.getConnection(Constants.PATH, cn);
		reader.printString(ac.toString());
		reader.flushConsole();
	}

	public static void main(String[] args) {
		ShowConnectionFunction scf = new ShowConnectionFunction();
	}
}
