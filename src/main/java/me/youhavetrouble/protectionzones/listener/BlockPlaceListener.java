package me.youhavetrouble.protectionzones.listener;

import me.youhavetrouble.protectionzones.ProtectionZones;
import me.youhavetrouble.protectionzones.flags.Flag;
import me.youhavetrouble.protectionzones.flags.FlagResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockPlaceEvent event) {
        System.out.println("Placed block");
        FlagResult<?> result = ProtectionZones.queryEffectivePlayerFlagResult(
                Flag.PLACE_BLOCKS.getZoneFlag(),
                event.getPlayer(),
                event.getBlock().getLocation()
        );
        if (result == null) return;
        System.out.println("result not null");
        if (FlagResult.isEmpty(result)) return;
        System.out.println("Result not empty");
        boolean value = (boolean) result.getResult();
        System.out.println("Result: "+value);
        event.setCancelled(!value);
    }

}
