package com.currenj.beanblocks.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBeanBrandingToolCool extends ItemBase {

    private long heatedTime;

    public ItemBeanBrandingToolCool(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {

    }


    public static ItemStack getHotBrand(ItemStack coolBrand, long totalWorldTime){
        ItemStack hBrand = new ItemStack(ModItems.itemBeanBrandingToolHeated, 1);
        NBTTagCompound compound = coolBrand.getTagCompound();
        compound.setLong("heat", totalWorldTime);
        hBrand.setTagCompound(compound);
        return hBrand;
    }
}
