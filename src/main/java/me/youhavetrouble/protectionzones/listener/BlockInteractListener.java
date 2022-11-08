package me.youhavetrouble.protectionzones.listener;

import me.youhavetrouble.protectionzones.ProtectionZones;
import me.youhavetrouble.protectionzones.flags.Flag;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteractListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        FlagResult<?> result = ProtectionZones.queryEffectivePlayerFlagResult(
                Flag.INTERACT_BLOCKS.getZoneFlag(),
                event.getPlayer(),
                event.getClickedBlock().getLocation()
        );
        if (result == null) return;
        if (FlagResult.isEmpty(result)) return;
        boolean value = (boolean) result.getResult();
        event.setCancelled(!value);
    }

}
