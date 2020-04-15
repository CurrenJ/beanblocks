package com.currenj.beanblocks.proxy;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.entity.*;
import com.currenj.beanblocks.entity.companionbean.EntityCompanionBean;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

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
        registerEntityRenderer(EntityCompanionBean.class);
        System.out.println("REGISTERED BEAN ENTITIES");
    }

    public static void registerEntityRenderer(final Class<? extends EntityBeanBase> golem) {
        RenderingRegistry.registerEntityRenderingHandler(golem, FACTORY_BEAN);
    }
}
