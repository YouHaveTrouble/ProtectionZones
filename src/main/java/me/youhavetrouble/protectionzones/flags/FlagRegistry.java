package me.youhavetrouble.protectionzones.flags;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class FlagRegistry {

    private static final HashMap<String, ZoneFlag> registeredFlags = new HashMap<>();

    public static void registerFlag(ZoneFlag zoneFlag) {
        registeredFlags.put(zoneFlag.getName(), zoneFlag);
    }

    public static ZoneFlag getZoneFlag(String name) {
        return registeredFlags.get(name);
    }

    /**
     *
     * @return Unmodifiable collection of all registered flags
     */
    public static Collection<ZoneFlag> getRegisteredFlags() {
        return Collections.unmodifiableCollection(registeredFlags.values());
    }
}
