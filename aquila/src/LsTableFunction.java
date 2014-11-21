import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;

import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

/**
 * 1.根据传入的信息，创建一个xml代码段 2.创建一个Connection，看看信息是否有效 3.将代码段写入c3p0的配置文件
 * 
 * @author u
 *
 */
public class LsTableFunction extends AquilaFunction {

	@SuppressWarnings("static-access")
	public LsTableFunction() {
		this.addOption(OptionBuilder.withDescription("list all table.").withLongOpt("").hasArg().create());
	}

	public Object executeFunction(CommandLine line) {
		try {
			lsTable();
		} catch (IOException | SQLException | InterruptedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void lsTable() throws IOException, SQLException, InterruptedException {
		ShellEnvironment.println("list all data type map:");
		List<Table> list = Constants.TABLES;
		for(Table table : list){
			ShellEnvironment.println(table);
		}
		ShellEnvironment.println("list all data type map successfully!");
	}
}
