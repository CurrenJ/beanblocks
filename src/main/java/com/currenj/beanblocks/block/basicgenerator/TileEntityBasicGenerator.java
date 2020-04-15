package com.currenj.beanblocks.block.basicgenerator;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucketFull;
import com.currenj.beanblocks.entity.EntityBeanBase;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemBeansCompressed;
import com.currenj.beanblocks.item.ItemDenseBeanBar;
import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TileEntityBasicGenerator extends TileEntity implements ITickable, IEnergyStorage {

    private ItemStackHandler input = new ItemStackHandler(1);
    private boolean generating;
    private int energyStoredRF;
    public static int TRANSFER_RATE = 10;
    public static int GENERATION_RATE = 2;
    private EntityBeanBase runnerBean;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("generating", generating);
        compound.setTag("input", input.serializeNBT());
        compound.setInteger("energy", this.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN).getEnergyStored());
        if(runnerBean != null)
            compound.setUniqueId("bean", runnerBean.getUniqueID());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.generating = compound.getBoolean("generating");
        input.deserializeNBT(compound.getCompoundTag("inventory"));
        this.energyStoredRF = compound.getInteger("energy");
        if(compound.hasKey("bean"))
            runnerBean = this.findBean(compound.getUniqueId("bean"));
        super.readFromNBT(compound);
    }

    private EntityBeanBase findBean(UUID uuid){
        List<EntityBeanBase> entities = world.getEntitiesWithinAABB(EntityBeanBase.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1));
        if(entities == null)
            return null;
        for(EntityBeanBase b : entities){
            if(b.getUniqueID().equals(uuid))
                return b;
        }
        return null;
    }

    private EntityBeanBase findBean(){
        List<EntityBeanBase> entities = world.getEntitiesWithinAABB(EntityBeanBase.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1));
        for(EntityBeanBase b : entities){
           return b;
        }
        return null;
    }

    public boolean releaseBean(){
        if(runnerBean != null) {
            runnerBean.setNoAI(false);
            return true;
        } else return false;
    }

    public void setBlockToUpdate() {
        sendUpdates();
    }

    private void sendUpdates() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        markDirty();
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T)input;
        else if(capability == CapabilityEnergy.ENERGY) {
            return (T) this;
        }
        else return null;
    }

    public ItemStackHandler getInventory(){
        return input;
    }

    public void update() {
        if (!world.isRemote) {
            if (isGenerating()) {
                energyStoredRF += this.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN).receiveEnergy(GENERATION_RATE, true);
                this.setBlockToUpdate();
            }

            //push energy to any adjacent energy capabilities;
            //apparently FE, like RF, is primarily push-based for various reasons
            for (EnumFacing side : EnumFacing.VALUES) {
                TileEntity tileEntity = world.getTileEntity(pos.offset(side));
                if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite()) && tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).canReceive()) {
                    int energyExtracted = this.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).extractEnergy(TRANSFER_RATE, true);
                    System.out.println(energyExtracted + " | " + TRANSFER_RATE + " | " + energyStoredRF);
                    tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).receiveEnergy(energyExtracted, false);
                    this.energyStoredRF -= energyExtracted;
                    this.setBlockToUpdate();
                }
            }

            ItemStack item = getInventory().getStackInSlot(0);
            if (item.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)) {
                int itemSlotEnergyExtracted = this.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN).extractEnergy(TRANSFER_RATE, true);
                item.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN).receiveEnergy(itemSlotEnergyExtracted, false);
                this.energyStoredRF -= itemSlotEnergyExtracted;
                this.setBlockToUpdate();
            }

            if (runnerBean == null || runnerBean.isDead) {
                runnerBean = findBean();
                if (runnerBean != null) {
                    EnumFacing facing = world.getBlockState(pos).getValue(BlockBasicGenerator.FACING);
                    if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH)
                        runnerBean.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.0625, pos.getZ() + 0.5, 0, 0);
                    else runnerBean.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.0625, pos.getZ() + 0.5, 90, 0);
                    runnerBean.setNoAI(true);
                    System.out.println("Bean on treadmill!");
                }
            }
        }
    }

    public boolean isGenerating(){
        return runnerBean != null;
    }

    public void setGenerating(boolean in){
        this.generating = in;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        simulate = true;
        int remainder = (maxReceive + energyStoredRF <= getMaxEnergyStored()) ? 0 : (maxReceive + energyStoredRF) - getMaxEnergyStored();
        if (!simulate){
            energyStoredRF = (remainder == 0) ? energyStoredRF + maxReceive : getMaxEnergyStored();
        }
        return maxReceive - remainder;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        //Disables the extract energy method
        //For use only to simulate
        //Seems to cause looping problems with connected cables if both receive and extract are enabled
        //Current solution is to just push and keep this method disabled
        simulate = true;

        int actual = Math.min(maxExtract, energyStoredRF);
        this.energyStoredRF -= simulate ? 0 : actual;
        this.energyStoredRF = (energyStoredRF < 0) ? 0 : energyStoredRF;
        return actual;
    }

    @Override
    public int getEnergyStored() {
        return energyStoredRF;
    }

    @Override
    public int getMaxEnergyStored() {
        return 16000;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
