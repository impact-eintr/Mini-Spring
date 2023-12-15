package org.eintr.springframework.core.convert.converter;

public interface Converter<S, T> {
    T convert(S souurce);
}
