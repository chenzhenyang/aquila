package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.Function;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

public class FunctionTransfer {
	
	private static Logger logger = Logger.getLogger(FunctionTransfer.class);
	
	public static boolean createFunction(String name) {
		List<Function> list = fetchFunctionsFromSqlServer();
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			for (Function pd : list) {
				if (!pd.name.toUpperCase().equals(name.toUpperCase())) {
					continue;
				}
				st.execute(pd.toSql());
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);
		}
		return true;
	}

	public static boolean createFunction() {
		List<Function> list = fetchFunctionsFromSqlServer();
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			for (Function pd : list) {
				st.execute(pd.toSql());
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);
		}
		return true;
	}

	public static List<Function> fetchFunctionsFromSqlServer() {
		List<Function> list = new LinkedList<>();
		String sql = "select name ,definition from sys.objects,sys.sql_modules where sys.objects.object_id =  sys.sql_modules.object_id and (type = 'AF' or type = 'FN' or type = 'FS' or type = 'FT' or type = 'IF')";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				list.add(new Function(rs.getString("name"), rs.getString("definition")));
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);
		}
		return list;
	}
}
