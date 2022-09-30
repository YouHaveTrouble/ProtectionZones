package me.youhavetrouble.protectionzones.flags;

import java.util.regex.Pattern;

public abstract class ZoneFlag {
    private static final Pattern VALID_NAME = Pattern.compile("^[:_A-Za-z0-9\\-]{1,64}$");
    private final String name;

    public ZoneFlag(String name) {
        if (!isValidName(name)) throw new IllegalArgumentException("Invalid flag name");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FlagResult<?> getPublicFlagResult() {
        return FlagResult.empty();
    }

    public FlagResult<?> getMemberFlagResult() {
        return FlagResult.empty();
    }

    public FlagResult<?> getAdminFlagResult() {
        return FlagResult.empty();
    }

    public static boolean isValidName(String name) {
        if (name == null) return false;
        return VALID_NAME.matcher(name).matches();
    }

}
