package db.wrapper;

import common.Common;
import common.Reflect.ReflectHelper;
import db.DataBase;
import db.wrapper.map.InsertMap;
import db.xml.XMLResultMapResolver;
import org.xml.sax.SAXException;
import pojo.Question;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InsertWrapper implements BaseWrapper<Long> {
    private Class clazz;
    /**
     * 需要插入的字段
     */
    private InsertMap insertMap = new InsertMap();

    public static void main(String[] args) throws SQLException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        InsertWrapper insertWrapper = new InsertWrapper(Question.class);
        insertWrapper.put("content", "什么是迭代和进化式开发");
        insertWrapper.put("type", "4");
        insertWrapper.put("answer", "");
        insertWrapper.put("analysis", "迭代开发是对原有多次迭代的系统进行持续扩展和精化，并以循环反馈和调整为核心驱动力，使之成为适当的系统。随着时间和一次又一次的迭代的递进，系统增量式地发展完善");
        Long id = insertWrapper.execute();
        System.out.println(id);
    }
    public InsertWrapper(Class clazz) {
        this.clazz = clazz;
    }

    public InsertWrapper(Object obj) {
        this.clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        ReflectHelper reflectHelper = new ReflectHelper(clazz);
        for (Field field : fields) {
            String property = field.getName();
            Method getter = reflectHelper.getProxyWrapper().getGetterByName(property);
            Object val = ReflectHelper.invokeGet(getter, obj);
            if (val != null) {
                this.put(property, val);
            }
        }
    }

    @Override
    public Long execute() {
        try {
            return executeInsert();
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

    public Long executeInsert () throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParserConfigurationException, IOException, SAXException {
        PreparedStatement pstt = this.generateSQL();
        pstt.execute();
        ResultSet rs = pstt.getGeneratedKeys();
        Long id = null;
        while(rs.next()) {
            id = rs.getLong(1);
        };
        return id;
    }


    public void put (String key, Object val) {
        insertMap.put(key, val);
    }

    public PreparedStatement generateSQL () {
        Connection conn = DataBase.getConn();
        XMLResultMapResolver xmlResultMapResolver = new XMLResultMapResolver();
        String tableName = xmlResultMapResolver.getTableName(clazz);
        // insert question(type,content,score) values(1,2,3);
        String sql = "insert into " + tableName;
        sql += insertMap.increaseSQL(clazz);

        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int i = 1;
        insertMap.setParameter(pstt, i);
        return pstt;
    }
}
