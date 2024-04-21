package outil;

import java.sql.*;

public class database {
    private Connection conn;
    final String url = "jdbc:mysql://localhost:3306/deux";
    final String user = "root";
    final String pwd = "";
    static database instance;

    private database() {
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connected");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static database getInstance() {
        if (instance == null) {
            return instance = new database();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}