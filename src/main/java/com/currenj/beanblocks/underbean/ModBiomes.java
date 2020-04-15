package com.currenj.beanblocks.underbean;

/**
 Copyright (C) 2017 by jabelar

 This file is part of jabelar's Minecraft Forge modding examples; as such,
 you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
 */
import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModWorldGen;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

// TODO: Auto-generated Javadoc
@ObjectHolder(BeanBlocks.modId)
public class ModBiomes
{
    // instantiate Biomes
    public final static BiomeUnderbean underbeanbiome = null;

    @Mod.EventBusSubscriber(modid = BeanBlocks.modId)
    public static class RegistrationHandler
    {
        /**
         * Register this mod's {@link Biome}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBiomes(final RegistryEvent.Register<Biome> event)
        {
            final IForgeRegistry<Biome> registry = event.getRegistry();

            System.out.println("Registering biomes");

            registry.register(new BiomeUnderbean().setRegistryName(BeanBlocks.modId, ModWorldGen.BEAN_NAME));
        }
    }

    /**
     * This method should be called during the "init" FML lifecycle
     * because it must happen after object handler injection.
     */
    public static void initBiomeManagerAndDictionary()
    {
        BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(underbeanbiome, 10));
        BiomeManager.addSpawnBiome(underbeanbiome);
        BiomeManager.addStrongholdBiome(underbeanbiome);
        BiomeManager.addVillageBiome(underbeanbiome, true);
        BiomeDictionary.addTypes(underbeanbiome,
                BiomeDictionary.Type.COLD,
                BiomeDictionary.Type.DRY,
                BiomeDictionary.Type.MAGICAL
        );
    }
}
