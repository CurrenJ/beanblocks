package com.currenj.beanblocks.block.beanbucket;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityBeanBucketFull extends TileEntity {

    private ItemStackHandler inventory = new ItemStackHandler(1);

    public void setInventory(ItemStack itemStack){
        System.out.println("Setting bInventory to " + itemStack.getUnlocalizedName() + " (" + itemStack.getCount() + ")");
        inventory.setStackInSlot(0, itemStack);
        System.out.println("Getting bInventory " + inventory.getStackInSlot(0).getUnlocalizedName() + " (" + inventory.getStackInSlot(0).getCount() + ")");
    }

    public boolean isEmpty(){
        return inventory.getStackInSlot(0).getCount() <= 0;
    }

    public ItemStack retrieveItem(){
        if(inventory.getStackInSlot(0).getCount() > 0) {
            ItemStack itemStack = new ItemStack(inventory.getStackInSlot(0).getItem(), 1);
            inventory.getStackInSlot(0).shrink(1);
            return itemStack;
        }
        return ItemStack.EMPTY;
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
    }


    public void setBlockToUpdate() {
        sendUpdates();
    }

    private void sendUpdates() {
        world.markBlockRangeForRenderUpdate(pos, pos);
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
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

}
