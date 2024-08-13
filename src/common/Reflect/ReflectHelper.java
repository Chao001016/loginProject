package common.Reflect;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class ReflectHelper {
    private Class clazz;
    private ProxyWrapper proxyWrapper;

    public ReflectHelper (Class clazz) {
        this.clazz = clazz;

    }

    public ProxyWrapper getProxyWrapper () {
        if (proxyWrapper == null) {
            proxyWrapper = new ProxyWrapper<>();
            Object obj;
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
        }
        return this.proxyWrapper;
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
}
