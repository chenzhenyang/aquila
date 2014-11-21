import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.myutil.MigrateConstraint;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 1.���ݴ������Ϣ������һ��xml����� 2.����һ��Connection��������Ϣ�Ƿ���Ч 3.�������д��c3p0�������ļ�
 * 
 * @author u
 *
 */
public class MigrateConstraintFKFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public MigrateConstraintFKFunction() {
		this.addOption(OptionBuilder.withDescription("migrate Unique Constraint.").withLongOpt("name").hasArg().create("name"));
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
		ShellEnvironment.println("Create Check Constraint:" + constrName);
		MigrateConstraint.createConstraintUK(constrName);
		ShellEnvironment.println("Create Check Constraint:" + constrName + " successfully!");
	}
}