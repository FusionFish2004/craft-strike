package io.github.jeremyhu.craftstrike.listeners;

import io.github.jeremyhu.craftstrike.CraftStrike;
import io.github.jeremyhu.craftstrike.sound.SoundUtil;
import io.github.jeremyhu.craftstrike.weapon.WeaponAction;
import io.github.jeremyhu.craftstrike.weapon.WeaponType;
import io.github.jeremyhu.craftstrike.weapon.WeaponUtil;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;


public class PlayerInteractListener implements Listener {

    private CraftStrike plugin;

    public PlayerInteractListener(CraftStrike plugin){
        plugin.getLogger().info("Player Interact Listener Registered.");
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(WeaponUtil.checkWeapon(player.getInventory().getItemInMainHand())) {
            WeaponType type = WeaponUtil.getType(player.getInventory().getItemInMainHand().getItemMeta());

            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                plugin.weaponRunnable.get(player).useWeapon(type.CD);
                event.setCancelled(true);
            }

            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
                if(player.isSneaking()){
                    plugin.weaponRunnable.get(player).reload();
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        ItemStack is = new ItemStack(Material.STICK,1);
        ItemMeta meta = is.getItemMeta();
        meta.setLore(Arrays.asList(String.valueOf(WeaponType.AK47.clip),String.valueOf((WeaponType.AK47.clipNum-1) * WeaponType.AK47.clip)));
        meta.setCustomModelData(1);
        meta.setDisplayName("§f§lAK47");
        is.setItemMeta(meta);
        player.getInventory().addItem(is);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();


        if(WeaponUtil.checkWeapon(player.getInventory().getItemInMainHand())){
            plugin.weaponRunnable.get(player).useWeapon(2);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        plugin.weaponRunnable.get(player).interruptReload();
        if(WeaponUtil.checkWeapon(player.getInventory().getItem(event.getNewSlot()))){
            ItemStack is = player.getInventory().getItem(event.getNewSlot());
            WeaponType type = WeaponUtil.getType(is.getItemMeta());
            player.getWorld().playSound(player.getLocation(), SoundUtil.getSound(type,WeaponAction.SWITCH), SoundCategory.MASTER, 0.5F, 1F);
        }
    }
}
