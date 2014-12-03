package com.highgo.hgdbadmin.myutil;
import java.util.LinkedList;
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

public class Constants {

	// 这个用Resource相关的方法初始化时获取。
	public static final String PATH = "D:\\code\\ASqlServer-C3P0\\src\\c3p0-config.xml";
	public static final List<Schema> SCHEMAS = new LinkedList<>();
	public static final List<Table> TABLES = new LinkedList<>();
	public static final List<Sequence> SEQUENCES = new LinkedList<>();
	public static final List<View> VIEWS = new LinkedList<>();
	public static final List<Index> INDEXES = new LinkedList<>();
	public static final List<Procedure> PROCEDURES = new LinkedList<>();
	public static final List<Function> FUNCTIONS = new LinkedList<>();
	public static final List<Trigger> TRIGGERS = new LinkedList<>();
	public static final List<ConstraintCK> CKS = new LinkedList<>();
	public static final List<ConstraintPK> PKS = new LinkedList<>();
	public static final List<ConstraintUK> UKS = new LinkedList<>();
	public static final List<ConstraintFK> FKS = new LinkedList<>();

	public static final String RESOURCE_NAME = "shell-resource";
	public static final String BOLD_STR_SEQUENCE = "@|bold";
	public static final String END_STR_SEQUENCE = "|@";

	public static final String PROP_CURDIR = "user.dir";

	private Constants() {
	}
}
