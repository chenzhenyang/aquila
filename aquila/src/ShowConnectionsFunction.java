import java.io.IOException;
import java.util.List;

import jline.ConsoleReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.myutil.Constants;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class ShowConnectionsFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public ShowConnectionsFunction() {
		this.addOption(OptionBuilder.withDescription("show all connections.").withLongOpt("").hasArg().create());
	}

	public Object executeFunction(CommandLine line) {
		try {
			getAllConnection();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void getAllConnection() throws IOException {
		ConsoleReader reader = new ConsoleReader();
		reader.printString("all connections:");
		reader.printNewline();
		List<AConnection> list = XMLUtil.getConnections(Constants.PATH);
		for (int i = 0; i < list.size(); i++) {
			reader.printString("Connection" + i + ":");
			reader.printNewline();
			reader.printString(list.get(i).toString());
		}
		reader.flushConsole();
	}

	public static void main(String[] args) {
		ShowConnectionsFunction scf = new ShowConnectionsFunction();
	}
}
