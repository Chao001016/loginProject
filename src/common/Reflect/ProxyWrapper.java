package common.Reflect;

import java.lang.reflect.Method;
import java.util.Map;

public class ProxyWrapper<T> {
    T proxy;
    Map<String, Method> methodMap;
    public void setProxy (T data) {
        this.proxy = data;
    }
    public void setMethods (Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }
    public T getProxy  () {
        return this.proxy;
    }
    public Map<String, Method> getMethodMap () {
        return this.methodMap;
    }
    public Method getMethodByName (String methodName) {
        return this.methodMap.get(methodName);
    }
    public Method getSetterByName (String property) {
        String methodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
        return this.methodMap.get(methodName);
    }
    public Method getGetterByName (String property) {
        String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        return this.methodMap.get(methodName);
    }
}
