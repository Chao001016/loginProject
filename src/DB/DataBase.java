package DB;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class DataBase implements ServletContextListener {
    private static Connection conn;

    private static void initConn () {
        if (conn == null) {
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
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        initConn();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
