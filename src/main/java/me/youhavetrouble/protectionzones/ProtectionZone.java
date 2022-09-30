package me.youhavetrouble.protectionzones;

import me.youhavetrouble.protectionzones.flags.FlagResult;
import me.youhavetrouble.protectionzones.flags.ZoneFlag;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class ProtectionZone extends BoundingBox {
    private final String id;
    private int priority = 0;
    private final HashSet<UUID> admins = new HashSet<>();
    private final HashSet<UUID> members = new HashSet<>();
    private final HashMap<String, FlagResult<?>> memberPermissions = new HashMap<>();
    private final HashMap<String, FlagResult<?>> publicPermissions = new HashMap<>();
    private final UUID worldUuid;

    public ProtectionZone(String id, UUID worldUuid) {
        this.id = id;
        this.worldUuid = worldUuid;
    }

    /**
     * Returns if provided block is within the bounding box
     * @return
     * true if provided block is within the bounding box<br>
     * false if block is not within the bounding box or in different world
     */
    public boolean contains(Block block) {
        if (!block.getWorld().getUID().equals(worldUuid)) return false;
        return contains(block.getX(), block.getY(), block.getZ());
    }

    /**
     * Returns if provided location is within the bounding box
     * @return
     * true if provided location is within the bounding box<br>
     * false if location is not within the bounding box or in different world
     */
    public boolean contains(Location location) {
        if (!location.getWorld().getUID().equals(worldUuid)) return false;
        return contains(location.getX(), location.getY(), location.getZ());
    }

    public boolean isMember(OfflinePlayer offlinePlayer) {
        return isMember(offlinePlayer.getUniqueId());
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public boolean isAdmin(UUID uuid) {
        return admins.contains(uuid);
    }

    public void addAdmin(UUID uuid) {
        admins.add(uuid);
    }

    public void removeAdmin(UUID uuid) {
        admins.remove(uuid);
    }

    /**
     * Returns unmodifiable set of all members
     * @return Unmodifiable set of all members
     */
    public Set<UUID> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    /**
     * Returns unmodifiable set of all admins
     * @return Unmodifiable set of all admins
     */
    public Set<UUID> getAdmins() {
        return Collections.unmodifiableSet(admins);
    }

    /**
     * Returns effective permission for provided player for this region
     * @return Effective permission for provided player for this region
     */
    public FlagResult<?> resolvePermission(OfflinePlayer player, ZoneFlag zoneFlag) {
        UUID uuid = player.getUniqueId();
        if (isAdmin(uuid)) return null;
        if (isMember(uuid)) return getMemberPermission(zoneFlag);
        return getPublicPermission(zoneFlag);
    }

    /**
     * Get permission for admins of the zone
     */
    public FlagResult<?> getAdminPermission(ZoneFlag zoneFlag) {
        return memberPermissions.getOrDefault(zoneFlag.getName(), FlagResult.empty());
    }

    /**
     * Get permission for members of the zone
     */
    public FlagResult<?> getMemberPermission(ZoneFlag zoneFlag) {
        return memberPermissions.getOrDefault(zoneFlag.getName(), FlagResult.empty());
    }

    /**
     * Get permission for non-members
     */
    public FlagResult<?> getPublicPermission(ZoneFlag zoneFlag) {
        return publicPermissions.getOrDefault(zoneFlag.getName(), FlagResult.empty());
    }

    public String getId() {
        return id;
    }

    public UUID getWorldUuid() {
        return worldUuid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
