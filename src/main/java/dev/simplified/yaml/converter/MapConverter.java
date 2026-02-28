package dev.sbs.api.io.yaml.converter;

import dev.sbs.api.io.yaml.ConfigSection;
import dev.sbs.api.io.yaml.InternalConverter;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class MapConverter extends YamlConverter {

    public MapConverter(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
        java.util.Map<Object, Object> map;

        if (genericType != null) {
            try {
                map = (java.util.Map<Object, Object>) Reflection.of((Class<?>) genericType.getRawType()).newInstance();
            } catch (ReflectionException e) {
                map = new HashMap<>();
            }

            if (genericType.getActualTypeArguments().length == 2) {
                Class<?> keyClass = ((Class<?>) genericType.getActualTypeArguments()[0]);

                if (section == null)
                    section = new HashMap<>();

                java.util.Map<?, ?> map1 = (section instanceof java.util.Map) ? (java.util.Map<?, ?>) section : ((ConfigSection) section).getRawMap();
                for (java.util.Map.Entry<?, ?> entry : map1.entrySet()) {
                    Object key;

                    if (keyClass.equals(Integer.class) && !(entry.getKey() instanceof Integer))
                        key = Integer.valueOf((String) entry.getKey());
                    else if (keyClass.equals(Short.class) && !(entry.getKey() instanceof Short))
                        key = Short.valueOf((String) entry.getKey());
                    else if (keyClass.equals(Byte.class) && !(entry.getKey() instanceof Byte))
                        key = Byte.valueOf((String) entry.getKey());
                    else if (keyClass.equals(Float.class) && !(entry.getKey() instanceof Float))
                        key = Float.valueOf((String) entry.getKey());
                    else if (keyClass.equals(Double.class) && !(entry.getKey() instanceof Double))
                        key = Double.valueOf((String) entry.getKey());
                    else
                        key = entry.getKey();

                    Class<?> clazz;
                    if (genericType.getActualTypeArguments()[1] instanceof ParameterizedType parameterizedType1)
                        clazz = (Class<?>) parameterizedType1.getRawType();
                    else
                        clazz = (Class<?>) genericType.getActualTypeArguments()[1];

                    YamlConverter converter = this.getConverter(clazz);
                    map.put(key, (converter != null ? converter.fromConfig(clazz, entry.getValue(), (genericType.getActualTypeArguments()[1] instanceof ParameterizedType) ? (ParameterizedType) genericType.getActualTypeArguments()[1] : null) : entry.getValue()));
                }
            } else {
                YamlConverter converter = this.getConverter((Class<?>) genericType.getRawType());
                if (converter != null) return converter.fromConfig((Class<?>) genericType.getRawType(), section, null);
                return (section instanceof java.util.Map) ? (java.util.Map<?, ?>) section : ((ConfigSection) section).getRawMap();
            }
        } else {
            try {
                map = (java.util.Map<Object, Object>) Reflection.of(type).newInstance();
            } catch (ReflectionException e) {
                map = new HashMap<>();
            }

            java.util.Map<?, ?> sectionMap = (section instanceof java.util.Map) ? (java.util.Map<?, ?>) section : ((ConfigSection) section).getRawMap();

            for (java.util.Map.Entry<?, ?> entry : sectionMap.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof ConfigSection)
                    value = ((ConfigSection) value).getRawMap();

                YamlConverter converter = this.getConverter(value.getClass());
                map.put(key, (converter != null ? converter.fromConfig(value.getClass(), value, null) : value));
            }
        }

        return map;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        java.util.Map<Object, Object> map = (java.util.Map<Object, Object>) obj;

        for (java.util.Map.Entry<Object, Object> entry : map.entrySet()) {
            if (entry.getValue() == null) continue;
            Class<?> clazz = entry.getValue().getClass();
            YamlConverter converter = this.getConverter(clazz);
            map.put(entry.getKey(), (converter != null ? converter.toConfig(clazz, entry.getValue(), null) : entry.getValue()));
        }

        return map;
    }

    @Override
    public boolean supports(Class<?> type) {
        return java.util.Map.class.isAssignableFrom(type);
    }

}
