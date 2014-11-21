import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jline.ConsoleReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.xml.sax.SAXException;

import com.highgo.hgdbadmin.myutil.DataTypeMap;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class DeleteDataTypeMapFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public DeleteDataTypeMapFunction() {
		this.addOption(OptionBuilder.withDescription("delete a data type map.").withLongOpt("s").hasArg().create("s"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("s")) {
			ShellEnvironment.println("Required argument --s is missing!");
			return null;
		}
		try {
			deleteDataTypeMap(line.getOptionValue("s"));
		} catch (IOException ex) {
			throw new AquilaException(ShellError.SHELL_0007, "delete");
		}
		return null;
	}

	private void deleteDataTypeMap(String s) throws IOException {
		ShellEnvironment.println("delete data type map:" + s);
		DataTypeMap.delete(s);
		ShellEnvironment.println("delete data type map:" + s + " successfully!");
	}
}
