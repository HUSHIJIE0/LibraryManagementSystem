package com.hu.library.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Example {
//    public static void main(String[] args) {
//        // JDBC连接字符串，使用嵌入式连接
//        String url = "jdbc:h2:~/test";
//
//        try {
//            // 加载数据库驱动程序
//            Class.forName("org.h2.Driver");
//
//            // 建立数据库连接
//            Connection conn = DriverManager.getConnection(url, "sa", "");
//
//            // 创建表
//            createTable(conn);
//
//            // 插入数据
//            insertData(conn);
//
//            // 查询数据
//            queryData(conn);
//
//            // 关闭数据库连接
//            conn.close();
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//    }

    // 创建表
    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(255))";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    // 插入数据
    public static void insertData(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "INSERT INTO users (id, name) VALUES (1, 'Alice'), (2, 'Bob')";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    // 查询数据
    public static void queryData(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            System.out.println("ID: " + id + ", Name: " + name);
        }
        rs.close();
        stmt.close();
    }
}
