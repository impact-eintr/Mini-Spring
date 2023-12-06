package org.eintr.springframework.aop;

public class TargetSource {
    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?>[] getTargetClass() {
        return target.getClass().getInterfaces(); // 获取该类实现的所有接口
    }

    public Object getTarget() {
        return this.target;
    }
}
