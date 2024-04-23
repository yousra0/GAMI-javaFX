package Outil;

        import java.sql.*;

public class DataBase {
    private Connection conn;
    final String url = "jdbc:mysql://localhost:3306/bdpi";
    final String user = "root";
    final String pwd = "";
    public static DataBase instance;


    public Connection getConn() {

        return conn;
    }

    private DataBase() {
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connection établie avec succès...");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static DataBase getInstance() {
        if (instance == null) {
            return instance = new DataBase();
        }
        return instance;
    }

}