package com.currenj.beanblocks.underbean;


import net.minecraft.world.biome.BiomeProviderSingle;

public class BiomeProviderUnderbean extends BiomeProviderSingle
{

    /**
     * Instantiates a new biome provider cloud.
     */
    public BiomeProviderUnderbean()
    {
        super(ModBiomes.underbeanbiome);

        // DEBUG
        System.out.println("Constructing BiomeProviderCloud");
    }
}
