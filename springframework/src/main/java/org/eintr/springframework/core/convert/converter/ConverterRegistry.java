package org.eintr.springframework.core.convert.converter;

// 类型转换注册接口
public interface ConverterRegistry {
    void addConverter(Converter<?,?> convert);

    void addConverter(GenericConverter converter);

    void addConvertFactory(ConverterFactory<?, ?> converterFactory);
}
