import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Schema;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class MigrateSchemaFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateSchemaFunction() {
		this.addOption(OptionBuilder.withDescription("migrate schema.").withLongOpt("name").hasArg()
				.create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing.");
			return null;
		}
		String schmea = line.getOptionValue("name");
		migrateSchema(schmea);
		return null;
	}

	private void migrateSchema(String schema){
		ShellEnvironment.println("Creating Schema:" + schema);
		MigrateCenter.createSchema(schema);
		ShellEnvironment.println("Creating Schema:" + schema + " successfully!");
	}
}
