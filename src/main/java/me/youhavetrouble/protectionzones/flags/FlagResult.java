package me.youhavetrouble.protectionzones.flags;

import org.jetbrains.annotations.NotNull;

public class FlagResult<T> {
    private static final FlagResult<Object> emptyResult = new FlagResult<>(null);
    private final T result;

    private FlagResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public static boolean isEmpty(@NotNull FlagResult<?> flagResult) {
        return flagResult.getResult() == null;
    }

    public static FlagResult<Object> empty() {
        return emptyResult;
    }

    public static FlagResult<?> newResult(Object object) {
        return new FlagResult<>(object);
    }

}
