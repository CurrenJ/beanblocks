package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.block.BlockCropPinto;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.item.ItemBeanPinto;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class EntityAIHarvestBeans extends EntityAIMoveToBlock {
    /**
     * Villager that is harvesting
     */
    private final EntityBeanBase bean;
    private boolean plantAndCollect;
    private boolean hasFarmItem;
    private boolean wantsToReapStuff;
    /**
     * 0 => harvest, 1 => replant, -1 => none
     */
    private int currentTask;

    public EntityAIHarvestBeans(EntityBeanBase beanIn, double speedIn, boolean plantAndCollect) {
        super(beanIn, speedIn, 16);
        this.bean = beanIn;
        this.plantAndCollect = plantAndCollect;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(bean.world, bean)) {
                return false;
            }

            this.currentTask = -1;
            if(plantAndCollect)
                this.hasFarmItem = ((EntityWorkBean)bean).isFarmItemInInventory();
            else this.hasFarmItem = false;
            this.wantsToReapStuff = true; //this.villager.wantsMoreFood();
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
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (plantAndCollect) {
                System.out.println("Work bean! [" + bean.getPosition().toString() + "]");
            }

            if (this.currentTask == 0 && block instanceof BlockCropPinto && ((BlockCropPinto) block).isMaxAge(iblockstate)) {
                world.destroyBlock(blockpos, true);
            } else if (this.currentTask == 1 && iblockstate.getMaterial() == Material.AIR) {
                //BEAN NEEDS INVENTORY FOR THIS TO BE ENABLED
                if (plantAndCollect) {
                    System.out.println("Work bean! [" + bean.getPosition().toString() + "]");
                    ItemStackHandler inventory = ((EntityWorkBean)bean).getWorkBeanInventory();

                    for (int i = 0; i < inventory.getSlots(); ++i) {
                        ItemStack itemstack = inventory.getStackInSlot(i);
                        boolean flag = false;

                        if (itemstack.getItem() instanceof ItemBeanPinto) {
                            world.setBlockState(blockpos, ModBlocks.blockCropPinto.getDefaultState(), 3);
                            flag = true;
                        }

                        //If we want it to harvest all kinds of crops
//                        if (!itemstack.isEmpty()) {
//                            if (itemstack.getItem() instanceof net.minecraftforge.common.IPlantable) {
//                                if (((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlantType(world, blockpos) == net.minecraftforge.common.EnumPlantType.Crop) {
//                                    world.setBlockState(blockpos, ((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlant(world, blockpos), 3);
//                                    flag = true;
//                                }
//                            }
//                        }

                        if (flag) {
                            itemstack.shrink(1);

                            if (itemstack.isEmpty()) {
                                inventory.setStackInSlot(i, ItemStack.EMPTY);
                            }

                            break;
                        }
                    }
                }
            }

            this.currentTask = -1;
            this.runDelay = 10;
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCropPinto && ((BlockCropPinto) block).isMaxAge(iblockstate) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0)) {
                this.currentTask = 0;
                return true;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0)) {
                this.currentTask = 1;
                return true;
            }
        }
        return false;
    }
}
