package com.currenj.beanblocks.block.beanhole;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.BlockCropPinto;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.entity.EntityBeanBase;
import com.currenj.beanblocks.entity.EntityWorkBean;
import com.currenj.beanblocks.item.ItemBeanPinto;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class EntityAIDepositInBeanHole extends EntityAIMoveToBlock {
    /**
     * Villager that is harvesting
     */
    private final EntityWorkBean bean;
    private boolean hasFarmItem;
    /**
     * 0 => harvest, 1 => replant, -1 => none
     */
    private int currentTask;

    public EntityAIDepositInBeanHole(EntityWorkBean beanIn, double speedIn) {
        super(beanIn, speedIn, 16);
        this.bean = beanIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.runDelay <= 0) {

            this.currentTask = -1;
            this.hasFarmItem = bean.isFarmItemInInventory();
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
        super.updateTask();
        bean.getLookHelper().setLookPosition((double) bean.getPosition().getX() + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) this.destinationBlock.getZ() + 0.5D, 10.0F, (float) bean.getVerticalFaceSpeed());

        if (this.getIsAboveDestination()) {
            World world = bean.world;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();


            if (this.currentTask == 0 && block instanceof BlockBeanHole) {

                System.out.println("Work bean! [" + bean.getPosition().toString() + "]");
                ItemStackHandler inventory = ((EntityWorkBean)bean).getWorkBeanInventory();

                for (int i = 0; i < inventory.getSlots(); ++i) {
                    ItemStackHandler beanHoleInventory = ((TileEntityBeanHole)world.getTileEntity(blockpos)).getInventory();
                    ItemStack remaining = inventory.getStackInSlot(i);
                    for(int h = 0; h < inventory.getSlots() && !remaining.isEmpty(); h++){
                        remaining = beanHoleInventory.insertItem(h, remaining, false);
                        inventory.setStackInSlot(i, remaining);
                    }
                }
            }
        }

        this.currentTask = -1;
        this.runDelay = 10;
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block instanceof BlockBeanHole) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            ItemStackHandler inventory = bean.getWorkBeanInventory();
            if(inventory.getStackInSlot(0).getItem() instanceof ItemBeanPinto && inventory.getStackInSlot(0).getCount() > 0) {
                this.currentTask = 0;
                return true;
            }
        }
        return false;
    }
}

