package com.tenjava.entries.Cryptkeeper.t3.actions;

import com.tenjava.entries.Cryptkeeper.t3.Plugin;
import com.tenjava.entries.Cryptkeeper.t3.api.ActionHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Random;

public class ExplodingChickenAction implements ActionHandler<LivingEntity>, Runnable {

    private final static Random RANDOM = new Random();

    private List<String> worlds;
    private double chance;

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (canActivate(entity, world)) {
                    activate(entity, world);
                }
            }
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        chance = section.getDouble("chance");
        worlds = section.getStringList("worlds");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.getInstance(), this, 40L, 40L);
    }

    @Override
    public void activate(LivingEntity target, World world) {
        target.damage(target.getHealth());
        target.getWorld().createExplosion(target.getLocation(), RANDOM.nextFloat() + 1F, true);
    }

    @Override
    public boolean canActivate(LivingEntity target, World world) {
        if (!worlds.contains(world.getName()))
            return false;
        if (!(target instanceof Chicken))
            return false;
        return RANDOM.nextDouble() <= chance;
    }

    @Override
    public String getSectionName() {
        return "exploding_chicken";
    }

    @Override
    public String toString() {
        return "ExplodingChickenAction{" +
                "chance=" + chance +
                '}';
    }
}
