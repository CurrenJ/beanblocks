package com.currenj.beanblocks.recipe;

import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.beanBlock, new ItemStack(ModItems.pintoBean, 9), 0.7f);
        GameRegistry.addSmelting(ModItems.itemBeanBrandingToolCool, new ItemStack(ModItems.itemBeanBrandingToolHeated, 1), 0.7f);

        ModItems.pintoBean.initOreDict();
    }
}
