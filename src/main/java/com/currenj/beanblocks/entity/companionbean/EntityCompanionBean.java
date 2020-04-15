package com.currenj.beanblocks.entity.companionbean;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.entity.EntityAIFollowPlayer;
import com.currenj.beanblocks.entity.EntityBeanBase;
import com.currenj.beanblocks.entity.EntityWildBean;
import com.currenj.beanblocks.item.ItemBeanBrandingToolHeated;
import com.currenj.beanblocks.item.brand.EnumBeanBrandHeadVariants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityCompanionBean extends EntityBeanBase {
    UUID linkedPlayerUUID;
    protected ItemStackHandler inventory = new ItemStackHandler(9);
    public boolean hasInventory;
    public boolean lockLocation = false;

    public EntityCompanionBean(World worldIn, EntityPlayer player, boolean hasInventory) {
        super(worldIn);
        this.hasInventory = hasInventory;
        this.linkedPlayerUUID = player.getUniqueID();
        System.out.println(linkedPlayerUUID.toString());
    }

    public EntityCompanionBean(World worldIn) {
        super(worldIn);
        linkedPlayerUUID = null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setUniqueId("linkedPlayerUUID", linkedPlayerUUID);
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setBoolean("hasInventory", hasInventory);
        System.out.println("Saving NBT: " + inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.linkedPlayerUUID = compound.getUniqueId("linkedPlayerUUID");
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.hasInventory = compound.getBoolean("hasInventory");
        System.out.println("Reading NBT: " + compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    public void updateAndPrint(){
        System.out.println(inventory.serializeNBT());
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

        if(lockLocation)
            return false;
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
        } else {
            if(this.hasInventory) {
                player.openGui(BeanBlocks.instance, ModGuiHandler.COMPANION_BEAN, world, this.getEntityId(), 0, 0);
                this.setVelocity(0, 0, 0);
                this.lockLocation = true;
            }
        }
        return false;
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        System.out.println(this.world.getWorldInfo().getWorldName());
        this.tasks.addTask(3, new EntityAIFollowPlayer(this, 2D, 1F, 25F));
    }

    @Override
    protected ResourceLocation getTexture() {
        return makeTexture(BeanBlocks.modId, "companion_bean");
    }

    public UUID getLinkedPlayerUUID(){
        return linkedPlayerUUID;
    }
}
