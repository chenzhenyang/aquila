package com.highgo.test.pgcopy;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Liu Yuanyuan
 */
public class ConnectHelper
{
    //use for migration assistant
    public static Connection getConnection(DBConnInfoDTO dbInfo) throws Exception
    {
        String driver = null;
        String url = null;
        switch (dbInfo.getDBType())
        {
            case HGDB:
                driver = "com.highgo.jdbc.Driver";//"org.postgresql.Driver";
                url = "jdbc:highgo://" + dbInfo.getHost() + ":" + dbInfo.getPort() + "/" + dbInfo.getDb();
                break;
//            case ORACLE:
//                driver = "oracle.jdbc.driver.OracleDriver";
//                url = "jdbc:oracle:thin:@" + dbInfo.getHost() + ":" + dbInfo.getPort() + ":" + dbInfo.getDb();
//                break;
            case SQLSERVER:
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://" + dbInfo.getHost() + ":" + dbInfo.getPort() + ";DatabaseName = " + dbInfo.getDb();
                break;
        }
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, dbInfo.getUser(), dbInfo.getPwd());
        return conn;
    }
}
