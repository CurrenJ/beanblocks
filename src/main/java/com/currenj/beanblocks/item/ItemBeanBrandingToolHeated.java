package com.currenj.beanblocks.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class ItemBeanBrandingToolHeated extends ItemBase {

    private double heatedTime;

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
        compound.setDouble("heat", this.heatedTime);
        stack.setTagCompound(compound);
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(stack.getTagCompound() == null || stack.getTagCompound().getDouble("heat") == 0) {
            this.heatedTime = worldIn.getTotalWorldTime();
            NBTTagCompound compound = new NBTTagCompound();
            compound.setDouble("heat", this.heatedTime);
            stack.setTagCompound(compound);
        }
        this.setDamage(stack, getDamageFromTimeDifference((int)(worldIn.getTotalWorldTime()-heatedTime), stack.getMaxDamage()));
        if(this.getDamage(stack) >= stack.getMaxDamage()-1){
            if(entityIn instanceof EntityPlayer){
                ((EntityPlayer)entityIn).inventory.setInventorySlotContents(itemSlot, new ItemStack(ModItems.itemBeanBrandingToolCool, 1));
            }
        }
    }

    public static int getDamageFromTimeDifference(int timeDifference, int maxDamage){
        int scaled = (int)((timeDifference / 1000.0) * maxDamage);
        System.out.println(timeDifference + " | " + scaled);
        return (scaled >= maxDamage) ? maxDamage-1 : scaled;
    }
}
