package common.Reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectHelper<T> {
    public ReflectHelper () {

    }

    public ProxyWrapper getProxyWrapper () throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ProxyWrapper ProxyWrapper = new ProxyWrapper();
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type type = superClass.getActualTypeArguments()[0];
        Class clazz = Class.forName(type.getTypeName());
        T obj = (T) clazz.newInstance();
        ProxyWrapper.setRequestData(obj);

        Map<String, Method> map = new HashMap<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            map.put(m.getName(), m);
        }
        ProxyWrapper.setMethods(map);
        return ProxyWrapper;
    }
}
