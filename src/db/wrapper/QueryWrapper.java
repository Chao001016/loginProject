package db.wrapper;

import common.Reflect.ProxyWrapper;
import common.Reflect.ReflectHelper;
import db.DataBase;
import db.wrapper.map.ConditionMap;
import db.wrapper.queue.OrderQueue;
import db.wrapper.queue.SortType;
import db.wrapper.queue.SqlSort;
import db.xml.XMLResultMapResolver;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.xml.sax.SAXException;
import pojo.Question;
import pojo.Result;
import pojo.Tag;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryWrapper<T> implements BaseWrapper<ArrayList> {
    private Class clazz;
    private ConditionMap condition = new ConditionMap();
    private ConditionMap likeCondition = new ConditionMap();
    // 升序队列
    private OrderQueue orderQueue = new OrderQueue();
    public static void main(String[] args) throws SQLException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Queue<String> queue = new LinkedList<>();
        String property;
        queue.offer("updateTime");
        queue.offer("createTime");
        Map<String, Result> resultMap = new XMLResultMapResolver().getXMLObjMap(Question.class);
        while ((property = queue.poll()) != null) {
            Result result = resultMap.get(property);
            String column = result.getColumn();
            System.out.println(column);
        }



//        QueryWrapper queryWrapper = new QueryWrapper(Question.class);
//        queryWrapper.addCondition("id", "2");
//        queryWrapper.addCondition("content", "lichao6");
//        ArrayList<Question> questionArrayList = queryWrapper.executeQuery();
//        for (Question question : questionArrayList) {
//            System.out.println(question.getContent());
//        }
    }
    public QueryWrapper(Class clazz) {
        this.clazz = clazz;
    }

    public QueryWrapper(Object obj) {
        this.clazz = obj.getClass();
        Method[] methods = this.clazz.getDeclaredMethods();
        for (Method method : methods) {
            Pattern pattern = Pattern.compile("^get(.+)");
            Matcher matcher = pattern.matcher(method.getName());
            if (matcher.matches()) {
                Object val = ReflectHelper.invokeGet(method, obj);
                if (val != null) {
                    String group = matcher.group(1);
                    String prop = group.substring(0,1).toLowerCase() + group.substring(1);
                    eq(prop, val);
                }
            }
        }
    }

    public ArrayList executeQuery () {
        PreparedStatement pstt = null;
        pstt = this.generateSQL();
        ResultSet rs = null;
        try {
            rs = pstt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ReflectHelper reflectHelper = new ReflectHelper(clazz);
            ProxyWrapper proxyWrapper = reflectHelper.getProxyWrapper();

            Class resultSetClazz = rs.getClass();
            ArrayList<Result> results =  new XMLResultMapResolver().getXMLObjList(clazz);
            for (Result result : results) {
               String property = result.getProperty();
               String jdbcType = result.getJdbcType();
               String column = result.getColumn();
               switch (jdbcType) {
                   case "LONG":
                   case "BIGINT": {
                       Method getLong = invokeGetMethod(resultSetClazz, "getLong", String.class);
                       Long id = (Long) invokeResultSetGet(getLong, rs, column);
                       if (id == null) break;;
                       Method setter = proxyWrapper.getSetterByName(property);
                       ReflectHelper.invokeSet(setter, proxyWrapper.getProxy(), id);
                       break;
                   }
                   case "TINYINT":
                   case "INTEGER": {
                       Method getInt = invokeGetMethod(resultSetClazz, "getInt", String.class);
                       Integer integer = (Integer) invokeResultSetGet(getInt, rs, column);
                       if (integer == null) break;
                       Method setter = proxyWrapper.getSetterByName(property);
                       ReflectHelper.invokeSet(setter, proxyWrapper.getProxy(), integer);
                       break;
                   }
                   case "TIMESTAMP": {
                       Method getTimestamp = invokeGetMethod(resultSetClazz, "getTimestamp", String.class);
                       Date date = (Date) invokeResultSetGet(getTimestamp, rs, column);
                       if (date == null) break;
                       Method setter = proxyWrapper.getSetterByName(property);
                       ReflectHelper.invokeSet(setter, proxyWrapper.getProxy(), date);
                       break;
                   }
                   case "BOOLEAN": {
                       Method getBoolean = invokeGetMethod(resultSetClazz, "getBoolean", String.class);
                       Boolean bool = (Boolean) invokeResultSetGet(getBoolean, rs, column);
                       if (bool == null) break;
                       Method setter = proxyWrapper.getSetterByName(property);
                       ReflectHelper.invokeSet(setter, proxyWrapper.getProxy(), bool);
                       break;
                   }
                   default: {
                       Method getString = invokeGetMethod(resultSetClazz, "getString", String.class);
                       String str = (String) invokeResultSetGet(getString, rs, column);
                       if (str == null) break;
                       Method setter = proxyWrapper.getSetterByName(property);
                       ReflectHelper.invokeSet(setter, proxyWrapper.getProxy(), str);
                   }
               }
            }
            arrayList.add(proxyWrapper.getProxy());

            // 通过POJO的属性，获取对应的数据库列名，已经数据库类型，然后调用rs.set方法获取值， 再调用代理对象的set方法，给代理对象的属性赋值
            // ResultHelper类就是保存 property 到 数据库列名 和 数据库列类型 的关系
            // 这里有一个pojo 属性与数据库列名和类型的一对一关系，预想这里通过xml来配置，然后获取xml标签信息，创建ResultHelper类

        }
        return arrayList;
    }

    public void descBy (String property) {
        orderQueue.offer(new SqlSort(SortType.DESC, property));
    }

    public void ascBy (String property) {
        orderQueue.offer(new SqlSort(SortType.DESC, property));
    }

    public Object invokeResultSetGet (Method method, Object obj, String column) {
        try {
            return method.invoke(obj, column);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Method invokeGetMethod (Class targetClazz, String methodName, Class argClazz) {
        try {
            return targetClazz.getMethod(methodName, argClazz);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList execute() throws RuntimeException {
        return this.executeQuery();
    }

    public void addCondition (String key, String val) {
        condition.put(key, val);
    }

    public void eq (String key, Object val) {
        condition.put(key, val);
    }

    // TODO 模糊匹配
    public void like (String key, String val) {

    }

    @Override
    public PreparedStatement generateSQL () {
        Connection conn = DataBase.getConn();
        XMLResultMapResolver xmlResultMapResolver = new XMLResultMapResolver();
        String tableName = xmlResultMapResolver.getTableName(clazz);
        String sql = "select * from " + tableName;
        // 查询条件
        sql += condition.increaseSQL(clazz);
        // 排序字段
        sql += orderQueue.increaseSQL(clazz);
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int i = 1;
        condition.setParameter(pstt, i);
        return pstt;
    }
}
