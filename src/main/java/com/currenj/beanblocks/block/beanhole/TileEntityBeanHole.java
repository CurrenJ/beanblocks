package com.currenj.beanblocks.block.beanhole;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucketFull;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemBeansCompressed;
import com.currenj.beanblocks.item.ItemDenseBeanBar;
import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityBeanHole extends TileEntity {

    private ItemStackHandler inventory = new ItemStackHandler(3);

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


    private void setBlockToUpdate() {
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
        return (T)inventory;
    }

    public ItemStackHandler getInventory(){
        return inventory;
    }
}
