import java.sql.SQLException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.myutil.Constants;
import com.highgo.hgdbadmin.myutil.MemoryTool;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.vthread.MainThread;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class MigrateTableFunction extends AquilaFunction {
	@SuppressWarnings("static-access")
	public MigrateTableFunction() {
		this.addOption(OptionBuilder.withDescription("migrate table.").withLongOpt("name").hasArg().create("name"));
		this.addOption(OptionBuilder.withDescription("migrate table.").withLongOpt("mode").hasArg().create("mode"));
		this.addOption(OptionBuilder.withDescription("migrate table.").withLongOpt("tn").hasArg().create("tn"));
		this.addOption(OptionBuilder.withDescription("migrate table.").withLongOpt("batch").hasArg().create("batch"));
		this.addOption(OptionBuilder.withDescription("migrate table.").withLongOpt("datamode").hasArg().create("datamode"));
		this.addOption(OptionBuilder.withDescription("migrate table.").create("all"));
	}

	public Object executeFunction(CommandLine line) {
		boolean all = false;
		if (line.hasOption("all")) {
			all = true;
		} else {
			all = false;
			if (!line.hasOption("name")) {
				ShellEnvironment.println("Required argument --name is missing.");
				return null;
			}
		}

		String mode = null;
		if (!line.hasOption("mode")) {
			mode = "full";
		} else {
			mode = line.getOptionValue("mode");
		}
		
		String datamode = null;
		if (!line.hasOption("datamode")) {
			datamode = "replace";
		} else {
			datamode = line.getOptionValue("datamode");
		}

		String sth = null;
		if (!line.hasOption("th")) {
			sth = "4";
		} else {
			sth = line.getOptionValue("th");
		}

		Integer tn = null;
		try {
			tn = Integer.parseInt(sth);
		} catch (NumberFormatException e) {
			throw new AquilaException(ShellError.SHELL_0007, "formating wrong!");
		}

		String sbatch = null;
		if (!line.hasOption("batch")) {
			sbatch = "5";
		} else {
			sbatch = line.getOptionValue("batch");
		}

		Integer batch = null;
		try {
			batch = Integer.parseInt(sbatch);
		} catch (NumberFormatException e) {
			throw new AquilaException(ShellError.SHELL_0007, "formating wrong!");
		}

		if (all) {
			migrateTable(mode,datamode, tn, batch);
		} else {
			String table = line.getOptionValue("name");
			migrateTable(table, mode, tn, batch);
		}
		return null;
	}

	private void migrateTable(String schemaTable, String mode, String datamode ,int tn, int batch) throws InterruptedException{
		ShellEnvironment.println("Create table:" + schemaTable);
		String[] strs = schemaTable.split("\\.");// 用"."作为分隔符
		MigrateCenter.createTable(strs[0], strs[1]);
		// 在这个地方运行优化的参数的算法
		// 主要是两个变量读线程的个数，缓冲区的大小
		// 第三个参数，第四个参数
		// TODO
		if ("full".equals(mode)) {
			int recInMemory = MemoryTool.getRecordNumInMemory(strs[0], strs[1]);
			MainThread.migrateTable(strs[0], strs[1], MemoryTool.getCoreNum(), recInMemory,MemoryTool.getBatch(recInMemory));
		}
		ShellEnvironment.println("Create table:" + schemaTable + " successfully!");
	}

	private void migrateTable(String mode, String datamode ,int tn, int batch) {
		ShellEnvironment.println("Carry All Tables");
		List<Table> tables = Constants.TABLES;
		for(Table table : tables){
			MigrateCenter.createTable(table);
		}
		
		if ("full".equals(mode)) {
			for (Table table : tables) {
				if(datamode.equals("replace")){
					MigrateCenter.deleteAllData(table.schema,table.name);
				}
				try {
					int recInMemory = MemoryTool.getRecordNumInMemory(table.schema, table.name);
					MainThread.migrateTable(table.schema, table.name, MemoryTool.getCoreNum(), recInMemory,MemoryTool.getBatch(recInMemory));
				} catch (InterruptedException e) {
					System.out.println("迁移表的时候出错了！");
				}
			}
		}
		ShellEnvironment.println("Carry All Tables Successfully!");
	}
}
