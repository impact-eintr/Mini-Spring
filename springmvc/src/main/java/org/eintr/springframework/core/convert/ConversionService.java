package org.eintr.springframework.core.convert;

// 类型转换抽象接口
public interface ConversionService {
    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    <T> T convert(Object source, Class<?> targetType);
}
