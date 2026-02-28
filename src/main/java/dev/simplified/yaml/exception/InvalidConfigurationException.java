package dev.sbs.api.io.yaml.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InvalidConfigurationException extends YamlException {

    public InvalidConfigurationException(@NotNull Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(@NotNull String message) {
        super(message);
    }

    public InvalidConfigurationException(@NotNull Throwable cause, @NotNull String message) {
        super(cause, message);
    }

    public InvalidConfigurationException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(message, args);
    }

    public InvalidConfigurationException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(cause, message, args);
    }

}
