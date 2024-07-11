package servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.BaseResponse;
import common.Reflect.ProxyWrapper;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonServlet<T> extends HttpServlet {
    public CommonServlet() {
        super();
    }

    public abstract BaseResponse service(T jsonObject, HttpServletRequest req, HttpServletResponse res) throws SQLException;

    private T getGenericType () throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        T superclass = (T) getClass().getGenericSuperclass();
//        if (superclass instanceof Class) {
//            throw new RuntimeException("Missing type parameter.");
//        }
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        Class clazz = Class.forName(type.getTypeName());
        T genericType = (T) clazz.newInstance();
        System.out.println(type.getTypeName());
        return superclass;
    }
    // 增加获取请求参数的方法
    public JSONObject getParameter (HttpServletRequest req) throws IOException {
        BufferedReader br = req.getReader();
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JSONObject jsonObject = JSON.parseObject(sb.toString());
        return jsonObject;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/type");
        resp.setCharacterEncoding("UTF-8");
        JSONObject jsonObject = getParameter(req);
        BaseResponse br = null;
        ProxyWrapper proxyWrapper;
        // 1.获取泛型类型
        try {
            proxyWrapper = getProxyWrapper(jsonObject);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 2.装配泛型实例
        assembleParameter(proxyWrapper, jsonObject);
        try {
            br = this.service((T) proxyWrapper.getProxy(), req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.getWriter().write(JSON.toJSONString(br));
    }

    public void assembleParameter (ProxyWrapper proxyWrapper, JSONObject jsonObject) {
        jsonObject.entrySet().forEach(info -> {
            String key = info.getKey();
            if (key != null) {
                // 获取set方法
                String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                Method m = proxyWrapper.getMethodByName(methodName);
                try {
                    m.invoke(proxyWrapper.getProxy(), info.getValue());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private ProxyWrapper getProxyWrapper (JSONObject jsonObject) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ProxyWrapper<T> proxyWrapper = new ProxyWrapper();
        // 获取泛型类T的权限名
        T superclass = (T) getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];

        // 创建泛型类T实例
        Class clazz = Class.forName(type.getTypeName());
        T obj = (T)clazz.newInstance();
        if (obj instanceof JSONObject) {
            proxyWrapper.setRequestData((T) jsonObject);
            return proxyWrapper;
        }
        proxyWrapper.setRequestData(obj);
        // 获取methods
        Method[] methods = clazz.getDeclaredMethods();
        Map map = new HashMap<String, Method>();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            map.put(m.getName(), m);
            System.out.println(m.getName());
        }
        proxyWrapper.setMethods(map);
        return proxyWrapper;
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


