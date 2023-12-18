package org.eintr.springframework.aop;

// 定义匹配类 用于帮助切点找到给定的接口
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
