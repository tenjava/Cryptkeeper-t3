package com.tenjava.entries.Cryptkeeper.t3.environments;

import com.tenjava.entries.Cryptkeeper.t3.api.Environment;
import org.bukkit.Material;

public class StandardEnvironment extends Environment {

    @Override
    public Material getMaterial(int y) {
        if (y <= 2) {
            return Material.DIRT;
        }
        if (y <= 3) {
            return Material.GRASS;
        }
        return Material.AIR;
    }

    @Override
    public double getChance() {
        return 0.35;
    }
}