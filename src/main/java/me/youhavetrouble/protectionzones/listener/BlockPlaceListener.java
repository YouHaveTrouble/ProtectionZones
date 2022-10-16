package me.youhavetrouble.protectionzones.listener;

import me.youhavetrouble.protectionzones.ProtectionZones;
import me.youhavetrouble.protectionzones.flags.Flag;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockPlaceEvent event) {
        FlagResult<?> result = ProtectionZones.queryEffectivePlayerFlagResult(
                Flag.PLACE_BLOCKS.getZoneFlag(),
                event.getPlayer(),
                event.getBlock().getLocation()
        );
        if (result == null) return;
        if (FlagResult.isEmpty(result)) return;
        boolean value = (boolean) result.getResult();
        event.setCancelled(!value);
    }

}
