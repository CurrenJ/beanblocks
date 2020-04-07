package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.beanhole.EntityAIDepositInBeanHole;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ModItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class EntityWorkBean extends EntityBeanBase {

    public ItemStackHandler inventory = new ItemStackHandler(3);

    public EntityWorkBean(World worldIn) {
        super(worldIn);
        this.setCanPickUpLoot(true);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityWorkBean(world);
    }

    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();

        if (this.canWorkBeanPickupItem(item))
        {
            ItemStack itemstack1 = ItemStack.EMPTY;
            boolean success = false;
            for(int i = 0; i < inventory.getSlots() && !success; i++){
                if(inventory.isItemValid(i, itemstack)){
                    itemstack1 = inventory.insertItem(i, itemstack, false);
                    success = true;
                }
            }

            if (itemstack1.isEmpty())
            {
                itemEntity.setDead();
            }
            else
            {
                itemstack.setCount(itemstack1.getCount());
            }
        }
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(3, new EntityAIHarvestBeans(this, 1.0D, true));
        this.tasks.addTask(4, new EntityAIDepositInBeanHole(this, 1.25D));
    }

    private boolean canWorkBeanPickupItem(Item itemIn)
    {
        return itemIn instanceof ItemBeanPinto;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);

        setCanPickUpLoot(true);
    }

    public ItemStackHandler getWorkBeanInventory(){
        return inventory;
    }

    public boolean isFarmItemInInventory()
    {
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && (itemstack.getItem() instanceof ItemBeanPinto))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    protected ResourceLocation getTexture(){
        return makeTexture(BeanBlocks.modId, "work_bean");
    }
}
