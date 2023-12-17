package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.FactoryBean;
import org.eintr.springframework.annotation.beans.Value;
import org.eintr.springframework.annotation.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component("userDao")
public class ProxyBeanFactory implements FactoryBean<IUserDao> {
    @Value("EINTR")
    public String name;

    @Value("10000")
    private String uId;

    @Value("天津")
    private String location;

    @Override
    public IUserDao getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {
            // 添加排除方法
            if ("toString".equals(method.getName())) return this.toString();

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("1000", "AAA");
            hashMap.put("1001", "BBB");
            hashMap.put("1002", "CCC");

            System.out.println(args[0]);

            return "你被代理了 " + method.getName() + "：" + hashMap.get(args[0].toString());
        };

        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
