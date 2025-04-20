package com.ITworks.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcConnectionTest {
    public static void main(String[] args) {
        try {
            // Tải driver thủ công
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println(" đã được tải thành công");
            
            String connectionUrl = 
                "jdbc:sqlserver://localhost:1433;" +
                "databaseName=RECRUITMENT_WEBSITE;" +
                "user=sa;" +
                "password=123456;" +
                "encrypt=true;" +
                "trustServerCertificate=true";

            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT 'Connection successful!'")) {
                
                while (resultSet.next()) {
                    System.out.println("Kết nối thành công: " + resultSet.getString(1));
                }
            }
            catch (Exception e) {
                System.out.println("Lỗi kết nối: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
}