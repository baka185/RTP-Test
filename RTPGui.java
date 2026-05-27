package me.rtp;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class RTPGui {

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(null, 9, "§8RTP Menu");

        ItemStack dirt = new ItemStack(Material.DIRT);
        ItemMeta meta = dirt.getItemMeta();
        meta.setDisplayName("§aClick to RTP");
        dirt.setItemMeta(meta);

        inv.setItem(4, dirt);

        player.openInventory(inv);
    }
}
