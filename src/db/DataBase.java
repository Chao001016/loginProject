package db;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.*;

@WebListener
public class DataBase implements ServletContextListener {
    private static Connection conn;
    private static String account;
    private static String password;
    private static String url;

    public static void main(String[] args) {
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usercenter", "root", "@Lc001016");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from user");

            while (rs.next()) {
                System.out.println(rs.getString("id"));
            }
            rs.close();
            System.out.println("---------");
            ResultSet rs2 = s.executeQuery("select * from question");
            rs2.close();
            while (rs2.next()) {
                System.out.println(rs.getString("content"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConn () {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                conn = DriverManager.getConnection(url, account, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取数据库连接对象
     * @return
     */
    public static Connection getConn () {
        if (conn == null) {
            initConn();
        }
        return conn;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.url = sce.getServletContext().getInitParameter("mysql_url");
        this.account = sce.getServletContext().getInitParameter("mysql_account");
        this.password = sce.getServletContext().getInitParameter("mysql_pwd");
        System.out.println(url);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
