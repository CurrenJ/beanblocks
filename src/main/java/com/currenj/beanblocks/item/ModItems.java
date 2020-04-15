package com.currenj.beanblocks.item;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.item.brand.ItemBeanBrandHead;
import com.currenj.beanblocks.item.brand.ItemBeanBrandRod;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import com.currenj.beanblocks.item.filter.recycler.ItemRecyclerFilter;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemBeansCompressed compressedBeans = new ItemBeansCompressed("beans_compressed");
    public static ItemBeanPinto pintoBean = new ItemBeanPinto(1, 0.1F, ModBlocks.blockCropPinto, "bean");
    public static ItemPressFilter pressFilter = new ItemPressFilter("press_filter");
    public static ItemDenseBeanBar denseBeanBar = new ItemDenseBeanBar("dense_bean_bar");
    public static ItemRecyclerFilter recyclerFilter = new ItemRecyclerFilter("recycler_filter");
    public static ItemFossilizedBeans itemFossilizedBeans = new ItemFossilizedBeans("fossilized_beans");
    public static ItemBeanBrandingToolHeated itemBeanBrandingToolHeated = new ItemBeanBrandingToolHeated("bean_brand_hot");
    public static ItemBeanBrandingToolCool itemBeanBrandingToolCool = new ItemBeanBrandingToolCool("bean_brand_cool");
    public static ItemGlowbeanDust itemGlowbeanDust = new ItemGlowbeanDust("glowbean_dust");
    public static ItemBeanBrandHead itemBeanBrandHead = new ItemBeanBrandHead("brand_head");
    public static ItemBeanBrandRod itemBeanBrandRod = new ItemBeanBrandRod("brand_rod");

    public static ItemArmor beanHelmet = new ItemArmor(BeanBlocks.beanArmor, EntityEquipmentSlot.HEAD, "bean_helmet");
    public static ItemArmor beanChestplate = new ItemArmor(BeanBlocks.beanArmor, EntityEquipmentSlot.CHEST, "bean_chestplate");
    public static ItemArmor beanLeggings = new ItemArmor(BeanBlocks.beanArmor, EntityEquipmentSlot.LEGS, "bean_leggings");
    public static ItemArmor beanBoots = new ItemArmor(BeanBlocks.beanArmor, EntityEquipmentSlot.FEET, "bean_boots");

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                compressedBeans,
                pintoBean,
                pressFilter,
                denseBeanBar,
                recyclerFilter,
                beanHelmet,
                beanChestplate,
                beanLeggings,
                beanBoots,
                itemFossilizedBeans,
                itemBeanBrandingToolHeated,
                itemBeanBrandingToolCool,
                itemGlowbeanDust,
                itemBeanBrandHead,
                itemBeanBrandRod
        );
    }

    public static void registerModels() {
        compressedBeans.registerItemModel();
        pintoBean.registerItemModel();
        pressFilter.registerItemModels();
        denseBeanBar.registerItemModel();
        recyclerFilter.registerItemModels();
        itemFossilizedBeans.registerItemModel();
        itemBeanBrandingToolHeated.registerItemModel();
        itemBeanBrandingToolCool.registerItemModel();
        itemGlowbeanDust.registerItemModel();
        itemBeanBrandHead.registerItemModels();
        itemBeanBrandRod.registerItemModels();

        beanHelmet.registerItemModel();
        beanChestplate.registerItemModel();
        beanLeggings.registerItemModel();
        beanBoots.registerItemModel();
    }

}
