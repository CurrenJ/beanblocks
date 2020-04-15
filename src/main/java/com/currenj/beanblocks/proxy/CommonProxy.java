package com.currenj.beanblocks.proxy;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.entity.EntityBeanBase;
import com.currenj.beanblocks.entity.companionbean.EntityCompanionBean;
import com.currenj.beanblocks.entity.EntityWildBean;
import com.currenj.beanblocks.entity.EntityWorkBean;
import com.currenj.beanblocks.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BeanBlocks.modId)
public class CommonProxy {

    private static int entityCount;

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    public String localize(String unlocalized, Object... args) {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().registerAll(
                    build(EntityWildBean.class, "wildbean", true),
                    build(EntityWorkBean.class, "workbean"),
                    build(EntityCompanionBean.class, "companionbean")
            );
            System.out.println("Entries registered");
    }

    protected static EntityEntry build(final Class<? extends EntityBeanBase> entityClass, final String name){
        EntityEntryBuilder builder = EntityEntryBuilder.<EntityBeanBase>create();
        builder.entity(entityClass);
        builder.name(BeanBlocks.modId + "." + name);
        builder.id(new ResourceLocation(BeanBlocks.modId, name), ++entityCount);
        builder.tracker(160, 2, false);
        return builder.build();
    }

    protected static EntityEntry build(final Class<? extends EntityBeanBase> entityClass, final String name, boolean spawn){
        EntityEntryBuilder builder = EntityEntryBuilder.<EntityBeanBase>create();
        builder.entity(entityClass);
        builder.name(BeanBlocks.modId + "." + name);
        builder.id(new ResourceLocation(BeanBlocks.modId, name), ++entityCount);
        builder.tracker(160, 2, false);
        if(spawn)
            builder.spawn(EnumCreatureType.AMBIENT, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection());
        return builder.build();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
        ModBlocks.registerItemBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(ModelRegistryEvent event) {
        ModItems.registerModels();
        ModBlocks.registerModels();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }

    public static void registerEntityRenderingHandlers(){

    }

}
