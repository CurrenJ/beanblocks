package com.currenj.beanblocks.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class ItemBeanBrandingToolHeated extends ItemBase {

    private long heatedTime;

    public ItemBeanBrandingToolHeated(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
        this.setMaxDamage(100);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        this.heatedTime = worldIn.getTotalWorldTime();
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("heat", this.heatedTime);
        stack.setTagCompound(compound);
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(stack.getTagCompound() == null || stack.getTagCompound().getLong("heat") == 0) {
            this.heatedTime = worldIn.getTotalWorldTime();
            NBTTagCompound compound = stack.getTagCompound();
            if(compound == null)
                compound = new NBTTagCompound();
            compound.setLong("heat", this.heatedTime);
            stack.setTagCompound(compound);
        }
        this.setDamage(stack, getDamageFromTimeDifference((int)(worldIn.getTotalWorldTime()-heatedTime), stack.getMaxDamage()));
        if(this.getDamage(stack) >= stack.getMaxDamage()-1){
            if(entityIn instanceof EntityPlayer){
                ((EntityPlayer)entityIn).inventory.setInventorySlotContents(itemSlot, getCoolBrand(stack));
            }
        }
    }

    public static int getDamageFromTimeDifference(int timeDifference, int maxDamage){
        int scaled = (int)((timeDifference / 1000.0) * maxDamage);
        return (scaled >= maxDamage) ? maxDamage-1 : scaled;
    }

    public static ItemStack getCoolBrand(ItemStack hotBrand){
        ItemStack cBrand = new ItemStack(ModItems.itemBeanBrandingToolCool, 1);
        cBrand.setTagCompound(hotBrand.getTagCompound());
        return cBrand;
    }
}
