package io.github.jeremyhu.craftstrike.particles;

import io.github.jeremyhu.craftstrike.weapon.WeaponType;
import io.github.jeremyhu.craftstrike.weapon.WeaponUtil;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ParticleUtil {
    private static final boolean particle = false;
    public static void draw(Location start, Location end, WeaponType type){

        double range = WeaponUtil.getRange(type);
        World world = start.getWorld();
        //world.spawnParticle(Particle.EXPLOSION_LARGE,end,1,0,0,0,0.1);
        world.playSound(end,"hit",0.5F,1F);
        Vector vector = end.toVector().clone().subtract(start.toVector()).normalize().multiply(0.5F);
        if(particle) {
            for (int i = 0; i < range / 0.5; i++) {
                Vector vecStart = start.toVector().add(vector.clone().multiply(i));
                world.spawnParticle(Particle.END_ROD, vecStart.toLocation(world), 1, 0, 0, 0, 0.05);
            }
        }
    }

    public static void drawDamage(Location location){
        World world = location.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255,0,0),3);
        world.spawnParticle(Particle.REDSTONE,location, 5,0.5,0.5,0.5,0.00001,dustOptions);
    }

    public static void drawHeadshot(LivingEntity entity,Vector direction){
        Location location = entity.getEyeLocation();
        World world = location.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255,0,0),5);
        world.spawnParticle(Particle.REDSTONE,location, 5,0.2,0.2,0.2,0.00001,dustOptions);
        dustOptions = new Particle.DustOptions(Color.ORANGE,5);
        for (int i = 0; i < 8; i++) {
            Vector vecStart = location.toVector().add(direction.clone().multiply(i));
            world.spawnParticle(Particle.REDSTONE,vecStart.toLocation(world), 2,0.3,0.3,0.3,0.001,dustOptions);
        }
        world.playSound(location,"headshot",0.5F,1F);
    }
}
