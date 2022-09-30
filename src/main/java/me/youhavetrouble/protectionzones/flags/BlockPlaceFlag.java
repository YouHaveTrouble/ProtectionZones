package me.youhavetrouble.protectionzones.flags;

@SuppressWarnings("unchecked")
public class BlockPlaceFlag extends ZoneFlag {

    public BlockPlaceFlag(String id) {
        super(id);
    }

    @Override
    public FlagResult<Boolean> getPublicFlagResult() {
        return (FlagResult<Boolean>) FlagResult.newResult(false);
    }

    @Override
    public FlagResult<Boolean> getMemberFlagResult() {
        return (FlagResult<Boolean>) FlagResult.newResult(true);
    }

    @Override
    public FlagResult<Boolean> getAdminFlagResult() {
        return (FlagResult<Boolean>) FlagResult.newResult(true);
    }

}
