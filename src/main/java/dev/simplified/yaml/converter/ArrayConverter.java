package dev.sbs.api.io.yaml.converter;

import dev.sbs.api.io.yaml.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ArrayConverter extends YamlConverter {

    public ArrayConverter(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
        Class<?> singleType = type.getComponentType();
        java.util.List<Object> values;

        if (section instanceof java.util.List)
            values = (java.util.List) section;
        else
            Collections.addAll(values = new ArrayList(), (Object[]) section);

        Object ret = java.lang.reflect.Array.newInstance(singleType, values.size());
        YamlConverter converter = this.getConverter(singleType);

        if (converter == null)
            return values.toArray((Object[]) ret);

        for (int i = 0; i < values.size(); i++)
            java.lang.reflect.Array.set(ret, i, converter.fromConfig(singleType, values.get(i), genericType));

        return ret;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        Class<?> singleType = type.getComponentType();
        YamlConverter converter = this.getConverter(singleType);

        if (converter == null)
            return obj;

        Object[] ret = new Object[java.lang.reflect.Array.getLength(obj)];
        for (int i = 0; i < ret.length; i++)
            ret[i] = converter.toConfig(singleType, java.lang.reflect.Array.get(obj, i), genericType);

        return ret;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isArray();
    }

}
