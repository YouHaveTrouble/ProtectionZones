package me.youhavetrouble.protectionzones.listener;

import me.youhavetrouble.protectionzones.ProtectionZones;
import me.youhavetrouble.protectionzones.flags.Flag;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        FlagResult<?> result = ProtectionZones.queryEffectivePlayerFlagResult(
                Flag.BREAK_BLOCKS.getZoneFlag(),
                event.getPlayer(),
                event.getBlock().getLocation()
        );
        if (result == null) return;
        if (FlagResult.isEmpty(result)) return;
        boolean value = (boolean) result.getResult();
        event.setCancelled(!value);
    }

}
