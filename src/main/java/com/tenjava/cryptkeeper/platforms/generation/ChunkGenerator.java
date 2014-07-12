package com.tenjava.cryptkeeper.platforms.generation;

import com.tenjava.cryptkeeper.platforms.Plugin;
import com.tenjava.cryptkeeper.platforms.api.Environment;
import com.tenjava.cryptkeeper.platforms.generation.populators.HolePopulator;
import com.tenjava.cryptkeeper.platforms.generation.populators.PyramidPopulator;
import com.tenjava.cryptkeeper.platforms.generation.populators.TentPopulator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkGenerator extends org.bukkit.generator.ChunkGenerator {

    private final Random random = new Random();
    private final List<Environment> environments = new ArrayList<>();
    private Location spawnLocation;

    @Override
    public byte[] generate(World world, Random random, int cx, int cz) {
        byte[] blocks = new byte[32768];
        int minHeight = Plugin.getInstance().getConfig().getInt("minHeight");
        int maxHeight = Plugin.getInstance().getConfig().getInt("maxHeight");
        int targetHeight = random.nextInt(maxHeight - minHeight) + minHeight;
        int targetY = random.nextInt(Plugin.getInstance().getConfig().getInt("yVariation")) + Plugin.getInstance().getConfig().getInt("startY");
        Environment environment = getEnvironment();
        if (environment.canSpawn() && spawnLocation == null) {
            spawnLocation = new Location(world, cx + 8.5D, targetY + targetHeight + 1, cz + 8.5D);
        }
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    Material material = Material.AIR;
                    if (y >= targetY && y <= targetY + targetHeight) {
                        material = environment.getMaterials().get(random.nextInt(environment.getMaterials().size()));
                    }
                    blocks[(x * 16 + z) * 128 + y] = (byte) material.getId();
                }
            }
        }
        return blocks;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> list = new ArrayList<>();
        list.add(new TentPopulator());
        list.add(new PyramidPopulator());
        list.add(new HolePopulator());
        return list;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return spawnLocation;
    }

    public List<Environment> getEnvironments() {
        return environments;
    }

    private Environment getEnvironment() {
        double chance = 1;
        while (chance > 0) {
            Environment current = environments.get(random.nextInt(environments.size()));
            chance -= current.getChance();
            if (chance <= 0) {
                return current;
            }
        }
        return null;
    }
}
