package servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import common.BaseResponse;
import common.Reflect.ProxyWrapper;
import common.Reflect.ReflectHelper;
import db.xml.XMLResultMapResolver;
import org.xml.sax.SAXException;
import pojo.Question;
import pojo.Result;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonServlet<T> extends HttpServlet {
    public CommonServlet() {
        super();
    }

    public abstract BaseResponse service(T jsonObject, HttpServletRequest req, HttpServletResponse res);

    // 增加获取请求参数的方法
    public JSONObject getParameter (HttpServletRequest req) throws IOException {
        String method = req.getMethod();
        String contentType = req.getContentType();
        if (method.equals("POST")) {
            if (contentType.contains("application/json")) {
                BufferedReader br = req.getReader();
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObject = JSON.parseObject(sb.toString());
                return jsonObject;
            } else {
                JSONObject jsonObject = new JSONObject();
                Collection<Part> parts;
                try {
                    parts = req.getParts();
                } catch (ServletException e) {
                    throw new RuntimeException(e);
                }
                for (Part part : parts) {
                    String filedName = part.getName();
                    jsonObject.put(filedName, jsonObject);
                }
                return jsonObject;
            }
        } else if (method.equals("GET")) {
            JSONObject jsonObject = new JSONObject();
            String queryString = req.getQueryString();
            for (String s : queryString.split("&")) {
                String[] keyVal = s.split("=");
                jsonObject.put(keyVal[0], keyVal[1]);
            }
            return jsonObject;
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return super.getLastModified(req);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      super.doHead(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("application/type");
        resp.setCharacterEncoding("UTF-8");
        JSONObject jsonObject = null;
        try {
            jsonObject = getParameter(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BaseResponse br;
        ProxyWrapper proxyWrapper;
        // 1.获取泛型类型
        proxyWrapper = getProxyWrapper();
        // 2.装配泛型实例
        assembleParameter(proxyWrapper, jsonObject);
        br = this.service((T) proxyWrapper.getProxy(), req, resp);
        if (br != null) {
            try {
                resp.getWriter().write(JSON.toJSONString(br));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void assembleParameter (ProxyWrapper proxyWrapper, JSONObject jsonObject) {
        XMLResultMapResolver xmlResultMapResolver = new XMLResultMapResolver();
        Class clazz = proxyWrapper.getProxy().getClass();
        if (clazz != JSONObject.class) {
            ArrayList<Result> resultArrayList = xmlResultMapResolver.getXMLObjList(proxyWrapper.getProxy().getClass());
            for (Result result : resultArrayList) {
                String property = result.getProperty();
                String jdbcType = result.getJdbcType();
                Object valObj = jsonObject.get(property);
                String val = null;
                if (valObj != null) {
                    val = String.valueOf(jsonObject.get(property));
                }
                if (val != null) {
                    Method setter = proxyWrapper.getSetterByName(property);
                    switch (jdbcType) {
                        case "LONG":
                        case "BIGINT": {
                            invoke(setter, proxyWrapper.getProxy(), Long.valueOf(val));
                            break;
                        }
                        case "INTEGER": {
                            invoke(setter, proxyWrapper.getProxy(), Integer.valueOf(val));
                            break;
                        }
                        case "VARCHAR": {
                            invoke(setter, proxyWrapper.getProxy(), val);
                            break;
                        }
                        default: {
                            invoke(setter, proxyWrapper.getProxy(), val);
                        }
                    }
                }
            }
        } else {
            proxyWrapper.setProxy(jsonObject);
        }
    }

    public void invoke (Method method, Object obj, Object val) {
        try {
            method.invoke(obj, val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取CommonServlet的泛型类的ProxyWrapper
     * @return
     */
    private ProxyWrapper getProxyWrapper () {
        // 获取泛型类T的权限名
        Type superclass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        Class clazz = null;
        try {
            clazz = Class.forName(type.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ReflectHelper reflectHelper = new ReflectHelper(clazz);
        return reflectHelper.getProxyWrapper();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }
}


