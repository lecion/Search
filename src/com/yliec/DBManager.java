package com.yliec;

import java.sql.*;

/**
 * 数据库管理类
 * Created by Lecion on 1/22/15.
 */
public class DBManager {
    private static Connection conn = null;

    private PreparedStatement psmt = null;

    private ResultSet rs = null;

    private static final String DBDRIVER = "com.mysql.jdbc.Driver";

    private static final String DBURL = "jdbc:mysql://localhost:3306/Search";

    private static final String DBUSER = "lecion";

    private static final String DBPWD = "123";
    static {
        try {
            Class.forName(DBDRIVER);
            conn = DriverManager.getConnection(DBURL, DBUSER, DBPWD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public DBManager() {
    }

    public static Connection getConnection() {
        return conn;
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
