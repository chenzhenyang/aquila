import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.model.View;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class MigrateViewFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateViewFunction() {
		this.addOption(OptionBuilder.withDescription("migrate view.").withLongOpt("name").hasArg().create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing.");
			return null;
		}
		try {
			String view = line.getOptionValue("name");
			migrateView(view);
		} catch (IOException | SQLException | InterruptedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void migrateView(String view) throws IOException, SQLException, InterruptedException {
		ShellEnvironment.println("Create view:" + view);
		String[] strs = view.split("\\.");// 用"."作为分隔符
		MigrateCenter.createView(strs[0], strs[1]);
		ShellEnvironment.println("Create view:" + view + " successfully!");
	}
}
