package dev.sbs.api.io.yaml.converter;

import dev.sbs.api.io.yaml.ConfigSection;
import dev.sbs.api.io.yaml.InternalConverter;
import dev.sbs.api.io.yaml.YamlMap;
import dev.sbs.api.reflection.Reflection;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class ConfigConverter extends YamlConverter {

    public ConfigConverter(InternalConverter converter) {
        super(converter);
    }

    private static Object newInstance(Class<?> type) {
        Class<?> enclosingClass = type.getEnclosingClass();

        if (enclosingClass != null)
            return Reflection.of(type).newInstance(newInstance(enclosingClass));

        return Reflection.of(type).newInstance();
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
        YamlMap obj = (YamlMap) newInstance(type);
        this.getCustomConverters().forEach(obj::addCustomConverter);
        obj.loadFromMap((section instanceof Map) ? (Map<?, ?>) section : ((ConfigSection) section).getRawMap(), type);
        return obj;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        if (obj instanceof Map)
            return obj;
        else {
            YamlMap map = (YamlMap) obj;
            this.getCustomConverters().forEach(map::addCustomConverter);
            return map.saveToMap(obj.getClass());
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return YamlMap.class.isAssignableFrom(type);
    }

}
