package com.highgo.hgdbadmin.myutil;
import java.util.List;

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
import com.highgo.hgdbadmin.transfer.ConstraintCKTransfer;
import com.highgo.hgdbadmin.transfer.ConstraintFKTransfer;
import com.highgo.hgdbadmin.transfer.ConstraintPKTransfer;
import com.highgo.hgdbadmin.transfer.ConstraintUKTransfer;
import com.highgo.hgdbadmin.transfer.FunctionTransfer;
import com.highgo.hgdbadmin.transfer.IndexTransfer;
import com.highgo.hgdbadmin.transfer.ProcedureTransfer;
import com.highgo.hgdbadmin.transfer.SchemaTransfer;
import com.highgo.hgdbadmin.transfer.SequenceTransfer;
import com.highgo.hgdbadmin.transfer.TableTransfer;
import com.highgo.hgdbadmin.transfer.TriggerTransfer;
import com.highgo.hgdbadmin.transfer.ViewTransfer;

public class InitialSourceList {
	public static void initialSourceObjectList() {
		List<Schema> schemas = SchemaTransfer.fetchSchemasFromSqlServer();
		Constants.SCHEMAS.clear();
		Constants.SCHEMAS.addAll(schemas);
		// list table
		List<Table> tables = TableTransfer.fetchTableFromSqlServer();
		Constants.TABLES.clear();
		Constants.TABLES.addAll(tables);
		// list sequences
		List<Sequence> sequences = SequenceTransfer.fetchSequencesFromSqlServer();
		Constants.SEQUENCES.clear();
		Constants.SEQUENCES.addAll(sequences);

		// list views
		List<View> views = ViewTransfer.fetchViewsFromSqlServer();
		Constants.VIEWS.clear();
		Constants.VIEWS.addAll(views);
		// list index
		List<Index> indexes = IndexTransfer.fetchIndexesFromSqlServer();
		Constants.INDEXES.clear();
		Constants.INDEXES.addAll(indexes);

		// list procedure
		List<Procedure> procedures = ProcedureTransfer.fetchProceduresFromSqlServer();
		Constants.PROCEDURES.clear();
		Constants.PROCEDURES.addAll(procedures);

		// list function
		List<Function> functions = FunctionTransfer.fetchFunctionsFromSqlServer();
		Constants.FUNCTIONS.clear();
		Constants.FUNCTIONS.addAll(functions);

		// list Trigger
		List<Trigger> triggers = TriggerTransfer.fetchTriggerFromSqlServer();
		Constants.TRIGGERS.clear();
		Constants.TRIGGERS.addAll(triggers);

		// list check constraint
		List<ConstraintCK> cks = ConstraintCKTransfer.fetchConstraintCKFromSqlServer();
		Constants.CKS.clear();
		Constants.CKS.addAll(cks);

		// list primary key
		List<ConstraintPK> pks = ConstraintPKTransfer.fetchConstraintPKFromSqlServer();
		Constants.PKS.clear();
		Constants.PKS.addAll(pks);

		// list unique key
		List<ConstraintUK> uks = ConstraintUKTransfer.fetchConstraintUKFromSqlServer();
		Constants.UKS.clear();
		Constants.UKS.addAll(uks);

		// list foreign key
		List<ConstraintFK> fks = ConstraintFKTransfer.fetchConstraintFKFromSqlServer();
		Constants.FKS.clear();
		Constants.FKS.addAll(fks);
	}
}
