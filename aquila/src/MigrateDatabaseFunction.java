import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

/**
 * 
 * @author u
 */
public class MigrateDatabaseFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateDatabaseFunction() {
		this.addOption(OptionBuilder.withDescription("migrate database.").withLongOpt("datamode").hasArg()
				.create("datamode"));
		this.addOption(OptionBuilder.withDescription("migrate database.").withLongOpt("").hasArg().create());
	}

	public Object executeFunction(CommandLine line) {

		String datamode = null;
		if (!line.hasOption("datamode")) {
			datamode = "replace";
		} else {
			datamode = line.getOptionValue("datamode");
		}
		try {
			migrateDatabase(datamode);
		} catch (InterruptedException e) {
		}
		return null;
	}

	private void migrateDatabase(String datamode) throws InterruptedException {
		ShellEnvironment.println("Migrate all database£¡");
		if (datamode.equals("replace")) {
			for (Table table : Constants.TABLES) {
				MigrateCenter.deleteAllData(table.schema, table.name);
			}
		}
		MigrateCenter.carryDatabase(Constants.SCHEMAS, Constants.TABLES, Constants.VIEWS, Constants.INDEXES,
				Constants.PROCEDURES, Constants.TRIGGERS, Constants.FUNCTIONS, Constants.SEQUENCES, Constants.CKS,
				Constants.PKS, Constants.UKS, Constants.FKS);
		ShellEnvironment.println("Migrate all database successfully!");
	}
}
