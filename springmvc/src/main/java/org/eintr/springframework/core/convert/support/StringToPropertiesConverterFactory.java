package org.eintr.springframework.core.convert.support;

import org.eintr.springframework.core.convert.converter.Converter;
import org.eintr.springframework.core.convert.converter.ConverterFactory;
import org.eintr.springframework.util.PropertiesUtils;

import java.util.Properties;


public class StringToPropertiesConverterFactory implements ConverterFactory<String, Properties> {
    @Override
    public <T extends Properties> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToProperties<>(targetType);
    }

    private static final class StringToProperties<T extends Properties> implements Converter<String, T> {
        private final Class<T> targetType;

        public StringToProperties(Class<T> targetType) {
            this.targetType = targetType;
        }

        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return PropertiesUtils.parseProperties(source, targetType);
        }
    }

}
