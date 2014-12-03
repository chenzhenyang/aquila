package com.highgo.hgdbadmin.transfer;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.shell.Groovysh;

import com.highgo.hgdbadmin.model.ConstraintCK;
import com.highgo.hgdbadmin.model.ConstraintFK;
import com.highgo.hgdbadmin.model.ConstraintPK;
import com.highgo.hgdbadmin.model.ConstraintUK;
import com.highgo.hgdbadmin.model.Function;
import com.highgo.hgdbadmin.model.Index;
import com.highgo.hgdbadmin.model.Procedure;
import com.highgo.hgdbadmin.model.Schema;
import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.model.Trigger;
import com.highgo.hgdbadmin.model.View;
import com.highgo.hgdbadmin.myutil.InitialSourceList;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.ReportInstance;

public class DatabaseTransfer {

	private static Logger logger = Logger.getLogger(DatabaseTransfer.class);

	/**
	 * 数据库的迁移顺序比较固定，按照schema、表结构、视图、数据、索引、Procedure、Trigger、Function、Sequence、
	 * Constraint的顺序迁移，问题不大 约束，约束有四种，只要外键最后创建，就没有问题，外键可能会依赖与主键或者唯一约束
	 * 
	 * @param cpds
	 * @param cpds2
	 * @param schemas
	 * @param tables
	 * @param sequences
	 * @param views
	 * @param indexes
	 * @param procs
	 * @param funcs
	 * @param triggers
	 * @param cks
	 * @param pks
	 * @param uks
	 * @param fks
	 * @return
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public static boolean carryDatabase(List<Schema> schemas, List<Table> tables, List<View> views,
			List<Index> indexes, List<Procedure> procs, List<Trigger> triggers, List<Function> funcs,
			List<Sequence> sequences, List<ConstraintCK> cks, List<ConstraintPK> pks, List<ConstraintUK> uks,
			List<ConstraintFK> fks) throws InterruptedException {

		ReportInstance.report.timeTable.start = new Date();

		// migrate schema
		for (Schema schema : schemas) {
			SchemaTransfer.createSchema(schema);
		}
		// migrate table schema
		for (Table table : tables) {
			TableTransfer.createTable(table);
		}

		// migrate view
		for (View view : views) {
			ViewTransfer.createView(view);
		}

		// migrate data
		// for (Table table : tables) {
		// int recInMemory = MemoryTool.getRecordNumInMemory(table.schema,
		// table.name);
		// int threadnum = MemoryTool.getCoreNum();
		// int batchSize = MemoryTool.getBatch(recInMemory) / 2;
		// logger.info(table.schema.toUpperCase() + "." +
		// table.name.toUpperCase() + " in thread num:"
		// + " batch size:" + batchSize);
		// MainThread.migrateTable(table.schema, table.name, threadnum,
		// recInMemory, batchSize);
		// }
		// migrate index
		for (Index index : indexes) {
			IndexTransfer.createIndex(index);
		}

		// pass Procedure/Trigger/Function

		// migrate sequence
		for (Sequence sequence : sequences) {
			SequenceTransfer.createSequence(sequence.schema, sequence.name);
		}

		// migrate Check Constraint
		for (ConstraintCK ck : cks) {
			ConstraintCKTransfer.createConstraintCK(ck);
		}
		// // migrate Primary Key
		for (ConstraintPK pk : pks) {
			ConstraintPKTransfer.createConstraintPK(pk.cName);
		}
		// // migrate Unique key
		for (ConstraintUK uk : uks) {
			ConstraintUKTransfer.createConstraintUK(uk.cName);
		}
		// // migrate Foreign Key
		for (ConstraintFK fk : fks) {
			ConstraintFKTransfer.createConstraintFK(fk.cName);
		}

		ReportInstance.report.timeTable.end = new Date();
		return true;
	}

	public static void main(String[] args) throws InterruptedException {

		System.setProperty("groovysh.prompt", "aquila");
		Groovysh shell = new Groovysh();
		ShellEnvironment.setIo(shell.getIo());

		InitialSourceList.initialSourceObjectList();

		List<Schema> schemas = SchemaTransfer.fetchSchemasFromSqlServer();
		List<Table> tables = TableTransfer.fetchTableFromSqlServer();
		List<View> views = ViewTransfer.fetchViewsFromSqlServer();
		List<Index> indexes = IndexTransfer.fetchIndexesFromSqlServer();
		List<Sequence> sequences = SequenceTransfer.fetchSequencesFromSqlServer();
		List<ConstraintCK> cks = ConstraintCKTransfer.fetchConstraintCKFromSqlServer();
		List<ConstraintPK> pks = ConstraintPKTransfer.fetchConstraintPKFromSqlServer();
		List<ConstraintUK> uks = ConstraintUKTransfer.fetchConstraintUKFromSqlServer();
		List<ConstraintFK> fks = ConstraintFKTransfer.fetchConstraintFKFromSqlServer();

		DatabaseTransfer
				.carryDatabase(schemas, tables, views, indexes, null, null, null, sequences, cks, pks, uks, fks);
		
		System.out.println(ReportInstance.report.toHTML());

		// List<View> views = ViewTransfer.fetchViewsFromSqlServer();
		// for (View view : views) {
		// if (view.name.equals("View_TemplateRelationLabelInfo")) {
		// System.out.println(view.toSql ());
		// ViewTransfer.createView(view);
		// }
		// }

		// View view = PostgreSQLObjectInfo.fetchViewFromSqlServer("dbo",
		// "View_TemplateRelationLabelInfo");
		// System.out.println(view);

	}

}
