package dev.sbs.api.io.yaml.converter;

import dev.sbs.api.io.yaml.InternalConverter;
import dev.sbs.api.util.NumberUtil;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;

public class PrimitiveConverter extends YamlConverter {

    private static final HashSet<String> types = new HashSet<>();

    static {
        types.addAll(Arrays.asList("boolean", "character", "byte", "short", "integer", "long", "float", "double", "string"));
    }

    public PrimitiveConverter(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
        if (section == null)
            return null;

        if (type.isAssignableFrom(section.getClass()))
            return section;

        if (section instanceof Number) {
            Number number = NumberUtil.createNumber(section.toString());

            switch (type.getSimpleName().toLowerCase()) {
                case "byte":
                    return number.byteValue();
                case "short":
                    return number.shortValue();
                case "integer":
                    return number.intValue();
                case "long":
                    return number.longValue();
                case "double":
                    return number.doubleValue();
                case "float":
                    return number.floatValue();
            }
        } else {
            switch (type.getSimpleName().toLowerCase()) {
                case "boolean":
                    return section;
                case "character":
                    return ((String) section).charAt(0);
                case "string":
                    return section.toString();
            }
        }

        return section;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        return obj;
    }

    @Override
    public boolean supports(Class<?> type) {
        return types.contains(type.getSimpleName().toLowerCase());
    }

}
