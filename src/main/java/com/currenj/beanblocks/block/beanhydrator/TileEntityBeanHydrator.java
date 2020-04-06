package com.currenj.beanblocks.block.beanhydrator;

import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemFossilizedBeans;
import com.currenj.beanblocks.item.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityBeanHydrator extends TileEntity implements ITickable {

    private ItemStackHandler inventory = new ItemStackHandler(3);
    private int hydrateTime = 5;
    private static final int CAPACITY = 10000;
    private int waterLevel = 0; //in mB

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("HydrateTime", (short)this.hydrateTime);
        compound.setInteger("WaterLevel", (short)this.waterLevel);
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.hydrateTime = compound.getInteger("HydrateTime");
        this.waterLevel = compound.getInteger("WaterLevel");
        super.readFromNBT(compound);
    }

    @Override
    public void onLoad(){
        updateBlockState();
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

    public int getOutputSlot(){
        return 1;
    }

    public int getWaterBucketSlot(){
        return 2;
    }

    public int getBeanSlot(){
        return 0;
    }

    public void update(){
        if(this.isPressing()){
            --this.hydrateTime;
        }

        ItemStack inputStack = inventory.getStackInSlot(getBeanSlot());
        ItemStack bucketStack = inventory.getStackInSlot(getWaterBucketSlot());
        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        if(canFill() && bucketStack.getItem().getUnlocalizedName().equals(Items.WATER_BUCKET.getUnlocalizedName())){
            waterLevel += 1000;
            inventory.setStackInSlot(getWaterBucketSlot(), new ItemStack(Items.BUCKET, 1));
            System.out.println("Water level: " + waterLevel);
            updateBlockState();
        }

        if(canHydrate(inputStack, bucketStack, outputStack)){
            if(hydrateTime <= 0){
                    hydrateTime = 100;
                    ItemStack[] newSlots = hydrateBean(inputStack, bucketStack, outputStack);
                    if(!world.isRemote) {
                        inventory.setStackInSlot(getOutputSlot(), newSlots[0]);
                        inventory.setStackInSlot(getBeanSlot(), newSlots[1]);
                    }
                    waterLevel -= 1000;
                    System.out.println("Water level: " + waterLevel);
                    updateBlockState();
                    sendUpdates();
            }
        }   else hydrateTime = 100;
    }

    public boolean fillFromHand(ItemStack hand){
        if(canFill() && hand.getUnlocalizedName().equals(Items.WATER_BUCKET.getUnlocalizedName())) {
            waterLevel += 1000;
            updateBlockState();
            return true;
        } else return false;

    }

    public boolean canFill(){
        return waterLevel <= getWaterCapacity() - 1000;
    }

    private void updateBlockState(){
        getWorld().setBlockState(this.pos, getWorld().getBlockState(pos).withProperty(BlockBeanHydrator.WATER_LEVEL, (int)(waterLevel * 10F / CAPACITY)));
    }

    public boolean isPressing(){
        if(hydrateTime > 0)
            return true;
        else return false;
    }

    public boolean canHydrate(ItemStack input, ItemStack filter, ItemStack currentOutputStack){
        return canHydrate("fossilized_beans", input, filter, currentOutputStack) && waterLevel >= 1000;
    }

    public boolean canHydrate(String unlocalizedNameOutput, ItemStack input, ItemStack filter, ItemStack currentOutputStack){
        if(unlocalizedNameOutput.equals("fossilized_beans")) {
            return input.getItem() instanceof ItemFossilizedBeans && input.getCount() > 0 && (currentOutputStack.getCount() == 0 || currentOutputStack.getItem() instanceof ItemBeanPinto) && currentOutputStack.getCount() < 64;
        }
        else return false;
    }

    public ItemStack[] hydrateBean(ItemStack input, ItemStack filter, ItemStack currentOutputStack){
        if(!world.isRemote) {
            if (canHydrate("fossilized_beans", input, filter, currentOutputStack)) {
                int count = (new Random()).nextInt(3);
                if(count + currentOutputStack.getCount() <= 64) {
                    ItemStack[] newSlots = {new ItemStack(ModItems.pintoBean, currentOutputStack.getCount() + count), new ItemStack(input.getItem(), input.getCount() - 1)};
                    return newSlots;
                } else {
                    ItemStack[] newSlots = {ItemStack.EMPTY, ItemStack.EMPTY};
                    return newSlots;
                }
            }
            ItemStack[] newSlots = {ItemStack.EMPTY, ItemStack.EMPTY};
            return newSlots;
        }
        ItemStack[] stack = new ItemStack[1];
        stack[0] = ItemStack.EMPTY;
        return stack;
    }

    public int getHydrateTime(){
        return hydrateTime;
    }

    public static int getWaterCapacity() { return CAPACITY; }

    public int getWaterLevel(){
        return waterLevel;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }
}
