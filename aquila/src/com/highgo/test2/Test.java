package com.highgo.test2;

import java.sql.SQLException;
import java.util.List;

import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Test {

	public static void main(String[] args) throws SQLException {
		ComboPooledDataSource cpds = new ComboPooledDataSource("source");
		ComboPooledDataSource cpds2 = new ComboPooledDataSource("postgres");
//		List<Table> tables = MigrateCenter.fetchTableFromSqlServer(cpds);
//		MigrateCenter.carryTablesDefinition(cpds, cpds2, tables);
	}
}
