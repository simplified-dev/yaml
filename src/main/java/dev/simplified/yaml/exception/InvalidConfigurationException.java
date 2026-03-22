package dev.sbs.api.io.yaml.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown when a YAML configuration file cannot be loaded, saved, or mapped.
 */
public class InvalidConfigurationException extends YamlException {

    /**
     * Constructs a new {@code InvalidConfigurationException} with the specified cause.
     *
     * @param cause the underlying throwable that caused this exception
     */
    public InvalidConfigurationException(@NotNull Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code InvalidConfigurationException} with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidConfigurationException(@NotNull String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidConfigurationException} with the specified cause and detail message.
     *
     * @param cause the underlying throwable that caused this exception
     * @param message the detail message
     */
    public InvalidConfigurationException(@NotNull Throwable cause, @NotNull String message) {
        super(cause, message);
    }

    /**
     * Constructs a new {@code InvalidConfigurationException} with a formatted detail message.
     *
     * @param message the format string
     * @param args the format arguments
     */
    public InvalidConfigurationException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(message, args);
    }

    /**
     * Constructs a new {@code InvalidConfigurationException} with the specified cause and a formatted detail message.
     *
     * @param cause the underlying throwable that caused this exception
     * @param message the format string
     * @param args the format arguments
     */
    public InvalidConfigurationException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(cause, message, args);
    }

}
