package com.currenj.beanblocks.item.filter.recycler;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.item.filter.press.EnumPressFilterVariants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemRecyclerFilter extends Item {


    public ItemRecyclerFilter(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumRecyclerFilterVariants.byDyeDamage(i).getUnlocalizedName();
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for(EnumRecyclerFilterVariants enumRecyclerFilterVariants : EnumRecyclerFilterVariants.values()) {
                items.add(new ItemStack(this, 1, enumRecyclerFilterVariants.getMetadata()));
            }
        }
    }

    public void registerItemModels() {
        for(EnumRecyclerFilterVariants enumRecyclerFilterVariants : EnumRecyclerFilterVariants.values()) {
            System.out.println("Registering " + enumRecyclerFilterVariants.getUnlocalizedName() + " : " + enumRecyclerFilterVariants.getMetadata());
            BeanBlocks.proxy.registerItemRenderer(this, enumRecyclerFilterVariants.getMetadata(), enumRecyclerFilterVariants.getUnlocalizedName());
        }
    }

}
