package org.eintr.springframework.core.convert.support;

import org.eintr.springframework.core.convert.ConversionService;
import org.eintr.springframework.core.convert.converter.Converter;
import org.eintr.springframework.core.convert.converter.ConverterFactory;
import org.eintr.springframework.core.convert.converter.ConverterRegistry;
import org.eintr.springframework.core.convert.converter.GenericConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericConversionService implements ConversionService, ConverterRegistry {

    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        GenericConverter converter = getConverter(sourceType, targetType);
        return false;
    }

    @Override
    public <T> T convert(Object source, Class<?> targetType) {
        return null;
    }

    @Override
    public void addConverter(Converter<?, ?> convert) {

    }

    @Override
    public void addConverter(GenericConverter converter) {

    }

    @Override
    public void addConvertFactory(ConverterFactory<?, ?> converterFactory) {

    }

    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate)
        }
    }

    // 获取一个类的继承链
    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        while (clazz != null) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }
}
