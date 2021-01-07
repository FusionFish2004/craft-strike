package io.github.jeremyhu.craftstrike.weapon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponUtil {
    public static boolean checkWeapon(ItemStack is){
        ItemMeta meta = is.getItemMeta();
        if(meta != null && meta.hasLore() && meta.hasCustomModelData()){
            Material material = is.getType();
            if(material == Material.STICK){
                return true;
            }
        }
        return false;
    }

    public static final double getRange(WeaponType type){
        return type.range;
    }

    public static final double getPrimaryDamage(WeaponType type){
        return type.PrimaryDamage;
    }

    public static WeaponType getType(ItemMeta meta){
        if(meta.hasCustomModelData()) {
            switch (meta.getCustomModelData()) {
                case 1:
                    return WeaponType.AK47;
                case 2:
                    return WeaponType.SAWN_OFF;
            }
        }
        return null;
    }

    public static int getAmmo(ItemStack is){
        ItemMeta meta = is.getItemMeta();
        int ammo = Integer.valueOf(meta.getLore().get(0));
        return ammo;
    }

    public static int getTotal(ItemStack is){
        ItemMeta meta = is.getItemMeta();
        int total = Integer.valueOf(meta.getLore().get(1));
        return total;
    }

    public static final double getDamage(WeaponType type,double distance){
        double range = getRange(type);

        if(distance > range){
            return 0D;
        }
        double rate = (range - distance)/range;
        double primaryDamage = getPrimaryDamage(type);
        return primaryDamage * rate;
    }
}
