import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.model.View;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.MigrateConstraint;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class MigrateConstraintPKFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateConstraintPKFunction() {
		this.addOption(OptionBuilder.withDescription("migrate Parimary Key Constraint.").withLongOpt("name").hasArg().create("name"));
	}

	public Object executeFunction(CommandLine line) {
		if (!line.hasOption("name")) {
			ShellEnvironment.println("Required argument --name is missing.");
			return null;
		}
		try {
			String name = line.getOptionValue("name");
			migrateCheckConstraint(name);
		} catch (IOException | SQLException | InterruptedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void migrateCheckConstraint(String constrName) throws IOException, SQLException, InterruptedException {
		ShellEnvironment.println("Create  Parimary Key Constraint:" + constrName);
		MigrateConstraint.createConstraintPK(constrName);
		ShellEnvironment.println("Create  Parimary Key Constraint:" + constrName + " successfully!");
	}
}
