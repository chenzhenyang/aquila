package com.highgo.test.pgcopy;

/**
 *
 * @author Liu Yuanyuan
 */
public class ObjEnum
{
    public enum DBType
    {
        HGDB, SQLSERVER
    }

    public enum DBObj
    {
        SCHEMA, TABLE, SEQUENCE, VIEW, INDEX, PROCEDURE, FUNCTION, TRIGGER
    }

    //this order is design for object list sort,so care of changing this order
    public enum ObjType
    {
        SCHEMA, TABLE,
        COLUMN, CONSTRAINT_MAIN,
        //the following commented content is for oracle
        //CONSTRAINT_COLUMN, CONSTRAINT_CHECKCONDITION, CONSTRAINT_RCOLUMN, 
        //CONSTRAINT_FR is for foreign key reference column
        CONSTRAINT_C, CONSTRAINT_F, CONSTRAINT_PK,CONSTRAINT_UQ,CONSTRAINT_FR,
        SEQUENCE, VIEW, INDEX, PROCEDURE, FUNCTION, TRIGGER,
        SCRIPT_OBJ
    }

}
