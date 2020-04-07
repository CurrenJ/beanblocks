package com.currenj.beanblocks.proxy;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.entity.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ClientProxy extends CommonProxy {

    public static final IRenderFactory<EntityBeanBase> FACTORY_BEAN = RenderBean::new;

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(BeanBlocks.modId + ":" + id, "inventory"));
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }

    public static void registerEntityRenderingHandlers(){
        registerEntityRenderer(EntityWildBean.class);
        registerEntityRenderer(EntityWorkBean.class);
        System.out.println("REGISTERED BEAN ENTITIES");
    }

    public static void registerEntityRenderer(final Class<? extends EntityBeanBase> golem) {
        RenderingRegistry.registerEntityRenderingHandler(golem, FACTORY_BEAN);
    }
}
