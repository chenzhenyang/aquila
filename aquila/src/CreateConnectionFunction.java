import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jline.ConsoleReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.xml.sax.SAXException;

import com.highgo.hgdbadmin.myutil.DBUtil;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class CreateConnectionFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public CreateConnectionFunction() {
		this.addOption(OptionBuilder.withDescription("create connection.").withLongOpt("name").hasArg()
				.create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing");
			return null;
		}
		try {
			createConnection(line.getOptionValue("name"));
		} catch (IOException ex) {
			throw new AquilaException(ShellError.SHELL_0007, "create");
		}
		return null;
	}

	private void createConnection(String sname) throws IOException {
		ShellEnvironment.println("create connection:"+ sname);
		ConsoleReader reader = new ConsoleReader();
//		reader.printString("Name:");
//		reader.flushConsole();
//		String name = reader.readLine();
		reader.printString("Connection Configuration:");
		reader.printNewline();
		reader.printString("JDBC Driver Class:");
		reader.flushConsole();
		String driver = reader.readLine();
		reader.printString("Connection String:");
		reader.flushConsole();
		String url = reader.readLine();
		reader.printString("username:");
		reader.flushConsole();
		String username = reader.readLine();
		reader.printString("password:");
		reader.flushConsole();
		String password = reader.readLine();

		// 这个地方要对输入的信息进行有效性的判断，并用AquilaException标准化
		// TODO
		boolean bool = DBUtil.testConnection(driver, url, username, password);
		// c3p0的配置文件的位置，下一步要跟Resource那个类联系起来
		// 下一步会增加一些c3p0的相关配置
		if (bool) {
			try {
				XMLUtil.saveConnection(Constants.PATH, sname, driver, url, username, password);
			} catch (ParserConfigurationException | SAXException | TransformerException e) {
				e.printStackTrace();
			}
			ShellEnvironment.println("create connection " + sname + " successfully!");
		} else {
			ShellEnvironment.println("create connection " + sname + " failed!");
		}
	}
}
