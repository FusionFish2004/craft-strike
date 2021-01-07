package io.github.jeremyhu.craftstrike.players;

import io.github.jeremyhu.craftstrike.sound.SoundUtil;
import io.github.jeremyhu.craftstrike.utils.ActionBarUtil;
import io.github.jeremyhu.craftstrike.weapon.WeaponAction;
import io.github.jeremyhu.craftstrike.weapon.WeaponType;
import io.github.jeremyhu.craftstrike.weapon.WeaponUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.function.Predicate;

import static io.github.jeremyhu.craftstrike.particles.ParticleUtil.*;

public class PlayerWeaponImpl {

    public static void passEvent(final Player player){

        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        WeaponType type = WeaponUtil.getType(meta);
        double range = WeaponUtil.getRange(type);

        if(!WeaponUtil.checkWeapon(is)){
            return;
        }

        List<String> lore = meta.getLore();
        if(lore.get(0) != "INF"){
            String ln1 = lore.get(0);
            String ln2 = lore.get(1);
            if(StringUtils.isNumeric(ln1) && StringUtils.isNumeric(ln2)){
                int clip = Integer.valueOf(ln1);
                int total = Integer.valueOf(ln2);
                int maxClip = type.clip;

                if(clip <= maxClip && clip > 0){
                    clip--;
                    lore.set(0,String.valueOf(clip));
                    is.setLore(lore);
                    ActionBarUtil.sendActionBar(player,"§f§l" + type.name() + " | " + clip + "/" + total);
                }else if(clip == 0){
                    SoundUtil.playWeaponSound(player, WeaponAction.EMPTY);
                    return;
                }

            }
        }


        Vector direction = player.getLocation().getDirection().normalize();
        Predicate<Entity> predicate = new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                return !entity.getUniqueId().equals(player.getUniqueId());
            }
        };
        RayTraceResult rayTrace = player.getWorld().rayTrace(player.getEyeLocation(),direction,range,FluidCollisionMode.NEVER,true,0D,predicate);
        if(rayTrace != null){
            Location hitPos = rayTrace.getHitPosition().toLocation(player.getWorld());
            //player.sendMessage(hitPos.toString());
            if(rayTrace.getHitEntity() != null){

                Entity entity = rayTrace.getHitEntity();
                if(entity instanceof LivingEntity){
                    double distance = entity.getLocation().toVector().distance(player.getEyeLocation().toVector());
                    LivingEntity livingEntity = (LivingEntity)entity;

                    if(livingEntity.getEyeLocation().toVector().distance(rayTrace.getHitPosition()) > 0.5){
                        livingEntity.damage(WeaponUtil.getDamage(type,distance),player);
                        livingEntity.setVelocity(direction.clone().multiply(0.5));
                        drawDamage(rayTrace.getHitPosition().toLocation(entity.getWorld()));
                    }else{
                        livingEntity.damage(500);
                        livingEntity.setVelocity(direction.clone().multiply(2));
                        drawHeadshot(livingEntity,direction);
                        ActionBarUtil.sendActionBar(player,"§c§l爆头");
                    }

                }else{
                    //entity.remove();
                }

            }
            draw(player.getEyeLocation(),hitPos,type);
        }else{
            Vector vector = direction.clone().multiply(range);
            draw(player.getEyeLocation(),player.getEyeLocation().add(vector),type);
        }

        SoundUtil.playWeaponSound(player, WeaponAction.FIRE);
        Vector di = player.getLocation().getDirection().clone();
        Vector vector;
        if(player.isSneaking()) {
            vector = new Vector(0, 0.5, 0).multiply(type.offset);
        }else{
            vector = new Vector(0, 1, 0).multiply(type.offset);
        }
        Vector velocity = player.getVelocity();
        di.add(vector).normalize();
        Location location = player.getLocation();
        location.setDirection(di);
        player.teleport(location);
        player.setVelocity(velocity);
    }
}
