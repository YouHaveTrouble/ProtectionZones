package me.youhavetrouble.protectionzones.command;

import me.youhavetrouble.protectionzones.ProtectionZone;
import me.youhavetrouble.protectionzones.ProtectionZones;
import me.youhavetrouble.protectionzones.item.PZWand;
import me.youhavetrouble.protectionzones.player.PZPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PZCommand implements TabExecutor {

    private final HashMap<String, String> subcommands = new HashMap<>();

    public PZCommand() {
        subcommands.put("wand", "protectionzones.wand");
        subcommands.put("pos1", "protectionzones.create");
        subcommands.put("pos2", "protectionzones.create");
        subcommands.put("create", "protectionzones.create");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }
        switch (args[0]) {
            case "wand" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("This can only be used as a player");
                    return true;
                }
                player.getInventory().addItem(new PZWand());
                player.sendMessage(Component.text("Here you go!"));
            }
            case "pos1" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("This can only be used as a player");
                    return true;
                }
                PZPlayer.getPlayerData(player.getUniqueId()).setLocation1(player.getLocation().toBlockLocation());
                player.sendMessage(Component.text("Set position 1 to " + player.getLocation().toBlockLocation()));
            }
            case "pos2" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("This can only be used as a player");
                    return true;
                }
                PZPlayer.getPlayerData(player.getUniqueId()).setLocation2(player.getLocation().toBlockLocation());
                player.sendMessage(Component.text("Set position 2 to " + player.getLocation().toBlockLocation()));
            }
            case "create" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("This can only be used as a player");
                    return true;
                }
                PZPlayer playerData = PZPlayer.getPlayerData(player.getUniqueId());
                if (playerData.getLocation1() == null || playerData.getLocation2() == null) {
                    player.sendMessage("You need to mark 2 edge points first");
                    return true;
                }
                ProtectionZone zone = new ProtectionZone(UUID.randomUUID().toString(), player.getLocation().getWorld().getUID());
                zone.addAdmin(player.getUniqueId());
                ProtectionZones.registerZone(zone);
                sender.sendMessage("Zone created!");
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
