package com.ye.todo;

import java.sql.Connection;
import java.sql.DriverManager;

import android.util.Log;

public class MysqlManager {
    private static final String TAG = "MysqlManager";

    private static String driver = "com.mysql.jdbc.Driver";
    private static String user = "todo";
    private static String password = "123456";
    private static final String ip = "47.106.155.215";
    private static final String port = "3306";
    private static final String db = "todo";
    private static final String table = "table_repeat_mode";

    private static Connection connection;
    public static Connection connect(){
        if (connection == null) {
            try {
                Class.forName(driver);// 动态加载类

                // 尝试建立到给定数据库URL的连接
                String url = "jdbc:mysql://" + ip + ":" + port + "/" + db;
                String sufix = "?useSSL=false&serverTimezone=UTC";
                Log.d(TAG, "connect to url:" + url + sufix);
                connection = DriverManager.getConnection(url + sufix, user, password);
                Log.d(TAG, "getConnection ok!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return connection;
    }
}
