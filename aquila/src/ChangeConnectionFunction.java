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
 * @author u
 *
 */
public class ChangeConnectionFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public ChangeConnectionFunction() {
		this.addOption(OptionBuilder.withDescription("change sth").withLongOpt("name").hasArg().create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing");
			;
			return null;
		}
		try {
			changeConnection(line.getOptionValue("name"));
		} catch (IOException ex) {
			throw new AquilaException(ShellError.SHELL_0007, "create");
		}
		return null;
	}

	/**
	 * 1.接受输入的信息，想create时那样 2.验证信息可用 3.删除原来的 4.写入当前这个
	 * 
	 * @param sname
	 * @throws IOException
	 */
	private void changeConnection(String sname) throws IOException {
		ShellEnvironment.println("change connection:");
		ConsoleReader reader = new ConsoleReader();
		reader.printString("Name:");
		reader.flushConsole();
		String name = reader.readLine();
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
		// TODO 目前检测连接的方法不会抛出错误
		boolean bool = DBUtil.testConnection(driver, url, username, password);

		// c3p0的配置文件的位置，下一步要跟Resource那个类联系起来
		// 下一步会增加一些c3p0的相关配置
		if (bool) {
			try {
				XMLUtil.deleteConnection(name);
				XMLUtil.saveConnection(Constants.PATH, name, driver, url, username, password);
			} catch (ParserConfigurationException | SAXException | TransformerException e) {
				e.printStackTrace();
			}
			ShellEnvironment.println("change connection " + name + " successfully!");
		} else {
			ShellEnvironment.println("change connection " + name + " failed!");
		}
	}
}
