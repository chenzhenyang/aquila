import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Index;
import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.model.View;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author u
 *
 */
public class MigrateIndexFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateIndexFunction() {
		this.addOption(OptionBuilder.withDescription("migrate index.").withLongOpt("name").hasArg().create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing.");
			return null;
		}
		try {
			String indeName = line.getOptionValue("name");
			migrateIndex(indeName);
		} catch (IOException | SQLException | InterruptedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void migrateIndex(String index) throws IOException, SQLException, InterruptedException {
		ShellEnvironment.println("Create Index:" + index);
		String[] strs = index.split("\\.");// 用"."作为分隔符
		MigrateCenter.createIndex(strs[0],strs[1],strs[2]);
		ShellEnvironment.println("Create Index " + index + " successfully!");
	}
}
