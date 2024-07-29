package db.xml;

import common.Reflect.ProxyWrapper;
import common.Reflect.ReflectHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pojo.Result;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 从POJOMapper.xml中生成Result对象，用于Java属性到数据库类型的映射
 */
@WebListener
public class XMLResultMapResolver implements ServletContextListener {
    /**
     * clazz到Results的映射
     */
    private static Map<String, ArrayList> clazzResultListsMap = new HashMap<>();

    private static Map<String, Map<String, Result>> clazzResultsMapMap = new HashMap<>();

    /**
     * clazz到表名的映射
     */
    private static Map clazzTableNameMap = new HashMap<>();

    public XMLResultMapResolver() {
        if (clazzTableNameMap.size() == 0) startResolveXML();
    }

    /**
     *
     * @param clazz XML对映POJO的类
     * @return POJO每个属性对应的Result所组成的数组
     */
    public Map<String, Result> getXMLObjMap (Class clazz) {
        String qualifiedName = clazz.getTypeName();
        if (clazzResultsMapMap.get(qualifiedName) != null) {
            return clazzResultsMapMap.get(qualifiedName);
        } else {
            System.out.println("未加载【" + qualifiedName + "】");
        }
        return null;
    }

    public ArrayList getXMLObjList (Class clazz) {
        String qualifiedName = clazz.getTypeName();
        if (clazzResultListsMap.get(qualifiedName) != null) {
            return clazzResultListsMap.get(qualifiedName);
        } else {
            System.out.println("未加载【" + qualifiedName + "】");
        }
        return null;
    }

    public String getTableName (Class clazz) {
        String qualifiedName = clazz.getTypeName();
        if (clazzTableNameMap.get(qualifiedName) != null) {
            return (String) clazzTableNameMap.get(qualifiedName);
        } else {
            System.out.println("未加载【" + qualifiedName + "】");
        }
        return null;
    }


    /**
     * 将POJOMapper中的ReulstMap全部映射为每个类权限名对应的ArrayList<Result>
     * @param is
     */
    public void resolveXMLObjList (InputStream is) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = null;
        try {
            document = documentBuilder.parse(is);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NodeList resultMaps = document.getElementsByTagName("resultMap");
        for (int j = 0; j < resultMaps.getLength(); j++) {
            Element resultMap = (Element)resultMaps.item(j);
            String id = resultMap.getAttributes().getNamedItem("id").getNodeValue();
            String tableName = resultMap.getAttributes().getNamedItem("tableName").getNodeValue();
            clazzTableNameMap.put(id, tableName);
            NodeList results = resultMap.getElementsByTagName("result");
            ArrayList<Result> TList = new ArrayList<>();
            Map<String, Result> map = new HashMap<>();
            for (int i = 0; i < results.getLength(); i++) {
                Node result = results.item(i);
                String property = result.getAttributes().getNamedItem("property").getNodeValue();
                String column = result.getAttributes().getNamedItem("column").getNodeValue();
                String jdbcType = result.getAttributes().getNamedItem("jdbcType").getNodeValue();
                ReflectHelper reflectHelper = null;
                reflectHelper = new ReflectHelper(Result.class);
                ProxyWrapper proxyWrapper = null;
                proxyWrapper = reflectHelper.getProxyWrapper();
                Method setProperty = proxyWrapper.getSetterByName("property");
                // TODO 为了避免过多的try catch 语句，应该封装一个方法来进行方法调用
                try {
                    setProperty.invoke(proxyWrapper.getProxy(), property);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                Method setColumn = proxyWrapper.getSetterByName("column");
                try {
                    setColumn.invoke(proxyWrapper.getProxy(), column);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                Method setJdbcType = proxyWrapper.getSetterByName("jdbcType");
                try {
                    setJdbcType.invoke(proxyWrapper.getProxy(), jdbcType);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                TList.add((Result) proxyWrapper.getProxy());
                map.put(property, (Result) proxyWrapper.getProxy());
            }
            clazzResultsMapMap.put(id, map);
            clazzResultListsMap.put(id, TList);
        }
    }

    public void startResolveXML() {
        InputStream is1 = XMLResultMapResolver.class.getResourceAsStream("./POJOMapper.xml");
        resolveXMLObjList(is1);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        startResolveXML();
        System.out.println("contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
