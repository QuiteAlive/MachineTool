package cn.qzd.machinetool.helper;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by admin on 2018/5/17.
 */

public class ConnectionHelper {
    String ip,db,DBUserNameStr,DBPasswordStr;


    @SuppressLint("NewApi")
    public Connection connectionclasss()
    {
        // Declaring Server ip, username, database name and password
       // db = "QMdb";
        ip="10.255.250.245";

       /* ip="47.97.184.211";*/
       // db="QM_Server_DB2";
        db="QM_Server_DB";
//        db="DBTest";
        //db="alyTest";
        //db="Newaly714";
        DBUserNameStr ="sa";
        //DBPasswordStr="QZD_machine123";
        DBPasswordStr="QZD_machinesoft";
      /*  ip = "10.255.250.82:1433";
        db="QMTest2";
        DBUserNameStr = "sa";
        DBPasswordStr = "123456";*/
        // Declaring Server ip, username, database name and password


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
          //  DriverManager.setLoginTimeout(10);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            DriverManager.setLoginTimeout(10);
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip +";databaseName="+ db + ";user=" + DBUserNameStr+ ";password=" + DBPasswordStr + ";";
            connection = DriverManager.getConnection(ConnectionURL+"?connectTimeout=3000&socketTimeout=60000");
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}

