package com.currenj.beanblocks.item.brand;

import com.currenj.beanblocks.BeanBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBeanBrandRod extends Item {


    public ItemBeanBrandRod(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumBeanBrandRodVariants.byDyeDamage(i).getUnlocalizedName();
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for(EnumBeanBrandRodVariants enumBeanBrandRodVariants : EnumBeanBrandRodVariants.values()) {
                items.add(new ItemStack(this, 1, enumBeanBrandRodVariants.getMetadata()));
            }
        }
    }

    public void registerItemModels() {
        for(EnumBeanBrandRodVariants enumBeanBrandRodVariants : EnumBeanBrandRodVariants.values()) {
            System.out.println("Registering " + enumBeanBrandRodVariants.getUnlocalizedName() + " : " + enumBeanBrandRodVariants.getMetadata());
            BeanBlocks.proxy.registerItemRenderer(this, enumBeanBrandRodVariants.getMetadata(), enumBeanBrandRodVariants.getUnlocalizedName());
        }
    }

}
