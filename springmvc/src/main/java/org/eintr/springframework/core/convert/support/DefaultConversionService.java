package org.eintr.springframework.core.convert.support;

import org.eintr.springframework.core.convert.converter.ConverterRegistry;

public class DefaultConversionService extends GenericConversionService {
    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        // 添加 各类型转换工厂
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
    }
}
