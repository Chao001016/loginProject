package db.wrapper;

import db.DataBase;
import db.wrapper.map.ConditionMap;
import db.xml.XMLResultMapResolver;
import org.xml.sax.SAXException;
import pojo.Question;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteWrapper<T> implements BaseWrapper<Integer> {
    private Class clazz;
    private ConditionMap condition = new ConditionMap();
    public static void main(String[] args) throws SQLException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        DeleteWrapper deleteWrapper = new DeleteWrapper(Question.class);
        deleteWrapper.eq("id", "8");
        Integer num = deleteWrapper.execute();
        System.out.println(num);
    }
    public DeleteWrapper(Class clazz) {
        this.clazz = clazz;
    }

    public Integer executeDelete () throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParserConfigurationException, IOException, SAXException {
        PreparedStatement pstt = this.generateSQL();
        Integer affectedRows = pstt.executeUpdate();
        return affectedRows;
    }

    @Override
    public Integer execute() {
        try {
            return this.executeDelete();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void eq (String key, Object val) {
        condition.put(key, val);
    }

    // TODO 模糊匹配
    public void like (String key, String val) {

    }

    @Override
    public PreparedStatement generateSQL () throws ParserConfigurationException, IOException, ClassNotFoundException, InvocationTargetException, SAXException, InstantiationException, IllegalAccessException, NoSuchMethodException, SQLException {
        Connection conn = DataBase.getConn();
        XMLResultMapResolver xmlResultMapResolver = new XMLResultMapResolver();
        String tableName = xmlResultMapResolver.getTableName(clazz);
        String sql = "delete from " + tableName;
        sql += condition.increaseSQL(clazz);
        PreparedStatement pstt = conn.prepareStatement(sql);
        int i = 1;
        condition.setParameter(pstt, i);
        return pstt;
    }


}
