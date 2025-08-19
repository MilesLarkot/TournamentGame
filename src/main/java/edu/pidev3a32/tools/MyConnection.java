package edu.pidev3a32.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3306/3a32db";
    private String login = "root";
    private String pwd = "";

    private static MyConnection instance;

    private Connection cnx;

    public MyConnection(){
        try {
            cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connection established!");
        } catch (SQLException e) {
            System.out.println("Error connection /" + e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnection getInstance(){
        if (instance == null){
            instance = new MyConnection();
        }
        return instance;
    }

}
