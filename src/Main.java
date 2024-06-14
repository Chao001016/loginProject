import javax.xml.transform.Result;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usercenter", "root", "@Lc001016");
        PreparedStatement pstmt = conn.prepareStatement("select * from user");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            String username = rs.getString("username");
            System.out.println(username);
        }
        System.out.println("Hello world!");
    }
}