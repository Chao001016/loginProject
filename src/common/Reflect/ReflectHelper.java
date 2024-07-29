package common.Reflect;

import pojo.Result;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class ReflectHelper<T> {
    private Class clazz;
    private String qualifiedName;
    private ProxyWrapper proxyWrapper;

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ReflectHelper reflectHelper = new ReflectHelper(Result.class);
        ProxyWrapper proxyWrapper = reflectHelper.getProxyWrapper();
        Method setProperty = proxyWrapper.getMethodByName("setProperty");
        setProperty.invoke(proxyWrapper.getProxy(), "12");
        Result rs = (Result) proxyWrapper.getProxy();
        System.out.println(rs.getProperty());

        //        Result rs = new Result();
//        rs.setColumn("12");
//        Object obj = rs;
//        Result rs2 = (Result) obj;
//        System.out.println(rs2.getColumn());
    }
    public <T>ReflectHelper (Class clazz) {
        this.clazz = clazz;

    }

    public ProxyWrapper getProxyWrapper () {
        if (proxyWrapper == null) {
            ProxyWrapper proxyWrapper = new ProxyWrapper();
            Object obj = null;
            try {
                obj = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            proxyWrapper.setProxy(obj);
            Map<String, Method> map = new HashMap<>();
            Method[] methods = clazz.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                map.put(m.getName(), m);
            }
            proxyWrapper.setMethods(map);
            setProxyWrapper(proxyWrapper);
        }
        return proxyWrapper;
    }

    public String getQualifiedName() {
        if (qualifiedName == null) {
            setQualifiedName(clazz.getTypeName());
        }
        return qualifiedName;
    }

    public static void invokeSet (Method method, Object obj, Object val) {
        try {
            method.invoke(obj, val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeGet (Method method, Object obj) {
        try {
           return method.invoke(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public void setProxyWrapper(ProxyWrapper proxyWrapper) {
        this.proxyWrapper = proxyWrapper;
    }
}
