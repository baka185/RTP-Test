package me.rtp;

import org.bukkit.plugin.java.JavaPlugin;

public class RTPPlugin extends JavaPlugin {

    private static RTPPlugin instance;

    public static RTPPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getCommand("rtp").setExecutor(new RTPCommand());
        getServer().getPluginManager().registerEvents(new RTPListener(), this);

        getLogger().info("RTP Plugin Enabled");
    }
}
