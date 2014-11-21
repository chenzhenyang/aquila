package com.highgo.test.pgcopy;
/**
 *
 * @author Liu Yuanyuan
 */
public class DBConnInfoDTO
{
    private ObjEnum.DBType dbType;
    private String host;
    private String port;
    private String db;
    private String user;
    private String pwd;

    public DBConnInfoDTO()
    {
    }

    public ObjEnum.DBType getDBType()
    {
        return dbType;
    }

    public void setDBType(ObjEnum.DBType dbType)
    {
        this.dbType = dbType;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getDb()
    {
        return db;
    }

    public void setDb(String db)
    {
        this.db = db;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String ped)
    {
        this.pwd = ped;
    }
}
