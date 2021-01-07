package io.github.jeremyhu.craftstrike.players;

import io.github.jeremyhu.craftstrike.CraftStrike;
import io.github.jeremyhu.craftstrike.sound.SoundUtil;
import io.github.jeremyhu.craftstrike.utils.ActionBarUtil;
import io.github.jeremyhu.craftstrike.weapon.WeaponAction;
import io.github.jeremyhu.craftstrike.weapon.WeaponType;
import io.github.jeremyhu.craftstrike.weapon.WeaponUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.jeremyhu.craftstrike.players.PlayerWeaponImpl.passEvent;

public class PlayerWeaponRunnable extends BukkitRunnable {

    private CraftStrike plugin;
    private Player player;
    private int time = 0;
    private int lastShoot = 0;
    private int reloadTimer = 0;
    private int reloadTime = 0;
    private boolean isReloading = false;
    private List<String> lore = new ArrayList<String>();

    public PlayerWeaponRunnable(CraftStrike plugin, Player player){
        this.plugin = plugin;
        this.player = player;
        this.start();
    }

    public void run(){
        if(time < plugin.getMaxCD()){
            time++;
        }
        if(isReloading){
            ActionBarUtil.sendActionBar(player,"§6§l换弹中...");
            if(reloadTimer < reloadTime) {
                reloadTimer++;
            }else if(reloadTimer == reloadTime){
                player.getInventory().getItemInMainHand().setLore(lore);
                ActionBarUtil.sendActionBar(player,"§a§l换弹完成！");
                reloadTimer = 0;
                isReloading = false;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void start(){
        this.runTaskTimerAsynchronously(plugin,0L,1L);
        plugin.getLogger().info(player.getName() + "'s runnable started.");
    }

    public void stop(){
        plugin.getLogger().info(player.getName() + "'s runnable stopped.");
        this.cancel();
    }

    public void useWeapon(int CD){

        if(isReloading){
            return;
        }

        if(time == 0){
            passEvent(player);
        }else{
            if(time - lastShoot >= CD){
                time = 0;
                lastShoot = 0;
                passEvent(player);
            }
        }
    }

    public void reload(){
        if(WeaponUtil.checkWeapon(player.getInventory().getItemInMainHand()) && !isReloading){
            WeaponType type = WeaponUtil.getType(player.getInventory().getItemInMainHand().getItemMeta());
            int ammo = WeaponUtil.getAmmo(player.getInventory().getItemInMainHand());
            int total = WeaponUtil.getTotal(player.getInventory().getItemInMainHand());
            if(ammo >= type.clip || total == 0){
                return;
            }
            isReloading = true;
            reloadTimer = 0;
            reloadTime = type.reloadTime;
            SoundUtil.playWeaponSound(player, WeaponAction.RELOAD);
            String ln1 = String.valueOf(type.clip);
            String ln2 = String.valueOf(total - type.clip);
            lore = Arrays.asList(ln1,ln2);
        }else{
            return;
        }
    }

    public boolean isReloading() {
        return isReloading;
    }

    public void interruptReload(){
        isReloading = false;
        reloadTimer = 0;
        reloadTime = 0;
    }
}
