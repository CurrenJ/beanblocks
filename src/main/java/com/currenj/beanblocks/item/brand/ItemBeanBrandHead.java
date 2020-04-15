package com.currenj.beanblocks.item.brand;

import com.currenj.beanblocks.BeanBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBeanBrandHead extends Item {


    public ItemBeanBrandHead(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumBeanBrandHeadVariants.byDyeDamage(i).getUnlocalizedName();
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for(EnumBeanBrandHeadVariants enumBeanBrandHeadVariants : EnumBeanBrandHeadVariants.values()) {
                items.add(new ItemStack(this, 1, enumBeanBrandHeadVariants.getMetadata()));
            }
        }
    }

    public void registerItemModels() {
        for(EnumBeanBrandHeadVariants enumBeanBrandHeadVariants : EnumBeanBrandHeadVariants.values()) {
            System.out.println("Registering " + enumBeanBrandHeadVariants.getUnlocalizedName() + " : " + enumBeanBrandHeadVariants.getMetadata());
            BeanBlocks.proxy.registerItemRenderer(this, enumBeanBrandHeadVariants.getMetadata(), enumBeanBrandHeadVariants.getUnlocalizedName());
        }
    }

}
