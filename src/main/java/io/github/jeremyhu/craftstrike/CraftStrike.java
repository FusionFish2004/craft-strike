package io.github.jeremyhu.craftstrike;

import io.github.jeremyhu.craftstrike.listeners.PlayerInteractListener;
import io.github.jeremyhu.craftstrike.listeners.PlayerJoinListener;
import io.github.jeremyhu.craftstrike.listeners.PlayerQuitListener;
import io.github.jeremyhu.craftstrike.players.PlayerWeaponRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CraftStrike extends JavaPlugin {

    private int maxCD = 600;  //定义最大武器CD
    public HashMap<Player, PlayerWeaponRunnable> weaponRunnable = new HashMap<Player, PlayerWeaponRunnable>();  //为每个玩家计时的HashMap

    @Override
    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this),this);
    }

    public int getMaxCD() {
        return maxCD;
    }

    public void checkJoin(Player player){
        if(!weaponRunnable.containsKey(player)){
            weaponRunnable.put(player,new PlayerWeaponRunnable(this,player));
        }else{
            return;
        }
    }

    public void checkRemove(Player player){
        if(weaponRunnable.containsKey(player)){
            weaponRunnable.get(player).cancel();
            weaponRunnable.remove(player);
        }else{
            return;
        }
    }
}
