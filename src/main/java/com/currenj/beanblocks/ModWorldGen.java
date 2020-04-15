package com.currenj.beanblocks;

import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.underbean.WorldProviderUnderbean;
import com.currenj.beanblocks.underbean.WorldTypeUnderbean;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nullable;
import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

    public static final String BEAN_NAME = "underbeanbiome";
    public static final int BEAN_DIM_ID = findFreeDimensionID();
    public static final DimensionType BEAN_DIM_TYPE = DimensionType.register(BEAN_NAME, "_"+ BEAN_NAME, BEAN_DIM_ID, WorldProviderUnderbean.class, true);
    public static final WorldType BEAN_WORLD_TYPE = new WorldTypeUnderbean(); // although instance isn't used, must create the instance to register the WorldType
    //public static final VillagerRegistry.IVillageCreationHandler BEAN_VILLAGE_HANDLER = new VillageHouseUnderbeanCreationHandler();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) { // the overworld
            generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateOre(ModBlocks.blockAncientBeans.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, 4 + random.nextInt(4), 6);
    }

    private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size, int chances) {
        int deltaY = maxY - minY;

        for (int i = 0; i < chances; i++) {
            BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

            WorldGenMinable generator = new WorldGenMinable(ore, size);
            generator.generate(world, random, pos);
        }
    }

    public static final void registerDimensions()
    {
        DimensionManager.registerDimension(BEAN_DIM_ID, BEAN_DIM_TYPE);
    }

    public static void registerWorldGenerators()
    {
        // DEBUG
        System.out.println("Registering world generators");
        //GameRegistry.registerWorldGenerator(new WorldGenUnderbean(), 10);
    }

    @Nullable
    private static Integer findFreeDimensionID()
    {
        for (int i=2; i<Integer.MAX_VALUE; i++)
        {
            if (!DimensionManager.isDimensionRegistered(i))
            {
                // DEBUG
                System.out.println("Found free dimension ID = "+i);
                return i;
            }
        }

        // DEBUG
        System.out.println("ERROR: Could not find free dimension ID");
        return null;
    }
}
