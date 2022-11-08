package me.youhavetrouble.protectionzones.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PZWand extends ItemStack {

    private static final NamespacedKey wandKey = new NamespacedKey("protectionzones", "wand");

    public PZWand() {
        super(Material.WOODEN_AXE, 1);
        ItemMeta meta = this.getItemMeta();
        meta.getPersistentDataContainer().set(wandKey, PersistentDataType.BYTE, (byte) 1);
        meta.displayName(Component.text("ProtectionZones Wand"));
        this.setItemMeta(meta);
    }

    public static boolean isWand(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().has(wandKey);
    }

}
