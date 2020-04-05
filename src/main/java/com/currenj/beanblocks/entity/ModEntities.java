package com.currenj.beanblocks.entity;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEntities {

    public static void register(IForgeRegistry<EntityEntry> registry) {
        registry.register(EntityEntryBuilder.create().entity(EntityBean.class)
                .id(new ResourceLocation("beanblocks", "bean"), 33).name("Bean").tracker(160, 2, false)
                .egg(0x4c3e30, 0xf0f0f)
                .spawn(EnumCreatureType.AMBIENT, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection()).build());
        System.out.println("Entries registered");
    }
}
