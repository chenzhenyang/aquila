package com.highgo.hgdbadmin.myutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.ConstraintCK;
import com.highgo.hgdbadmin.model.ConstraintFK;
import com.highgo.hgdbadmin.model.ConstraintPK;
import com.highgo.hgdbadmin.model.ConstraintUK;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MigrateConstraint {

	private static Logger logger = Logger.getLogger(MigrateConstraint.class);

	public static boolean createConstraintCK(String constrName) {
		logger.info("createConstraintCK");
		Connection conn = null;
		Statement st = null;
		try {
			ConstraintCK cc = fetchConstraintCKFormSqlServer(constrName);
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cc.toSql();
			logger.info(sql);
			st.execute(sql);
			logger.info("create check constraint " + constrName.toUpperCase() + " successfully");
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

	public static List<ConstraintCK> fetchConstraintCKFromSqlServer() {
		logger.info("fetchConstraintCKFromSqlServer");
		List<ConstraintCK> list = new LinkedList<>();
		String sql = "select b.TABLE_SCHEMA as TSCHEMA,b.TABLE_NAME as TNAME,b.CONSTRAINT_NAME as CNAME,a.CHECK_CLAUSE as CLAUSE from INFORMATION_SCHEMA.CHECK_CONSTRAINTS a ,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new ConstraintCK(rs.getString("TSCHEMA"), rs.getString("TNAME"), rs.getString("CNAME"), rs
						.getString("CLAUSE")));

			}
			logger.info("CHECK CONSTRAINTS:" + list);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return list;
	}

	public static ConstraintCK fetchConstraintCKFormSqlServer(String constrName) {
		logger.info("fetchConstraintCKFormSqlServer");
		String sql = "select b.TABLE_SCHEMA as TSCHEMA,b.TABLE_NAME as TNAME,b.CONSTRAINT_NAME as CNAME,a.CHECK_CLAUSE as CLAUSE from INFORMATION_SCHEMA.CHECK_CONSTRAINTS a ,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  AND b.CONSTRAINT_NAME = ?";
		logger.info(sql);
		ConstraintCK cc = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			cc = null;
			rs = ps.executeQuery();
			while (rs.next()) {
				cc = new ConstraintCK(rs.getString("TSCHEMA"), rs.getString("TNAME"), rs.getString("CNAME"),
						rs.getString("CLAUSE"));
			}
			logger.info("create check constraint:" + constrName.toUpperCase() + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cc;
	}

	public static boolean createConstraintUK(String constrName) {
		logger.info("createConstraintUK");
		Connection conn = null;
		Statement st = null;
		try {
			ConstraintUK cu = fetchConstraintUKFromSqlServer(constrName);
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cu.toSql();
			logger.info(sql);
			st.execute(sql);
			logger.info("create unique constraint " + constrName.toUpperCase() + " successfully");
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

	public static List<ConstraintUK> fetchConstraintUKFromSqlServer() {
		logger.info("fetchConstraintUKFromSqlServer");
		List<ConstraintUK> list = new LinkedList<>();
		List<String> constrNames = new LinkedList<>();
		// String sql =
		// "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.CONSTRAINT_NAME as CNAME,b.COLUMN_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS a,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE b where a.CONSTRAINT_TYPE = 'UNIQUE' and a.CONSTRAINT_NAME = b.CONSTRAINT_NAME";
		String sql = "select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS  where CONSTRAINT_TYPE = 'UNIQUE'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		logger.info(sql);
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				constrNames.add(rs.getString("CONSTRAINT_NAME"));
			}
			for (String constrName : constrNames) {
				list.add(fetchConstraintUKFromSqlServer(constrName));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		logger.info("UNIQUE KEYS:" + list);
		return list;
	}

	public static ConstraintUK fetchConstraintUKFromSqlServer(String constrName) {
		logger.info("fetchConstraintUKFromSqlServer");
		ConstraintUK cu = null;
		String sql = "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.CONSTRAINT_NAME as CNAME,b.COLUMN_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS a,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE b where a.CONSTRAINT_TYPE = 'UNIQUE' and a.CONSTRAINT_NAME = b.CONSTRAINT_NAME and a.CONSTRAINT_NAME=?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			rs = ps.executeQuery();
			List<String> columns = new LinkedList<>();
			while (rs.next()) {
				cu = new ConstraintUK(rs.getString("TSCHEMA"), rs.getString("TNAME"), rs.getString("CNAME"));
				columns.add(rs.getString("COLUMN_NAME"));
			}
			logger.info("create unique key " + constrName.toUpperCase() + " successfully");
			cu.columns = columns;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cu;
	}

	public static boolean createConstraintPK(String constrName) {
		logger.info("createConstraintPK");
		Connection conn = null;
		Statement st = null;
		try {
			ConstraintPK cp = fetchConstraintPKFromSqlServer(constrName);
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cp.toSql();
			logger.info(sql);
			st.execute(sql);
			logger.info("create primary key" + constrName.toUpperCase() + " successfully");
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

	public static boolean createConstraintPK(ConstraintPK constraint) {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = constraint.toSql();
			logger.info(sql);
			st.execute(sql);
			logger.info("create primary key" + constraint.cName.toUpperCase() + " successfully");
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

	public static List<ConstraintPK> fetchConstraintPKFromSqlServer() {
		logger.info("fetchConstraintPKFromSqlServer");
		List<ConstraintPK> list = new LinkedList<>();
		List<String> constrNames = new LinkedList<>();
		String sql = "select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS  where CONSTRAINT_TYPE = 'PRIMARY KEY'";
		logger.info(sql);

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				constrNames.add(rs.getString("CONSTRAINT_NAME"));
			}
			for (String constrName : constrNames) {
				list.add(fetchConstraintPKFromSqlServer(constrName));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);
		}
		logger.info("PRIMARY KEY:" + list);
		return list;
	}

	public static ConstraintPK fetchConstraintPKFromSqlServer(String constrName) {
		logger.info("fetchConstraintPKFromSqlServer");
		ConstraintPK cp = null;
		String sql = "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.COLUMN_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE a where a.CONSTRAINT_NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			rs = ps.executeQuery();
			String tschema = null;
			String ttable = null;
			List<String> columns = new LinkedList<>();
			while (rs.next()) {
				tschema = rs.getString("TSCHEMA");
				ttable = rs.getString("TNAME");
				columns.add(rs.getString("COLUMN_NAME"));
			}
			cp = new ConstraintPK(tschema, ttable, constrName, columns);
			logger.info("PRIMARY KEY:" + cp);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cp;
	}

	public static boolean createConstraintFK(String constrName) {
		logger.info("createConstraintFK");
		Connection conn = null;
		Statement st = null;
		try {
			ConstraintFK cf = fetchConstraintFKFromSqlServer(constrName);
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cf.toSql();
			logger.info(sql);
			st.execute(sql);
			logger.info("create foreign key " + constrName.toUpperCase() + " successfully");
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

	public static List<ConstraintFK> fetchConstraintFKFromSqlServer() {
		logger.info("fetchConstraintFKFromSqlServer");
		List<ConstraintFK> list = new LinkedList<>();
		List<String> constrNames = new LinkedList<>();
		String sql = "select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE = 'FOREIGN KEY'";
		logger.info(sql);
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			while (rs.next()) {
				constrNames.add(rs.getString("CONSTRAINT_NAME"));
			}
			for (String constrName : constrNames) {
				list.add(fetchConstraintFKFromSqlServer(constrName));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		logger.info("FOREIGN KEY:" + list);
		return list;
	}

	public static ConstraintFK fetchConstraintFKFromSqlServer(String constrName) {
		logger.info("fetchConstraintFKFromSqlServer");
		ConstraintFK cf = null;
		String sql = "select b.TABLE_SCHEMA,b.TABLE_NAME,b.CONSTRAINT_NAME,c.COLUMN_NAME,a.UNIQUE_CONSTRAINT_NAME,a.MATCH_OPTION,a.UPDATE_RULE,a.DELETE_RULE from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS a,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE c where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  and a.CONSTRAINT_NAME = c.CONSTRAINT_NAME and a.CONSTRAINT_NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			rs = ps.executeQuery();
			String tschema = null;
			String ttable = null;
			String cname = null;
			String uniqueConstraintName = null;
			String matchOption = null;
			String updateRule = null;
			String deleteRule = null;
			List<String> columns = new LinkedList<>();
			ConstraintPK cp = null;
			while (rs.next()) {
				tschema = rs.getString("TABLE_SCHEMA");
				ttable = rs.getString("TABLE_NAME");
				cname = rs.getString("CONSTRAINT_NAME");
				uniqueConstraintName = rs.getString("UNIQUE_CONSTRAINT_NAME");
				cp = fetchConstraintPKFromSqlServer(uniqueConstraintName);
				matchOption = rs.getString("MATCH_OPTION");
				updateRule = rs.getString("UPDATE_RULE");
				deleteRule = rs.getString("DELETE_RULE");
				columns.add(rs.getString("COLUMN_NAME"));
			}
			cf = new ConstraintFK(tschema, ttable, cname, columns, matchOption, updateRule, deleteRule, cp);
			logger.info("FOREIGN KEY:" + cf);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cf;
	}

	public static void main(String[] args) throws SQLException {
		ComboPooledDataSource cpds = new ComboPooledDataSource("source");
		Connection source = cpds.getConnection();
		ComboPooledDataSource cpds2 = new ComboPooledDataSource("postgres");
		Connection dest = cpds2.getConnection();

		// List<ConstraintCK> list = fetchConstraintCKFormSqlServer(source);
		// for (ConstraintCK cc : list) {
		// System.out.println(cc.toSql());
		// }
		//
		// ConstraintCK cc =
		// fetchConstraintCKFormSqlServer(source,"chkRowCount");
		// System.out.println(cc.toSql());

		// MigrateCenter.createTable(source, dest, "dbo", "testaddck1");
		// createConstraintCK(source,dest,"addchtest1");

		// List<ConstraintUK> cus = fetchConstraintUKFormSqlServer(source);
		// for(ConstraintUK cu : cus ){
		// System.out.println(cu);
		// }

		// ConstraintUK cu = fetchConstraintUKFromSqlServer(source, "ktestuc1");
		// System.out.println(cu);

		// MigrateCenter.createTable(source, dest, "dbo", "TestUC1");
		// createConstraintUK(source, dest, "ktestuc1");

		// List<ConstraintPK> list = fetchPrimaryKeyFromSqlServer(source);
		// for(ConstraintPK cp : list){
		// System.out.println(cp.toSql());
		// }

		// PK__TestFK1__66F3C56052B83761
		// PK__TestFK3__609CF936269C7B8C
		// ConstraintPK cp =
		// fetchPrimaryKeyFromSqlServer(source,"PK__TestFK3__609CF936269C7B8C");
		// System.out.println(cp.toSql());

		// MigrateCenter.createTable(source, dest, "dbo", "testfk1");
		// createConstraintPK(source, dest, "PK__TestFK1__66F3C56052B83761");

		// ConstraintFK cf = fetchConstraintFKFromSqlServer(source, "fctest1");
		// System.out.println(cf.toSql());
		// //就剩这个还没测试了，应该差不多
		// createConstraintFK(source, dest, "fctest1");

	}
}
