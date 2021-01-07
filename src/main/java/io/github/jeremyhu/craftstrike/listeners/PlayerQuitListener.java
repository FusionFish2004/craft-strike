package io.github.jeremyhu.craftstrike.listeners;

import io.github.jeremyhu.craftstrike.CraftStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private CraftStrike plugin;

    public PlayerQuitListener(CraftStrike plugin){
        plugin.getLogger().info("Player Quit Listener Registered.");
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        plugin.checkRemove(player);
    }
}
