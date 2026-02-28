package dev.sbs.api.io.yaml.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {

    @NotNull String comment() default "";

    @NotNull String path() default "";

    /**
     * Sets whether to load/save static fields.
     */
    boolean preserveStatic() default false;

    /**
     * Sets whether this field should be censored when publicly rendered.
     */
    boolean secure() default false;

    @NotNull Type mode() default Type.DEFAULT;

    enum Type {

        DEFAULT,
        FIELD_IS_KEY,
        PATH_BY_UNDERSCORE

    }

}
