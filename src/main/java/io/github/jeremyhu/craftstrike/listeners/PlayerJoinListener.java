package io.github.jeremyhu.craftstrike.listeners;

import io.github.jeremyhu.craftstrike.CraftStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private CraftStrike plugin;

    public PlayerJoinListener(CraftStrike plugin){
        plugin.getLogger().info("Player Join Listener Registered.");
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.checkJoin(player);
    }
}
