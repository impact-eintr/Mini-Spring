package org.eintr.springframework.core.convert.converter;

// 类型转换工厂
public interface ConverterFactory<S, R> {
    // 返回一个可以从S转换为T的转换器
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
