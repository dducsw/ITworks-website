package com.ITworks.backend;

import java.sql.*;

public class JdbcConnectionTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver đã được tải thành công");

            String connectionUrl =
                    "jdbc:sqlserver://localhost:1433;" +
                    "databaseName=RECRUITMENT_WEBSITE;" +
                    "user=sa;" +
                    "password=123456;" +
                    "encrypt=true;" +
                    "trustServerCertificate=true";

            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                System.out.println("✅ Kết nối thành công với cơ sở dữ liệu!");

                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("\n🔹 Bảng: " + tableName);

                    // Nếu là bảng USER thì thêm dấu ngoặc vuông
                    String queryTableName = tableName.equalsIgnoreCase("USER") ? "[USER]" : tableName;

                    try (Statement stmt = connection.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT * FROM " + queryTableName)) {

                        ResultSetMetaData rsMeta = rs.getMetaData();
                        int columnCount = rsMeta.getColumnCount();

                        // In tên cột
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(rsMeta.getColumnName(i) + "\t");
                        }
                        System.out.println();

                        // In từng dòng dữ liệu
                        while (rs.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(rs.getString(i) + "\t");
                            }
                            System.out.println();
                        }

                    } catch (SQLException e) {
                        System.out.println("⚠️ Lỗi khi truy vấn bảng " + tableName + ": " + e.getMessage());
                    }
                }

            } catch (SQLException e) {
                System.out.println("❌ Lỗi kết nối: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Không tìm thấy driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
