package com.tenjava.cryptkeeper.platforms.generation;

import com.tenjava.cryptkeeper.platforms.Plugin;
import com.tenjava.cryptkeeper.platforms.api.Environment;
import com.tenjava.cryptkeeper.platforms.generation.populators.EntityPopulator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
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
            spawnLocation = new Location(world, cx + 7.5D, targetY + targetHeight + 3, cz + 7.5D);
            spawnLocation.getBlock().getRelative(BlockFace.DOWN).setType(Material.BEDROCK);
            world.setSpawnLocation(cx + 7, targetY + targetHeight + 3, cz + 7);
        }

        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Location center = new Location(world, 8, y, 8);
                    Location current = new Location(world, x, y, z);

                    if (y >= targetY && y <= targetY + targetHeight && random.nextBoolean()) {
                        if (current.distanceSquared(center) <= Math.pow(5 + random.nextInt(4), 2)) {
                            blocks[(x * 16 + z) * 128 + y] = (byte) environment.getMaterials().get(random.nextInt(environment.getMaterials().size())).getId();
                        }
                    }
                }
            }
        }
        return blocks;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> blockPopulators = new ArrayList<>();
        blockPopulators.add(new EntityPopulator());
        return blockPopulators;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return spawnLocation;
    }

    /**
     * Returns a List of all loaded Environments.
     *
     * @return
     */
    public List<Environment> getEnvironments() {
        return environments;
    }

    /**
     * Returns the preferred spawn point since Vanilla handles it in a messy way.
     *
     * @return
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * Returns a random Environment, used for calculating weighted Environments.
     *
     * @return
     */
    private Environment getEnvironment() {
        double chance = 1;
        int runs = 0;
        while (chance > 0) {
            Environment current = environments.get(random.nextInt(environments.size()));
            chance -= current.getChance();
            if (chance <= 0 || runs >= 10) {
                return current;
            }
            runs++;
        }
        return null;
    }
}
