package com.currenj.beanblocks.block.beanpress;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.block.ModBlocks;
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

public class TileEntityBeanPress extends TileEntity implements ITickable {

    private ItemStackHandler inventory = new ItemStackHandler(2);
    private ItemStackHandler outputSlot = new ItemStackHandler(1);
    private int pressTime = 5;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("PressTime", (short)this.pressTime);
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setTag("outputSlot", outputSlot.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        outputSlot.deserializeNBT(compound.getCompoundTag("outputSlot"));
        this.pressTime = compound.getInteger("PressTime");
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
        if(facing == EnumFacing.DOWN)
            return (T)outputSlot;
        else return (T)inventory;
    }

    public ItemStackHandler getInventory(){
        return inventory;
    }

    public int getOutputSlot(){
        return 0;
    }

    public int getFilterSlot(){
        return 1;
    }

    public int getBeanSlot(){
        return 0;
    }

    public int getBeansPerCompression(){
        return 9;
    }

    public void update(){
        if(this.isPressing()){
            --this.pressTime;
        }

        ItemStack inputStack = inventory.getStackInSlot(getBeanSlot());
        ItemStack filterStack = inventory.getStackInSlot(getFilterSlot());
        ItemStack outputStack = outputSlot.getStackInSlot(getOutputSlot());
        if(canPress(inputStack, filterStack, outputStack)){
            if(pressTime <= 0){
                    pressTime = 100;
                    ItemStack[] newSlots = pressBean(inputStack, filterStack, outputStack);
                    outputSlot.setStackInSlot(getOutputSlot(), newSlots[0]);
                    inventory.setStackInSlot(getBeanSlot(), newSlots[1]);
                    sendUpdates();
            }
        }   else pressTime = 100;
    }

    public boolean isPressing(){
        if(pressTime > 0)
            return true;
        else return false;
    }

    public boolean canPress(ItemStack input, ItemStack filter, ItemStack currentOutputStack){
        return canPress("beans_compressed", input, filter, currentOutputStack) ||
                canPress("bean_bucket_full", input, filter, currentOutputStack) ||
                canPress("dense_bean_bar", input, filter, currentOutputStack) ||
                canPress("bean_bucket_empty", input, filter, currentOutputStack);
    }

    public boolean canPress(String unlocalizedNameOutput, ItemStack input, ItemStack filter, ItemStack currentOutputStack){
        if(unlocalizedNameOutput.equals("beans_compressed")) {
            //System.out.print("1: " + (filter.getItem() instanceof ItemPressFilter));
            //System.out.print(" 2: " + (filter.getMetadata() == 0));
            //System.out.print(" 3: " + (input.getUnlocalizedName() == "bean_pinto"));
            //System.out.print(" 4: " + (input.getCount() >= getBeansPerCompression()));
            //System.out.print(" 5: " + currentOutputStack.getUnlocalizedName() + ", " + ((currentOutputStack.getCount() == 0 || currentOutputStack.getUnlocalizedName().equals("item.beans_compressed"))));
            //System.out.print(" 6: " + (currentOutputStack.getCount()));
            //System.out.println();
            return filter.getItem() instanceof ItemPressFilter && filter.getMetadata() == 0 && input.getItem() instanceof ItemBeanPinto && input.getCount() >= getBeansPerCompression() && (currentOutputStack.getCount() == 0 || currentOutputStack.getItem() instanceof ItemBeansCompressed) && currentOutputStack.getCount() < 64;
        }
        else if(unlocalizedNameOutput.equals("bean_bucket_full"))
            return filter.getItem() instanceof ItemPressFilter && filter.getMetadata() == 1 && Block.getBlockFromItem(input.getItem()) instanceof BlockBean && input.getCount() >= 1 && currentOutputStack.getCount() > 0 && Block.getBlockFromItem(currentOutputStack.getItem()) instanceof BlockBeanBucket && currentOutputStack.getCount() == 1;
        else if(unlocalizedNameOutput.equals("dense_bean_bar"))
            return filter.getItem() instanceof ItemPressFilter && filter.getMetadata() == 0 && Block.getBlockFromItem(input.getItem()) instanceof BlockBean && input.getCount() >= 1 && (currentOutputStack.getCount() == 0 || currentOutputStack.getItem() instanceof ItemDenseBeanBar) && currentOutputStack.getCount() < 64;
        else if(unlocalizedNameOutput.equals("bean_bucket_empty"))
            return filter.getItem() instanceof ItemPressFilter && filter.getMetadata() == 1 && Block.getBlockFromItem(input.getItem()) instanceof BlockBeanBucketFull && input.getCount() >= 1 && ((Block.getBlockFromItem(currentOutputStack.getItem()) instanceof BlockBeanBucket) || (currentOutputStack.getCount() == 0));

        else return false;
    }

    public ItemStack[] pressBean(ItemStack input, ItemStack filter, ItemStack currentOutputStack){
        if(canPress("beans_compressed", input, filter, currentOutputStack)){
            ItemStack[] newSlots = {new ItemStack(ModItems.compressedBeans, currentOutputStack.getCount()+1), new ItemStack(input.getItem(), input.getCount()-getBeansPerCompression())};
            return newSlots;
        }
        else if(canPress("bean_bucket_full", input, filter, currentOutputStack)){
            ItemStack[] newSlots = {new ItemStack(ModBlocks.blockBeanBucketFull, 1), new ItemStack(input.getItem(), input.getCount()-1)};
            return newSlots;
        }
        else if(canPress("dense_bean_bar", input, filter, currentOutputStack)){
            ItemStack[] newSlots = {new ItemStack(ModItems.denseBeanBar, currentOutputStack.getCount()+1), new ItemStack(input.getItem(), input.getCount()-1)};
            return newSlots;
        }
        else if(canPress("bean_bucket_empty", input, filter, currentOutputStack)){
            ItemStack[] newSlots = {new ItemStack(ModBlocks.blockBeanBucket, currentOutputStack.getCount()+1), new ItemStack(input.getItem(), input.getCount()-1)};
            return newSlots;
        }
        ItemStack[] newSlots = {ItemStack.EMPTY, ItemStack.EMPTY};
        return newSlots;
    }

    public int getPressTime(){
        return pressTime;
    }
}
