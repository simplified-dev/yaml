package dev.sbs.api.io.yaml;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.collection.mutable.Mutable;
import dev.sbs.api.io.yaml.annotation.Flag;
import dev.sbs.api.io.yaml.converter.YamlConverter;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.accessor.FieldAccessor;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class YamlMap {

    final transient InternalConverter converter = new InternalConverter();

    static ConfigSection convertFromMap(Map<?, ?> config) {
        ConfigSection section = new ConfigSection();
        section.map.putAll(config);
        return section;
    }

    static boolean doSkip(@NotNull FieldAccessor<?> accessor) {
        return accessor.isTransient() || accessor.isFinal() || (accessor.isStatic() && !accessor.getAnnotation(Flag.class).map(Flag::preserveStatic).orElse(false));
    }

    public final void addCustomConverter(Class<? extends YamlConverter> converter) {
        this.converter.addCustomConverter(converter);
    }

    public final Set<Class<? extends YamlConverter>> getCustomConverters() {
        return this.converter.getCustomConverters();
    }

    protected final String getPathMode(@NotNull FieldAccessor<?> accessor) {
        String path = accessor.getName();

        if (this.getClass().isAnnotationPresent(Flag.class)) {
            switch (Objects.requireNonNull(this.getClass().getAnnotation(Flag.class)).mode()) {
                case FIELD_IS_KEY:
                    path = path.replace("_", ".");
                    break;
                case PATH_BY_UNDERSCORE:
                    break;
                case DEFAULT:
                default:
                    if (path.contains("_"))
                        path = path.replace("_", ".");

                    break;
            }
        }

        return path;
    }

    public void loadFromMap(Map<?, ?> section, Class<?> clazz) throws Exception {
        if (!clazz.getSuperclass().equals(YamlMap.class))
            this.loadFromMap(section, clazz.getSuperclass());

        Reflection<?> refClass = Reflection.of(clazz);
        refClass.setProcessingSuperclass(false);

        for (FieldAccessor<?> accessor : refClass.getFields()) {
            if (doSkip(accessor)) continue;
            final Mutable<String> path = Mutable.of(this.getPathMode(accessor));

            accessor.getAnnotation(Flag.class)
                .map(Flag::path)
                .filter(StringUtil::isNotEmpty)
                .ifPresent(path::set);

            this.converter.fromConfig(this, accessor, convertFromMap(section), path.get());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> saveToMap(Class<?> clazz) throws Exception {
        ConcurrentMap<String, Object> returnMap = Concurrent.newMap();

        if (!clazz.getSuperclass().equals(YamlMap.class) && !clazz.getSuperclass().equals(Object.class)) {
            Map<String, Object> map = this.saveToMap(clazz.getSuperclass());
            returnMap.putAll(map);
        }

        Reflection<?> refClass = Reflection.of(clazz);
        refClass.setProcessingSuperclass(false);

        for (FieldAccessor<?> accessor : refClass.getFields()) {
            if (doSkip(accessor)) continue;
            final Mutable<String> path = Mutable.of(this.getPathMode(accessor));

            accessor.getAnnotation(Flag.class)
                .map(Flag::path)
                .filter(StringUtil::isNotEmpty)
                .ifPresent(path::set);

            try {
                returnMap.put(path.get(), accessor.get(this));
            } catch (ReflectionException ignore) { }
        }

        YamlConverter converter = this.converter.getConverter(Map.class);
        assert converter != null;
        return (Map<String, Object>) converter.toConfig(HashMap.class, returnMap, null);
    }

}
