package db;

import common.Common;
import common.Reflect.ProxyWrapper;
import common.Reflect.ReflectHelper;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryWrapper<T> {
    private String sql = "select * from user";
    private Map condition = new HashMap<String, String>();
    public static void main(String[] args) throws SQLException, IOException, SAXException, ParserConfigurationException {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.addCondition("id", "2");
        queryWrapper.addCondition("content", "lichao6");
        queryWrapper.addCondition("option_id", "1,2");


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/db/xml/POJOMapper.xml"));
        Element root = document.getDocumentElement(); // document.getElementById("pojo.Question");
        Element resultMap = document.getElementById("pojo.Question");
        System.out.println(resultMap.getTagName());

        NodeList elements = resultMap.getElementsByTagName("result");
        for (int i = 0; i < elements.getLength(); i++) {
            Node node = elements.item(i);
            NamedNodeMap namedNodeMap = node.getAttributes();
            System.out.println(namedNodeMap.getNamedItem("property").getNodeValue());
        }
    }
    public QueryWrapper () {

    }

    // 执行查询 TODO
    public ArrayList<T> executeQuery () throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        PreparedStatement pstt = this.generateSQL();
        ResultSet rs = pstt.executeQuery();
        while (rs.next()) {
            ReflectHelper reflectHelper = new <T>ReflectHelper();
            ProxyWrapper proxyWrapper = reflectHelper.getProxyWrapper();
            Class clazz = Class.forName(ResultSet.class.getName());
            Object rsObj = clazz.newInstance();
            // 通过POJO的属性，获取对应的数据库列名，已经数据库类型，然后调用rs.set方法获取值， 再调用代理对象的set方法，给代理对象的属性赋值
            // ResultHelper类就是保存 property 到 数据库列名 和 数据库列类型 的关系
            // 这里有一个pojo 属性与数据库列名和类型的一对一关系，预想这里通过xml来配置，然后获取xml标签信息，创建ResultHelper类

        }
        return null;
    }


    public void addCondition (String key, String val) {
        condition.put(key, val);
    }

    public PreparedStatement generateSQL () throws SQLException {
        Connection conn = DataBase.getConn();
        String sql = "select * from question";
        String[] conditionArr = new String[condition.size()];
        int i = 0;
        for (Object key : condition.keySet()) {
            conditionArr[i++] = key + "=?";
        }
        if (conditionArr.length > 0) {
            sql += " where " + Common.join(conditionArr, " and ");
        }
        PreparedStatement pstt = conn.prepareStatement(sql);
        i = 1;
        for (Object key : condition.keySet()) {
            String val = (String) condition.get(key);
            pstt.setString(i++, val);
        }
        return pstt;
    }


}
