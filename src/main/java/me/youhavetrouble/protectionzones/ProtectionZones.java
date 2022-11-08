package me.youhavetrouble.protectionzones;

import me.youhavetrouble.protectionzones.command.PZCommand;
import me.youhavetrouble.protectionzones.exception.ZoneAlreadyRegisteredException;
import me.youhavetrouble.protectionzones.flags.Flag;
import me.youhavetrouble.protectionzones.flags.FlagRegistry;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import me.youhavetrouble.protectionzones.flags.ZoneFlag;
import me.youhavetrouble.protectionzones.listener.BlockBreakListener;
import me.youhavetrouble.protectionzones.listener.BlockInteractListener;
import me.youhavetrouble.protectionzones.listener.BlockPlaceListener;
import me.youhavetrouble.protectionzones.player.PlayerListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public final class ProtectionZones extends JavaPlugin {

    private static ProtectionZones instance;
    private static final HashMap<UUID, WorldProtectionZones> protectionZones = new HashMap<>();
    public static boolean debug = true;

    @Override
    public void onEnable() {

        instance = this;

        FlagRegistry.registerFlag(Flag.BREAK_BLOCKS.getZoneFlag());
        FlagRegistry.registerFlag(Flag.PLACE_BLOCKS.getZoneFlag());
        FlagRegistry.registerFlag(Flag.INTERACT_BLOCKS.getZoneFlag());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockInteractListener(), this);

        getCommand("protectionzones").setExecutor(new PZCommand());

    }

    /**
     * Register protection zone
     * @param protectionZone Protection zone to register
     */
    public static void registerZone(ProtectionZone protectionZone) {
        UUID worldUuid = protectionZone.getWorldUuid();
        WorldProtectionZones worldProtectionZones = protectionZones.get(worldUuid);
        if (worldProtectionZones == null) {
            worldProtectionZones = new WorldProtectionZones(worldUuid);
            protectionZones.put(worldUuid, worldProtectionZones);
        }
        if (worldProtectionZones.getProtectionZoneById(protectionZone.getId()) != null) {
            throw new ZoneAlreadyRegisteredException(String.format("Zone with id %s is already registered", protectionZone.getId()));
        }
        worldProtectionZones.registerZone(protectionZone);
    }

    /**
     * Unregister protection zone
     * @param worldUuid UUID of world the region is in
     * @param id Region id to unregister
     */
    public static void unregisterZone(UUID worldUuid, String id) {
        if (worldUuid == null) return;
        if (id == null) return;
        WorldProtectionZones worldProtectionZones = protectionZones.get(worldUuid);
        if (worldProtectionZones == null) return;
        worldProtectionZones.unregisterZone(id);
        if (worldProtectionZones.getProtectionZones().isEmpty()) protectionZones.remove(worldUuid);
    }

    /**
     * Queries the location for all protection zones that contain it
     * @param location Location to query
     * @return Set of all protection zones that contain given location, null if world in location is null
     */
    public Set<ProtectionZone> queryProtectionZones(Location location) {
        if (location.getWorld() == null) return null;
        return queryProtectionZones(location.getWorld().getUID(), location.toVector());
    }

    /**
     * Queries the location for all protection zones that contain it
     * @param worldUuid UUID of world to query
     * @param positionVector Location vector to query
     * @return Set of all protection zones that contain given location, null if worldUuid is null
     */
    public Set<ProtectionZone> queryProtectionZones(UUID worldUuid, Vector positionVector) {
        if (worldUuid == null) return null;
        Set<ProtectionZone> zones = new HashSet<>();
        WorldProtectionZones worldProtectionZones = protectionZones.get(worldUuid);
        if (worldProtectionZones == null) return zones;
        return worldProtectionZones.queryProtectionZones(positionVector);
    }

    /**
     * Gets the result of flag query
     * @param zoneFlag Flag to query
     * @param player Player to query with
     * @param worldUuid UUID of world to query in
     * @param positionVector Vector position to query with
     * @return Result of flag query. Null if world uuid is null
     */
    public static FlagResult<?> queryEffectivePlayerFlagResult(ZoneFlag zoneFlag, Player player, UUID worldUuid, Vector positionVector) {
        if (worldUuid == null) return null;
        WorldProtectionZones worldProtectionZones = protectionZones.get(worldUuid);
        if (worldProtectionZones == null) return FlagResult.empty();
        return worldProtectionZones.queryEffectivePlayerFlagResult(
                zoneFlag,
                player,
                positionVector.getBlockX(),
                positionVector.getBlockY(),
                positionVector.getBlockZ()
        );
    }

    /**
     * Gets the result of flag query
     * @param zoneFlag Flag to query
     * @param player Player to query with
     * @param location location to query with
     * @return Result of flag query. Null if world in location is null
     */
    public static FlagResult<?> queryEffectivePlayerFlagResult(ZoneFlag zoneFlag, Player player, Location location) {
        if (location.getWorld() == null) return null;
        return queryEffectivePlayerFlagResult(zoneFlag, player, location.getWorld().getUID(), location.toVector());
    }

    public static Collection<ProtectionZone> zonesForWorld(UUID worldUid) {
        return protectionZones.get(worldUid).getProtectionZones();
    }

    public static ProtectionZone getZoneById(UUID worldUid, String zoneId) {
        return protectionZones.get(worldUid).getProtectionZoneById(zoneId);
    }

    public static void debugMessage(String message) {
        if (!debug) return;
        instance.getLogger().info(message);
    }
}
