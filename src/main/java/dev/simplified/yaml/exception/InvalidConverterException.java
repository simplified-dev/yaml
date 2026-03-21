package dev.sbs.api.io.yaml.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown when a YAML converter cannot be instantiated or invoked.
 */
public class InvalidConverterException extends YamlException {

    /**
     * Constructs a new {@code InvalidConverterException} with the specified cause.
     *
     * @param cause the underlying throwable that caused this exception
     */
    public InvalidConverterException(@NotNull Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code InvalidConverterException} with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidConverterException(@NotNull String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidConverterException} with the specified cause and detail message.
     *
     * @param cause   the underlying throwable that caused this exception
     * @param message the detail message
     */
    public InvalidConverterException(@NotNull Throwable cause, @NotNull String message) {
        super(cause, message);
    }

    /**
     * Constructs a new {@code InvalidConverterException} with a formatted detail message.
     *
     * @param message the format string
     * @param args    the format arguments
     */
    public InvalidConverterException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(message, args);
    }

    /**
     * Constructs a new {@code InvalidConverterException} with the specified cause and a formatted detail message.
     *
     * @param cause   the underlying throwable that caused this exception
     * @param message the format string
     * @param args    the format arguments
     */
    public InvalidConverterException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(cause, message, args);
    }

}
