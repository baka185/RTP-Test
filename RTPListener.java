package me.rtp;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RTPListener implements Listener {

    private static final int BORDER = 2500;
    private static final Random random = new Random();

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;

        if (!e.getView().getTitle().equals("§8RTP Menu")) return;

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() != Material.DIRT) return;

        if (RTPManager.isCooldown(player.getUniqueId())) {
            player.sendMessage("§cCooldown: " + RTPManager.getCooldown(player.getUniqueId()) + "s");
            return;
        }

        player.closeInventory();
        startCountdown(player);
    }

    private void startCountdown(Player player) {

        RTPManager.setActive(player.getUniqueId(), true);

        Location startLoc = player.getLocation().clone();

        new BukkitRunnable() {

            int time = 3;

            @Override
            public void run() {

                if (!player.isOnline()) {
                    cancel();
                    RTPManager.setActive(player.getUniqueId(), false);
                    return;
                }

                if (player.getLocation().distance(startLoc) > 0.3) {
                    player.sendMessage("§cRTP cancelled (you moved)");
                    RTPManager.setActive(player.getUniqueId(), false);
                    cancel();
                    return;
                }

                if (time == 0) {
                    cancel();
                    RTPManager.setActive(player.getUniqueId(), false);
                    teleport(player);
                    return;
                }

                player.sendTitle("§aRTP", "§eTeleporting in §6" + time, 0, 20, 0);
                player.sendActionBar(Component.text("§7RTP in §a" + time + "s"));

                player.playSound(player.getLocation(),
                        Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);

                time--;
            }

        }.runTaskTimer(RTPPlugin.getInstance(), 0L, 20L);
    }

    private void teleport(Player player) {

        Location loc = findSafeLocation(player.getWorld());

        if (loc == null) {
            player.sendMessage("§cNo safe location found.");
            return;
        }

        player.teleport(loc);

        player.getWorld().spawnParticle(Particle.PORTAL, loc, 80);
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        RTPManager.setCooldown(player.getUniqueId(), 60);

        player.sendMessage("§aTeleported!");
    }

    private Location findSafeLocation(World world) {

        for (int i = 0; i < 40; i++) {

            int x = random.nextInt(BORDER * 2) - BORDER;
            int z = random.nextInt(BORDER * 2) - BORDER;

            int y = world.getHighestBlockYAt(x, z);

            Block ground = world.getBlockAt(x, y - 1, z);
            Block feet = world.getBlockAt(x, y, z);
            Block head = world.getBlockAt(x, y + 1, z);

            if (ground.getType().isSolid()
                    && feet.getType().isAir()
                    && head.getType().isAir()
                    && ground.getType() != Material.LAVA
                    && ground.getType() != Material.MAGMA_BLOCK) {

                return new Location(world, x + 0.5, y, z + 0.5);
            }
        }

        return null;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (!RTPManager.isActive(p.getUniqueId())) return;

        if (e.getFrom().distance(e.getTo()) > 0.1) {
            p.sendMessage("§cYou moved! RTP cancelled.");
            RTPManager.setActive(p.getUniqueId(), false);
        }
    }
}
