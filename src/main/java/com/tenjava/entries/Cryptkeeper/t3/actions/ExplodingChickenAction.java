package com.tenjava.entries.Cryptkeeper.t3.actions;

import com.tenjava.entries.Cryptkeeper.t3.Plugin;
import com.tenjava.entries.Cryptkeeper.t3.api.ActionHandler;
import com.tenjava.entries.Cryptkeeper.t3.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;

public class ExplodingChickenAction extends ActionHandler<LivingEntity> {

    @Override
    public void register() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.getInstance(), new Runnable() {

            @Override
            public void run() {
                for (LivingEntity entity : Util.getActiveEntities(worlds)) {
                    if (canActivate(entity, entity.getWorld())) {
                        activate(entity, entity.getWorld());
                    }
                }
            }
        }, 40L, 40L);
    }

    @Override
    public void activate(LivingEntity target, World world) {
        System.out.println("Activating: " + getClass().getSimpleName());
        target.damage(target.getHealth());
        target.getWorld().createExplosion(target.getLocation(), random.nextFloat() + 1F, true);
    }

    @Override
    public boolean canActivate(LivingEntity target, World world) {
        if (!super.canActivate(target, world))
            return false;
        if (!(target instanceof Chicken))
            return false;
        return random.nextDouble() <= chance;
    }

    @Override
    public String getSectionName() {
        return "exploding_chicken";
    }
}
