import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class MigrateSequenceFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateSequenceFunction() {
		this.addOption(OptionBuilder.withDescription("migrate sequence.").withLongOpt("name").hasArg().create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing.");
			return null;
		}
		try {
			String sequence = line.getOptionValue("name");
			migrateSequence(sequence);
		} catch (IOException | SQLException | InterruptedException ex) {
			throw new AquilaException(ShellError.SHELL_0007, "something wrong!");
		}
		return null;
	}

	private void migrateSequence(String sequence) throws IOException, SQLException, InterruptedException {
		ShellEnvironment.println("Create Sequence:" + sequence);
		String[] strs = sequence.split("\\.");// 用"."作为分隔符
		MigrateCenter.createSequence(strs[0], strs[1]);
		ShellEnvironment.println("Create Sequence:" + sequence + " successfully!");
	}
}
