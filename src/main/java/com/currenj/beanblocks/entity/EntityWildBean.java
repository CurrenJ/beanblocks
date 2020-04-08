package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.item.ItemBeanBrandingToolHeated;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
                    EntityWorkBean newWorkBean = new EntityWorkBean(world);
                    newWorkBean.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                    world.spawnEntity(newWorkBean);
                    this.setDead();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected ResourceLocation getTexture() {
        return makeTexture(BeanBlocks.modId, "bean");
    }
}
