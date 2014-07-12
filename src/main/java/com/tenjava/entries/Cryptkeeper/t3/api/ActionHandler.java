package com.tenjava.entries.Cryptkeeper.t3.api;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public interface ActionHandler<T> {

    public void load(ConfigurationSection section);

    public void activate(T target, World world);

    public boolean canActivate(T target, World world);

    public String getSectionName();
}