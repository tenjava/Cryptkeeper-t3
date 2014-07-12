package com.tenjava.entries.Cryptkeeper.t3.api;

import com.tenjava.entries.Cryptkeeper.t3.Plugin;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {

    protected final Random random = new Random();
    protected final ConfigurationSection section;
    protected final List<Material> materials = new ArrayList<>();
    protected final List<EntityType> entities = new ArrayList<>();

    public Environment(ConfigurationSection section) {
        this.section = section;
        for (String name : section.getStringList("materials")) {
            try {
                materials.add(Material.valueOf(name));
            } catch (IllegalArgumentException ex) {
                Plugin.getInstance().getLogger().warning("Unknown Material: " + name);
            }
        }
        for (String name : section.getStringList("entities")) {
            try {
                entities.add(EntityType.valueOf(name));
            } catch (IllegalArgumentException ex) {
                Plugin.getInstance().getLogger().warning("Unknown EntityType: " + name);
            }
        }
    }

    public Material getMaterial() {
        return materials.get(random.nextInt(materials.size()));
    }

    public boolean canSpawn() {
        return section.getBoolean("canSpawn", true);
    }

    public double getChance() {
        return section.getDouble("chance");
    }

    public List<EntityType> getEntities() {
        return entities;
    }
}
