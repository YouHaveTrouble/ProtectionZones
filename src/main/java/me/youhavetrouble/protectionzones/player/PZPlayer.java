package me.youhavetrouble.protectionzones.player;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class PZPlayer {

    private static final HashMap<UUID, PZPlayer> playerData = new HashMap<>();

    private Location location1, location2;

    protected PZPlayer() {}

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public static PZPlayer getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    protected static void addPlayer(UUID uuid) {
        playerData.put(uuid, new PZPlayer());
    }

    protected static void removePlayer(UUID uuid) {
        playerData.remove(uuid);
    }
}
