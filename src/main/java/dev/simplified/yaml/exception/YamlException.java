package dev.sbs.api.io.yaml.exception;

import dev.sbs.api.persistence.exception.SessionException;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlException extends SessionException {

    public YamlException(@NotNull Throwable cause) {
        super(cause);
    }

    public YamlException(@NotNull String message) {
        super(message);
    }

    public YamlException(@NotNull Throwable cause, @NotNull String message) {
        super(cause, message);
    }

    public YamlException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(message, args);
    }

    public YamlException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(cause, message, args);
    }

}
