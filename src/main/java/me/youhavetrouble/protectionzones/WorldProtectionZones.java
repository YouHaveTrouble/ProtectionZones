package me.youhavetrouble.protectionzones;

import me.youhavetrouble.protectionzones.exception.ZoneAlreadyRegisteredException;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import me.youhavetrouble.protectionzones.flags.ZoneFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class WorldProtectionZones {

    private final UUID worldUuid;
    private final HashMap<String, ProtectionZone> protectionZones = new HashMap<>();

    public WorldProtectionZones(UUID worldUuid) {
        this.worldUuid = worldUuid;
    }

    protected void registerZone(ProtectionZone protectionZone) {
        if (protectionZones.containsKey(protectionZone.getId())) {
            throw new ZoneAlreadyRegisteredException(
                    String.format(
                            "Zone with id %s is already registered in world %s",
                            protectionZone.getId(),
                            protectionZone.getWorldUuid()
                    )
            );
        }
        protectionZones.put(protectionZone.getId(), protectionZone);
    }

    protected void unregisterZone(String id) {
        protectionZones.remove(id);
    }

    /**
     * Queries the location for all protection zones that contain it
     * @param location Location to query
     * @return Set of all protection zones that contain given location
     */
    public Set<ProtectionZone> queryProtectionZones(Location location) {
        HashSet<ProtectionZone> zones = new HashSet<>();
        if (location.getWorld() == null) return zones;
        protectionZones.forEach((id, zone) -> {
            if (!location.getWorld().getUID().equals(zone.getWorldUuid())) return;
            if (zone.contains(location)) zones.add(zone);
        });
        return zones;
    }

    /**
     * Queries the location for all protection zones that contain it
     * @param vector Vector location to query
     * @return Set of all protection zones that contain given location
     */
    public Set<ProtectionZone> queryProtectionZones(Vector vector) {
        return queryProtectionZones(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    /**
     * Queries the location for all protection zones that contain it
     * @return Set of all protection zones that contain given location
     */
    public Set<ProtectionZone> queryProtectionZones(int x, int y, int z) {
        HashSet<ProtectionZone> zones = new HashSet<>();
        protectionZones.forEach((id, zone) -> {
            if (zone.contains(x, y, z)) zones.add(zone);
        });
        return zones;
    }

    /**
     * Performs flag query on the coordinates
     * @param zoneFlag flag to check
     * @return Result of flag query.
     */
    public FlagResult<?> queryEffectiveFlagResult(ZoneFlag zoneFlag, int x, int y, int z) {
        Set<ProtectionZone> zones = queryProtectionZones(x, y, z);
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

    /**
     * Performs flag query on the coordinates
     * @param zoneFlag flag to check
     * @param player player to check with
     * @return Result of flag query.
     */
    public FlagResult<?> queryEffectivePlayerFlagResult(ZoneFlag zoneFlag, Player player, int x, int y, int z) {
        Set<ProtectionZone> zones = queryProtectionZones(x, y, z);
        System.out.println("Zones query result");
        zones.forEach(zone -> System.out.println(zone.getId()));
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

    /**
     * Gets unmodifiable collection of registered protection zones in the world
     * @return unmodifiable collection of registered protection zones in the world
     */
    public Collection<ProtectionZone> getProtectionZones() {
        return Collections.unmodifiableCollection(this.protectionZones.values());
    }

    /**
     * Gets protection zone
     * @return protection zone if id exists, null if not
     */
    public ProtectionZone getProtectionZoneById(String id) {
        return protectionZones.get(id);
    }

}
