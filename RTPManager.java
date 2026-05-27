package me.rtp;

import java.util.*;

public class RTPManager {

    private static final Map<UUID, Long> cooldown = new HashMap<>();
    private static final Set<UUID> active = new HashSet<>();

    public static boolean isCooldown(UUID uuid) {
        return cooldown.containsKey(uuid) && cooldown.get(uuid) > System.currentTimeMillis();
    }

    public static int getCooldown(UUID uuid) {
        if (!isCooldown(uuid)) return 0;
        return (int)((cooldown.get(uuid) - System.currentTimeMillis()) / 1000);
    }

    public static void setCooldown(UUID uuid, int seconds) {
        cooldown.put(uuid, System.currentTimeMillis() + (seconds * 1000L));
    }

    public static boolean isActive(UUID uuid) {
        return active.contains(uuid);
    }

    public static void setActive(UUID uuid, boolean value) {
        if (value) active.add(uuid);
        else active.remove(uuid);
    }
}
