import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        System.out.println(sqlDate.toLocalDate());
//        Pattern p = Pattern.compile("^get(.+)");
//        Matcher m = p.matcher("getId");
//        if (m.matches()) {
//            String whole = m.group();
//            String group1 = m.group(1);
//            System.out.println(group1);
//            System.out.println(whole);
//        }
//        System.out.println(m.matches());
    }
}