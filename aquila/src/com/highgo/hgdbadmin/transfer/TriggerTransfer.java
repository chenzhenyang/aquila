package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.Trigger;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;

public class TriggerTransfer {
	
	private static Logger logger = Logger.getLogger(TriggerTransfer.class);
	
	public static boolean createTrigger(String name) {
		List<Trigger> list = fetchTriggerFromSqlServer();
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			for (Trigger pd : list) {
				if (!pd.name.equals("name")) {
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

	public static boolean createTrigger() {
		List<Trigger> list = fetchTriggerFromSqlServer();
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			for (Trigger pd : list) {
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

	public static List<Trigger> fetchTriggerFromSqlServer() {
		List<Trigger> list = new LinkedList<>();
		String sql = "select name ,definition from sys.sql_modules,sys.triggers where sys.sql_modules.object_id = sys.triggers.object_id";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				list.add(new Trigger(rs.getString("name"), rs.getString("definition")));
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
