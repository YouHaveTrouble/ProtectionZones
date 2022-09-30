package me.youhavetrouble.protectionzones.flags;

public enum Flag {

    BREAK_BLOCKS(new BlockBreakFlag("break_blocks")),
    PLACE_BLOCKS(new BlockPlaceFlag("place_blocks")),
    ;

    private final ZoneFlag zoneFlag;
    Flag(ZoneFlag zoneFlag) {
        this.zoneFlag = zoneFlag;
    }

    public ZoneFlag getZoneFlag() {
        return zoneFlag;
    }
}
