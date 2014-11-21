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
public class CreateDataTypeMapFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public CreateDataTypeMapFunction() {
		this.addOption(OptionBuilder.withDescription("create a data type map.").withLongOpt("s").hasArg().create("s"));
		this.addOption(OptionBuilder.withDescription("create a data type map.").withLongOpt("d").hasArg().create("d"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("s") || !line.hasOption("d")) {
			ShellEnvironment.println("Required argument --s|--d is missing!");
			return null;
		}
		try {
			createDataTypeMap(line.getOptionValue("s"), line.getOptionValue("d"));
		} catch (IOException ex) {
			throw new AquilaException(ShellError.SHELL_0007, "create");
		}
		return null;
	}

	private void createDataTypeMap(String s, String d) throws IOException {
		ShellEnvironment.println("create data type map:" + s + " <==> " + d);
		DataTypeMap.add(s, d);
		ShellEnvironment.println("create data type map:" + s + " <==> " + d + " successfully!");
	}
}
