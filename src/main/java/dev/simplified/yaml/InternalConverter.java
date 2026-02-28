package dev.sbs.api.io.yaml;

import dev.sbs.api.io.yaml.annotation.Flag;
import dev.sbs.api.io.yaml.converter.ArrayConverter;
import dev.sbs.api.io.yaml.converter.ConfigConverter;
import dev.sbs.api.io.yaml.converter.EnumConverter;
import dev.sbs.api.io.yaml.converter.ListConverter;
import dev.sbs.api.io.yaml.converter.MapConverter;
import dev.sbs.api.io.yaml.converter.PrimitiveConverter;
import dev.sbs.api.io.yaml.converter.SetConverter;
import dev.sbs.api.io.yaml.converter.YamlConverter;
import dev.sbs.api.io.yaml.exception.InvalidConverterException;
import dev.sbs.api.reflection.accessor.FieldAccessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class InternalConverter {

    private final transient LinkedHashSet<YamlConverter> converters = new LinkedHashSet<>();
    private final transient LinkedHashSet<YamlConverter> customConverters = new LinkedHashSet<>();
    private final transient Set<Class<? extends YamlConverter>> customConverterClasses = new HashSet<>();

    public InternalConverter() {
        this.addConverter(PrimitiveConverter.class);
        this.addConverter(ConfigConverter.class);
        this.addConverter(ArrayConverter.class);
        this.addConverter(ListConverter.class);
        this.addConverter(EnumConverter.class);
        this.addConverter(MapConverter.class);
        this.addConverter(SetConverter.class);
    }

    private void addConverter(Class<? extends YamlConverter> converter) throws InvalidConverterException {
        this.addConverter(converter, this.converters);
    }

    private void addConverter(Class<? extends YamlConverter> converter, LinkedHashSet<YamlConverter> converters) throws InvalidConverterException {
        try {
            converters.add(converter.getConstructor(InternalConverter.class).newInstance(this));
        } catch (NoSuchMethodException nsmException) {
            throw new InvalidConverterException(nsmException, "Converter does not implement a constructor which takes the InternalConverter instance.");
        } catch (InvocationTargetException intException) {
            throw new InvalidConverterException(intException, "Converter could not be invoked.");
        } catch (InstantiationException insException) {
            throw new InvalidConverterException(insException, "Converter could not be instantiated.");
        } catch (IllegalAccessException ilaException) {
            throw new InvalidConverterException(ilaException, "Converter does not implement a public Constructor which takes the InternalConverter instance.");
        }
    }

    protected void addCustomConverter(Class<? extends YamlConverter> converter) throws InvalidConverterException {
        this.addConverter(converter, this.customConverters);
        this.customConverterClasses.add(converter);
    }

    public void fromConfig(YamlMap yamlMap, FieldAccessor<?> accessor, ConfigSection root, String path) throws Exception {
        Object obj = accessor.get(yamlMap);
        YamlConverter converter;

        if (obj != null) {
            converter = this.getConverter(obj.getClass());

            if (converter != null) {
                if (accessor.isStatic() && !accessor.getAnnotation(Flag.class).map(Flag::preserveStatic).orElse(false))
                    return;

                accessor.getField().set(yamlMap, converter.fromConfig(obj.getClass(), root.get(path), (accessor.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) accessor.getGenericType() : null));
                return;
            }
        }

        converter = this.getConverter(accessor.getType());

        if (accessor.isStatic() && !accessor.getAnnotation(Flag.class).map(Flag::preserveStatic).orElse(false))
            return;

        if (converter != null) {
            accessor.getField().set(yamlMap, converter.fromConfig(accessor.getType(), root.get(path), (accessor.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) accessor.getGenericType() : null));
            return;
        }

        accessor.set(yamlMap, root.get(path));
    }

    public final YamlConverter getConverter(Class<?> type) {
        for (YamlConverter converter : this.customConverters) {
            if (converter.supports(type))
                return converter;
        }

        for (YamlConverter converter : this.converters) {
            if (converter.supports(type))
                return converter;
        }

        return null;
    }

    public final Set<Class<? extends YamlConverter>> getCustomConverters() {
        return Collections.unmodifiableSet(this.customConverterClasses);
    }

    public void toConfig(YamlMap yamlMap, FieldAccessor<?> accessor, ConfigSection root, String path) throws Exception {
        Object obj = accessor.get(yamlMap);
        YamlConverter converter;

        if (obj != null) {
            converter = this.getConverter(obj.getClass());

            if (converter != null) {
                root.set(path, converter.toConfig(obj.getClass(), obj, (accessor.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) accessor.getGenericType() : null));
                return;
            }

            converter = this.getConverter(accessor.getType());

            if (converter != null) {
                root.set(path, converter.toConfig(accessor.getType(), obj, (accessor.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) accessor.getGenericType() : null));
                return;
            }
        }

        root.set(path, obj);
    }

}
