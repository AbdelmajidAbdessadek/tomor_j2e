package edu.inpt.tomor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSingletonConnection {
    String db = "tomor_db";
    String user = "admin";
    String pwd = "password";
    String url = "jdbc:mysql://localhost:3306/" + db;
    private static Connection connection = null;

    private DBSingletonConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pwd);
            System.out.println("instance created!!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("----------------------------- Error happened!");
        }

    }

    public static Connection getConnection() {
        if (connection == null) {
			new DBSingletonConnection();
		}
        return connection;
    }
}
