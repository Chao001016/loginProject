import javax.xml.transform.Result;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Pattern p = Pattern.compile("^get(.+)");
        Matcher m = p.matcher("getId");
        if (m.matches()) {
            String whole = m.group();
            String group1 = m.group(1);
            System.out.println(group1);
            System.out.println(whole);
        }
        System.out.println(m.matches());
    }
}