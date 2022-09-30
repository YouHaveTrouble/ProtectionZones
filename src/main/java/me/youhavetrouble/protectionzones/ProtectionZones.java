package me.youhavetrouble.protectionzones;

import me.youhavetrouble.protectionzones.exception.ZoneAlreadyRegisteredException;
import me.youhavetrouble.protectionzones.flags.Flag;
import me.youhavetrouble.protectionzones.flags.FlagRegistry;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import me.youhavetrouble.protectionzones.flags.ZoneFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class ProtectionZones extends JavaPlugin {

    private static final HashMap<String, ProtectionZone> protectionZones = new HashMap<>();

    @Override
    public void onEnable() {
        FlagRegistry.registerFlag(Flag.BREAK_BLOCKS.getZoneFlag());
        FlagRegistry.registerFlag(Flag.PLACE_BLOCKS.getZoneFlag());

    }

    public static void registerZone(ProtectionZone protectionZone) {
        if (protectionZones.containsKey(protectionZone.getId())) {
            throw new ZoneAlreadyRegisteredException(String.format("Zone with id %s is already registered", protectionZone.getId()));
        }
        protectionZones.put(protectionZone.getId(), protectionZone);
    }

    public static void unregisterZone(ProtectionZone protectionZone) {
        unregisterZone(protectionZone.getId());
    }

    public static void unregisterZone(String id) {
        protectionZones.remove(id);
    }

    /**
     * Queries the location for all protection zones that contain it
     * @param location Location to query
     * @return Set of all protection zones that contain given location
     */
    public static Set<ProtectionZone> queryProtectionZones(Location location) {
        HashSet<ProtectionZone> zones = new HashSet<>();
        if (location.getWorld() == null) return zones;
        protectionZones.forEach((id, zone) -> {
            if (!location.getWorld().getUID().equals(zone.getWorldUuid())) return;
            if (zone.contains(location)) zones.add(zone);
        });
        return zones;
    }

    public static FlagResult<?> queryEffectiveFlagResult(Location location, ZoneFlag zoneFlag) {
        Set<ProtectionZone> zones = queryProtectionZones(location);
        if (zones.isEmpty()) return FlagResult.empty();
        FlagResult<?> result = FlagResult.empty();
        int highestPriority = 0;
        for (ProtectionZone zone : zones) {
            if (zone.getPriority() < highestPriority) continue;
            highestPriority = zone.getPriority();
            result = zone.getPublicPermission(zoneFlag);
        }
        return result;
    }

    public static FlagResult<?> queryEffectivePlayerFlagResult(Player player, Location location, ZoneFlag zoneFlag) {
        Set<ProtectionZone> zones = queryProtectionZones(location);
        if (zones.isEmpty()) return FlagResult.empty();
        FlagResult<?> permission = FlagResult.empty();
        int highestPriority = 0;
        for (ProtectionZone zone : zones) {
            if (zone.getPriority() < highestPriority) continue;
            highestPriority = zone.getPriority();
            permission = zone.resolvePermission(player, zoneFlag);
        }
        return permission;
    }

}
