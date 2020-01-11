package top.cyixlq.network;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import top.cyixlq.network.bean.ServiceMethod;

public class ByNet {

    private static volatile ByNet instance;

    private ByNet() {}

    private final Map<Method, ServiceMethod> serviceMethodCacheMap = new ConcurrentHashMap<>();

    public static ByNet get() {
        if (instance == null) {
            synchronized (ByNet.class) {
                if (instance == null) {
                    instance = new ByNet();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (o, method, args) ->
                getMethodParamsFromCache(method).invoke(args)
        );
    }

    // 获取方法名，方法注解，方法参数注解，方法参数
    private ServiceMethod getMethodParamsFromCache(Method method) {
        ServiceMethod result = serviceMethodCacheMap.get(method);
        if (result != null) return result;
        synchronized (serviceMethodCacheMap) {
            result = serviceMethodCacheMap.get(method);
            if (result == null) {
                result = new ServiceMethod.Builder(RetrofitClient.create(), method).build();
                serviceMethodCacheMap.put(method, result);
            }
        }
        return result;
    }
}
