package com.currenj.beanblocks.block.brandassembler;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucketFull;
import com.currenj.beanblocks.item.*;
import com.currenj.beanblocks.item.brand.EnumBeanBrandHeadVariants;
import com.currenj.beanblocks.item.brand.ItemBeanBrandHead;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
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

public class TileEntityBrandAssembler extends TileEntity {

    private ItemStackHandler rodSlot = new ItemStackHandler(1);
    private ItemStackHandler headSlot = new ItemStackHandler(1);
    private ItemStackHandler bindingSlot = new ItemStackHandler(1);
    private ItemStackHandler modifierSlots = new ItemStackHandler(6);
    private ItemStackHandler outputSlot = new ItemStackHandler(1);
    private boolean updatedSlotInternally;
    public static enum INVENTORIES {
        ROD,HEAD,BIND,MOD,OUT
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("rodSlot", rodSlot.serializeNBT());
        compound.setTag("headSlot", headSlot.serializeNBT());
        compound.setTag("bindingSlot", bindingSlot.serializeNBT());
        compound.setTag("modifierSlots", modifierSlots.serializeNBT());
        compound.setTag("outputSlot", outputSlot.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        rodSlot.deserializeNBT(compound.getCompoundTag("rodSlot"));
        headSlot.deserializeNBT(compound.getCompoundTag("headSlot"));
        bindingSlot.deserializeNBT(compound.getCompoundTag("bindingSlot"));
        modifierSlots.deserializeNBT(compound.getCompoundTag("modifierSlots"));
        outputSlot.deserializeNBT(compound.getCompoundTag("outputSlot"));
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
        if(facing == EnumFacing.DOWN)
            return (T)outputSlot;
        else return (T)rodSlot;
    }


    public ItemStackHandler getInventory(INVENTORIES inventory){
        if(inventory == INVENTORIES.ROD)
            return rodSlot;
        else if(inventory == INVENTORIES.HEAD)
            return headSlot;
        else if(inventory == INVENTORIES.BIND)
            return bindingSlot;
        else if(inventory == INVENTORIES.MOD)
            return modifierSlots;
        else if(inventory == INVENTORIES.OUT)
            return outputSlot;
        else return null;
    }

    public void updateOutput(){
        if(getInventory(INVENTORIES.HEAD).getStackInSlot(0).getCount() > 0 &&
                getInventory(INVENTORIES.ROD).getStackInSlot(0).getCount() > 0 &&
                getInventory(INVENTORIES.BIND).getStackInSlot(0).getCount() > 0){
            ItemStack beanBrand = new ItemStack(ModItems.itemBeanBrandingToolHeated, 1);
            NBTTagCompound compound = beanBrand.getTagCompound();
            beanBrand.setTagCompound(saveSlotsToNBT(compound));
            System.out.println("NBT: " + beanBrand.getTagCompound());
            outputSlot.setStackInSlot(0, beanBrand);
        } else {
            outputSlot.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    public ItemStack updateOutput(ItemStack stack) {
        ItemStack head = getInventory(INVENTORIES.HEAD).getStackInSlot(0);
        ItemStack rod = getInventory(INVENTORIES.ROD).getStackInSlot(0);
        ItemStack bind = getInventory(INVENTORIES.BIND).getStackInSlot(0);
        if (head.getCount() > 0 &&
                rod.getCount() > 0 &&
                bind.getCount() > 0) {
            ItemStack beanBrand = new ItemStack(ModItems.itemBeanBrandingToolHeated, 1);
            NBTTagCompound compound = beanBrand.getTagCompound();
            beanBrand.setTagCompound(saveSlotsToNBT(compound));

            if(head.getItem() instanceof ItemBeanBrandHead){
                if(EnumBeanBrandHeadVariants.byDyeDamage(head.getItemDamage()) == EnumBeanBrandHeadVariants.BEAN_FARMER)
                    beanBrand.setStackDisplayName("Bean Farmer Brand");
                else if(EnumBeanBrandHeadVariants.byDyeDamage(head.getItemDamage()) == EnumBeanBrandHeadVariants.COMPANION)
                    beanBrand.setStackDisplayName("Bean Companion Brand");
            }
            
            System.out.println("NBT: " + beanBrand.getTagCompound());
            stack = beanBrand;
            return beanBrand;
        } else {
            stack = ItemStack.EMPTY;
            return ItemStack.EMPTY;
        }
    }

    public void takeOutput(){
        updateOutput();
        System.out.println("Updated output slot!" + this.toString());
        getInventory(INVENTORIES.ROD).getStackInSlot(0).shrink(1);
        getInventory(INVENTORIES.HEAD).getStackInSlot(0).shrink(1);
        getInventory(INVENTORIES.BIND).getStackInSlot(0).shrink(1);
        ItemStackHandler modifiers = getInventory(INVENTORIES.MOD);
        for (int i = 0; i < modifiers.getSlots(); i++) {
            modifiers.getStackInSlot(i).shrink(1);
        }
        updatedSlotInternally = false;
    }

    public NBTTagCompound saveSlotsToNBT(NBTTagCompound tagIn){
        NBTTagCompound tagOut = new NBTTagCompound();
        if(tagIn != null)
            tagOut = tagIn;
        tagOut.setTag("rod", rodSlot.serializeNBT());
        tagOut.setTag("head", headSlot.serializeNBT());
        tagOut.setTag("modifiers", modifierSlots.serializeNBT());
        return tagOut;
    }
}
