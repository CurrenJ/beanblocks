package com.currenj.beanblocks.item.filter.press;

import com.currenj.beanblocks.BeanBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemPressFilter extends Item {


    public ItemPressFilter(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumPressFilterVariants.byDyeDamage(i).getUnlocalizedName();
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for(EnumPressFilterVariants enumPressFilterVariants : EnumPressFilterVariants.values()) {
                items.add(new ItemStack(this, 1, enumPressFilterVariants.getMetadata()));
            }
        }
    }

    public void registerItemModels() {
        for(EnumPressFilterVariants enumPressFilterVariants : EnumPressFilterVariants.values()) {
            System.out.println("Registering " + enumPressFilterVariants.getUnlocalizedName() + " : " + enumPressFilterVariants.getMetadata());
            BeanBlocks.proxy.registerItemRenderer(this, enumPressFilterVariants.getMetadata(), enumPressFilterVariants.getUnlocalizedName());
        }
    }

}
