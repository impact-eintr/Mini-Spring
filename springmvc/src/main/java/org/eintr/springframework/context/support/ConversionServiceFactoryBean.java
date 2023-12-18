package org.eintr.springframework.context.support;

import org.eintr.springframework.beans.factory.FactoryBean;
import org.eintr.springframework.beans.factory.InitializingBean;
import org.eintr.springframework.core.convert.ConversionService;
import org.eintr.springframework.core.convert.converter.Converter;
import org.eintr.springframework.core.convert.converter.ConverterFactory;
import org.eintr.springframework.core.convert.converter.ConverterRegistry;
import org.eintr.springframework.core.convert.converter.GenericConverter;
import org.eintr.springframework.core.convert.support.DefaultConversionService;
import org.eintr.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;
    private GenericConversionService conversionService;
    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    // 工厂在设置完自己的属性后 应该去为自己的产品添加机器
    @Override
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}
