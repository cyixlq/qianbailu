package top.cyixlq.network.bean;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import top.cyixlq.network.RetrofitClient;

/**
 * 例如：fun(@Param("appId")String appId)
 *  保存注解的值以及参数的值
 */
abstract class ParameterHandler {

    abstract void apply(RetrofitClient client, Object value);

    static final class Param extends ParameterHandler {

        private String name;

        Param(@NotNull String name) {
            this.name = name;
        }

        @Override
        void apply(RetrofitClient client, Object value) {
            client.addParam(name, value);
        }
    }

    static final class Params extends ParameterHandler {

        @SuppressWarnings("unchecked")
        @Override
        void apply(RetrofitClient client, Object value) {
            client.addParams((HashMap<String, Object>) value);
            /*if (value instanceof HashMap) {
                HashMap map = (HashMap) value;
                for (Object o : map.keySet()) {
                    if (o instanceof String)
                        continue;
                    throw new IllegalArgumentException("被@Params修饰的方法参数必须是HashMap<String, Object>");
                }
                client.addParams(map);
            }*/
        }
    }
}
