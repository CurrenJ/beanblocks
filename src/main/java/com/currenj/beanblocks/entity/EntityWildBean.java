package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.entity.companionbean.EntityCompanionBean;
import com.currenj.beanblocks.item.ItemBeanBrandingToolHeated;
import com.currenj.beanblocks.item.brand.EnumBeanBrandHeadVariants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class EntityWildBean extends EntityBeanBase {
    public EntityWildBean(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityWildBean(world);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty())
        {
            if (this.isBreedingItem(itemstack) && this.getGrowingAge() == 0 && !this.isInLove())
            {
                this.consumeItemFromStack(player, itemstack);
                this.setInLove(player);
                return true;
            }

            if (this.isChild() && this.isBreedingItem(itemstack))
            {
                this.consumeItemFromStack(player, itemstack);
                this.ageUp((int)((float)(-this.getGrowingAge() / 20) * 0.1F), true);
                return true;
            }

            if(itemstack.getItem() instanceof ItemBeanBrandingToolHeated){
                if(!world.isRemote) {
                    Entity newBean = getBrandedBean(this.world, itemstack, this, player);
                    if(newBean != null) {
                        world.spawnEntity(newBean);
                        this.setDead();
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityBeanBase.AIMeleeAttack());
        this.tasks.addTask(1, new EntityBeanBase.AIPanic());
    }

    public static Entity getBrandedBean(World world, ItemStack brand, EntityWildBean wildBean, EntityPlayer player){
        NBTTagCompound nbt = brand.getTagCompound();
        System.out.println(nbt);
        ItemStackHandler head = new ItemStackHandler(1);
        head.deserializeNBT(nbt.getCompoundTag("head"));
        ItemStackHandler rod = new ItemStackHandler(1);
        rod.deserializeNBT(nbt.getCompoundTag("rod"));
        ItemStackHandler mods = new ItemStackHandler(6);
        mods.deserializeNBT(nbt.getCompoundTag("modifiers"));

        System.out.println(head + " | " + head.getStackInSlot(0) + " | " + head.getStackInSlot(0).getItemDamage());
        System.out.println(EnumBeanBrandHeadVariants.byDyeDamage(head.getStackInSlot(0).getItemDamage()).getUnlocalizedName());
        if(!head.getStackInSlot(0).isEmpty()) {
            EnumBeanBrandHeadVariants headVariant = EnumBeanBrandHeadVariants.byDyeDamage(head.getStackInSlot(0).getItemDamage());
            if (headVariant == EnumBeanBrandHeadVariants.BEAN_FARMER) {
                EntityWorkBean newWorkBean = new EntityWorkBean(world);
                newWorkBean.setPositionAndRotation(wildBean.posX, wildBean.posY, wildBean.posZ, wildBean.rotationYaw, wildBean.rotationPitch);
                return newWorkBean;
            } else if(headVariant == EnumBeanBrandHeadVariants.COMPANION){
                int chestModSlot = getSlotOfItem(mods, new ItemStack(Item.getItemFromBlock(Blocks.CHEST)));
                EntityCompanionBean newCompanionBean = new EntityCompanionBean(world, player, chestModSlot == -1 ? false : true);
                newCompanionBean.setPositionAndRotation(wildBean.posX, wildBean.posY, wildBean.posZ, wildBean.rotationYaw, wildBean.rotationPitch);
                return newCompanionBean;
            } else {
                return null;
            }
        }
        else return null;
    }

    public static int getSlotOfItem(ItemStackHandler inv, ItemStack itemStack){
        itemStack.setCount(1);
        for(int i = 0; i < inv.getSlots(); i++){
            if(!inv.getStackInSlot(i).isEmpty() && inv.isItemValid(i, itemStack))
                return i;
        }
        return -1;
    }

    @Override
    protected ResourceLocation getTexture() {
        return makeTexture(BeanBlocks.modId, "bean");
    }
}
