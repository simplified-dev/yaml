package dev.sbs.api.io.yaml.converter;

import dev.sbs.api.io.yaml.InternalConverter;

import java.lang.reflect.ParameterizedType;

public class EnumConverter extends YamlConverter {

    public EnumConverter(InternalConverter converter) {
        super(converter);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object fromConfig(Class type, Object obj, ParameterizedType genericType) {
        return Enum.valueOf(type, obj.toString());
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) {
        return ((Enum<?>) obj).name();
    }

    @Override
    public boolean supports(Class<?> type) {
        return Enum.class.isAssignableFrom(type);
    }

}
