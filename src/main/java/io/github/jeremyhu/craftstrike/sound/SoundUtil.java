package io.github.jeremyhu.craftstrike.sound;

import io.github.jeremyhu.craftstrike.weapon.WeaponAction;
import io.github.jeremyhu.craftstrike.weapon.WeaponType;
import io.github.jeremyhu.craftstrike.weapon.WeaponUtil;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;





public class SoundUtil {
    public static void playWeaponSound(Player player, WeaponAction action){
        Location location = player.getLocation();
        World world = location.getWorld();
        WeaponType type = WeaponUtil.getType(player.getInventory().getItemInMainHand().getItemMeta());
        //player.sendMessage(type.name());

        String sound = getSound(type,action);
        if(sound != null) {
            world.playSound(location, sound, SoundCategory.MASTER, 0.5F, 1F);
        }
    }



    public static String getSound(WeaponType type, WeaponAction action) {
        if (type != null && action != WeaponAction.EMPTY){
            String prefix = "weapon.";
            String soundType = type.name().toLowerCase() + ".";
            String soundAction = action.name().toLowerCase();

            //Bukkit.getLogger().info(prefix + soundType + soundAction);

            return prefix + soundType + soundAction;

        }else if(type != null && action == WeaponAction.EMPTY){
            return "weapon.empty";
        }
        return null;
    }

}
