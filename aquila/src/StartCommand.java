import java.util.List;

import org.codehaus.groovy.tools.shell.Shell;

import com.highgo.hgdbadmin.myutil.ShellEnvironment;

/**
 *
 */
public class StartCommand extends AquilaCommand {

	private MigrateDatabaseFunction migrateDatabaseFunction;
	private MigrateSchemaFunction migrateSchemaFunction;
	private MigrateTableFunction migrateTableFunction;
	private MigrateViewFunction migrateViewFunction;
	private MigrateSequenceFunction migrateSequenceFunction;
	private MigrateIndexFunction migrateIndexFunction;
	private MigrateProcedureFunction migrateProcedureFunction;
	private MigrateFunctionFunction migrateFunctionFunction;
	private MigrateTriggerFunction migrateTriggerFunction;
	private MigrateConstraintCKFunction migrateConstraintCKFunction;
	private MigrateConstraintFKFunction migrateConstraintFKFunction;
	private MigrateConstraintPKFunction migrateConstraintPKFunction;
	private MigrateConstraintUKFunction migrateConstraintUKFunction;

	public StartCommand(Shell shell) {
		super(shell, "start", "\\s", new String[] { "database","schema", "table", "sequence", "view", "index", "procedure",
				"function", "trigger", "ck", "fk", "pk", "uk" }, "Start", "Info");
	}

	public Object executeCommand(List args) {
		if (!ShellEnvironment.isInteractive()) {
			throw new AquilaException(ShellError.SHELL_0007, "create");
		}
		if (args.size() == 0) {
			ShellEnvironment.println("Start Command Usage!");
			return null;
		}
		String func = (String) args.get(0);
		
		if (func.equals("database")) {
			if (migrateDatabaseFunction == null) {
				migrateDatabaseFunction = new MigrateDatabaseFunction();
			}
			return migrateDatabaseFunction.execute(args);
		}
		if (func.equals("schema")) {
			if (migrateSchemaFunction == null) {
				migrateSchemaFunction = new MigrateSchemaFunction();
			}
			return migrateSchemaFunction.execute(args);
		}
		if (func.equals("table")) {
			if (migrateTableFunction == null) {
				migrateTableFunction = new MigrateTableFunction();
			}
			return migrateTableFunction.execute(args);
		}
		if (func.equals("view")) {
			if (migrateViewFunction == null) {
				migrateViewFunction = new MigrateViewFunction();
			}
			ShellEnvironment.println(args);
			return migrateViewFunction.execute(args);
		}
		if (func.equals("sequence")) {
			if (migrateSequenceFunction == null) {
				migrateSequenceFunction = new MigrateSequenceFunction();
			}
			ShellEnvironment.println(args);
			return migrateSequenceFunction.execute(args);
		}
		if (func.equals("index")) {
			if (migrateIndexFunction == null) {
				migrateIndexFunction = new MigrateIndexFunction();
			}
			ShellEnvironment.println(args);
			return migrateIndexFunction.execute(args);
		}
		if (func.equals("procedure")) {
			if (migrateProcedureFunction == null) {
				migrateProcedureFunction = new MigrateProcedureFunction();
			}
			ShellEnvironment.println(args);
			return migrateProcedureFunction.execute(args);
		}
		if (func.equals("function")) {
			if (migrateFunctionFunction == null) {
				migrateFunctionFunction = new MigrateFunctionFunction();
			}
			ShellEnvironment.println(args);
			return migrateFunctionFunction.execute(args);
		}
		if (func.equals("trigger")) {
			if (migrateTriggerFunction == null) {
				migrateTriggerFunction = new MigrateTriggerFunction();
			}
			ShellEnvironment.println(args);
			return migrateTriggerFunction.execute(args);
		}
		if (func.equals("ck")) {
			if (migrateConstraintCKFunction == null) {
				migrateConstraintCKFunction = new MigrateConstraintCKFunction();
			}
			ShellEnvironment.println(args);
			return migrateConstraintCKFunction.execute(args);
		}
		if (func.equals("fk")) {
			if (migrateConstraintFKFunction == null) {
				migrateConstraintFKFunction = new MigrateConstraintFKFunction();
			}
			ShellEnvironment.println(args);
			return migrateConstraintFKFunction.execute(args);
		}
		if (func.equals("pk")) {
			if (migrateConstraintPKFunction == null) {
				migrateConstraintPKFunction = new MigrateConstraintPKFunction();
			}
			ShellEnvironment.println(args);
			return migrateConstraintPKFunction.execute(args);
		}
		if (func.equals("uk")) {
			if (migrateConstraintUKFunction == null) {
				migrateConstraintUKFunction = new MigrateConstraintUKFunction();
			}
			ShellEnvironment.println(args);
			return migrateConstraintUKFunction.execute(args);
		} else {
			ShellEnvironment.println("The specified function " + func + " is not recognized.");
			return null;
		}
	}
}
