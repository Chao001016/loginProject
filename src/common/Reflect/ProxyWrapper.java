package common.Reflect;

import java.lang.reflect.Method;
import java.util.Map;

public class ProxyWrapper<T> {
    T proxy;
    Map<String, Method> methodMap;
    public void setRequestData (T data) {
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
}
