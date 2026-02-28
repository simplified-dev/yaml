package dev.sbs.api.io.yaml.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InvalidConverterException extends YamlException {

    public InvalidConverterException(@NotNull Throwable cause) {
        super(cause);
    }

    public InvalidConverterException(@NotNull String message) {
        super(message);
    }

    public InvalidConverterException(@NotNull Throwable cause, @NotNull String message) {
        super(cause, message);
    }

    public InvalidConverterException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(message, args);
    }

    public InvalidConverterException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(cause, message, args);
    }

}
