package com.currenj.beanblocks.block.beanrecycler;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemBeansCompressed;
import com.currenj.beanblocks.item.ItemDenseBeanBar;
import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.item.filter.recycler.EnumRecyclerFilterVariants;
import com.currenj.beanblocks.item.filter.recycler.ItemRecyclerFilter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class TileEntityBeanRecycler extends TileEntity implements ITickable {

    private ItemStackHandler inventory = new ItemStackHandler(4);
    private ItemStackHandler outputInventory = new ItemStackHandler(15);
    private int recycleTime;
    private double beanWaste;
    private boolean risingEdge;
    private static HashMap<Item, Double> dropChances;
    static {
        dropChances = new HashMap<>();
        dropChances.put(Items.DIAMOND, 0.01);
        dropChances.put(Items.GOLD_NUGGET, 0.1);
        dropChances.put(Items.IRON_NUGGET, 0.2);
        dropChances.put(Item.getItemFromBlock(Blocks.DIRT), 0.07);
        dropChances.put(Items.STICK, 0.03);
        dropChances.put(Items.SLIME_BALL, 0.02);
        dropChances.put(Items.ROTTEN_FLESH, 0.1);
        dropChances.put(Items.LEATHER, 0.04);
        dropChances.put(Item.getItemFromBlock(Blocks.LEAVES), 0.04);
        dropChances.put(Item.getItemFromBlock(Blocks.LOG), 0.06);
        dropChances.put(Item.getItemFromBlock(Blocks.PLANKS), 0.09);
    }
    private static HashMap<Item, ArrayList<EnumRecyclerFilterVariants>> dropFilters;
    static {
        dropFilters = new HashMap<>();
        dropFilters.put(Items.DIAMOND, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); }});
        dropFilters.put(Items.GOLD_NUGGET, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        dropFilters.put(Items.IRON_NUGGET, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        dropFilters.put(Item.getItemFromBlock(Blocks.DIRT), new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.METALFILTER); add(EnumRecyclerFilterVariants.WOODENFILTER); add(EnumRecyclerFilterVariants.ORGANICFILTER);}});
        dropFilters.put(Items.STICK, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); add(EnumRecyclerFilterVariants.WOODENFILTER); add(EnumRecyclerFilterVariants.ORGANICFILTER);}});
        dropFilters.put(Items.SLIME_BALL, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); add(EnumRecyclerFilterVariants.WOODENFILTER);}});
        dropFilters.put(Items.ROTTEN_FLESH, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.METALFILTER); add(EnumRecyclerFilterVariants.WOODENFILTER);}});
        dropFilters.put(Items.LEATHER, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER);}});
        dropFilters.put(Item.getItemFromBlock(Blocks.LEAVES), new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.ORGANICFILTER); }});
        dropFilters.put(Item.getItemFromBlock(Blocks.LOG), new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.ORGANICFILTER); }});
        dropFilters.put(Item.getItemFromBlock(Blocks.PLANKS), new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.ORGANICFILTER); }});
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("RecycleTime", (short)this.recycleTime);
        compound.setDouble("BeanWaste", this.beanWaste);
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setTag("oInventory", outputInventory.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        outputInventory.deserializeNBT(compound.getCompoundTag("oInventory"));
        this.recycleTime = compound.getInteger("RecycleTime");
        this.beanWaste = compound.getDouble("BeanWaste");
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
    public void onLoad() {
        System.out.println("BWLOAD: " + this.beanWaste + "RTLOAD: " + this.recycleTime);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return EnumFacing.DOWN == facing ? (T)outputInventory : (T)inventory;
    }

    public int getFilterSlot(){
        return 1;
    }

    public int getBeanSlot(){
        return 0;
    }

    public int getEmptyBucketSlot() {
        return 2;
    }

    public int getFullBucketSlot(){
        return 3;
    }

    public void update(){
        if(this.isRecycling() && recycleTime > 0){
            --this.recycleTime;
        }

        if(beanWaste > 0 &&
                inventory.getStackInSlot(getEmptyBucketSlot()).getCount() > 0 &&
                inventory.getStackInSlot(getEmptyBucketSlot()).getCount() < inventory.getStackInSlot(getEmptyBucketSlot()).getMaxStackSize() &&
                inventory.getStackInSlot(getFullBucketSlot()).getCount() < inventory.getStackInSlot(getFullBucketSlot()).getMaxStackSize()){
            int bucketsToBeFilled = (int)(beanWaste / BlockBeanRecycler.beanWasteMax);

            while(bucketsToBeFilled > 0 && inventory.getStackInSlot(getEmptyBucketSlot()).getCount() > 0){
                inventory.getStackInSlot(getEmptyBucketSlot()).shrink(1);
                inventory.setStackInSlot(getFullBucketSlot(), new ItemStack(ModBlocks.blockBeanBucketFull, inventory.getStackInSlot(getFullBucketSlot()).getCount()+1));
                bucketsToBeFilled--;
                beanWaste -= BlockBeanRecycler.beanWasteMax;
                setBlockToUpdate();
            }
        }

        ItemStack inputStack = inventory.getStackInSlot(getBeanSlot());
        ItemStack filterStack = inventory.getStackInSlot(getFilterSlot());
        boolean jammed = false;
        if(canTryRecycle(inputStack, filterStack)){
            if(!risingEdge) {
                recycleTime = getMaxRecycleTime();
                risingEdge = true;
            }
            if(recycleTime <= 0){
                    ArrayList<ItemStack> outputItems = getOutputStack(inputStack, filterStack);
                    if(hasSpaceFor(outputItems) && beanWaste < BlockBeanRecycler.beanWasteMax) {
                        recycleTime = getMaxRecycleTime();
                        beanWaste += getBeanWasteValue(inputStack);
                        finishRecycle(outputItems, inputStack, filterStack);
                        System.out.println("Finished recycle. [" + toString() + "]");
                        setBlockToUpdate();
                    } else {
                        jammed = true;
                    }
            }
        }   else {
            risingEdge = false;
            recycleTime = getMaxRecycleTime();
        }
    }

    public boolean isRecycling(){
        ItemStack inputStack = inventory.getStackInSlot(getBeanSlot());
        ItemStack filterStack = inventory.getStackInSlot(getFilterSlot());
        if(recycleTime > 0 || canTryRecycle(inputStack, filterStack))
            return true;
        else return false;
    }

    public boolean canTryRecycle(ItemStack input, ItemStack filter){
        return canTryRecycle("bean_pinto", input, filter) ||
                canTryRecycle("beans_compressed", input, filter) ||
                canTryRecycle("bean_block", input, filter) ||
                canTryRecycle("dense_bean_bar", input, filter);
    }

    public boolean canTryRecycle(String inputOption, ItemStack input, ItemStack filter){
        if(inputOption.equals("bean_pinto")) {
            return filter.getItem() instanceof ItemRecyclerFilter && input.getItem() instanceof ItemBeanPinto && input.getCount() > 0;
        } else if(inputOption.equals("beans_compressed")) {
            return filter.getItem() instanceof ItemRecyclerFilter && input.getItem() instanceof ItemBeansCompressed && input.getCount() > 0;
        } else if(inputOption.equals("bean_block")) {
            return filter.getItem() instanceof ItemRecyclerFilter && Block.getBlockFromItem(input.getItem()) instanceof BlockBean && input.getCount() > 0;
        } else if(inputOption.equals("dense_bean_bar")) {
            return filter.getItem() instanceof ItemRecyclerFilter && input.getItem() instanceof ItemDenseBeanBar && input.getCount() > 0;
        }
        else return false;
    }

    private HashMap<Item, Double[]> getDropBounds(double chanceMultiplier, EnumRecyclerFilterVariants filter){
        HashMap<Item, Double[]> dropBounds = new HashMap<>();

        Item[] items = dropChances.keySet().toArray(new Item[dropChances.size()]);
        Double[] chances = dropChances.values().toArray(new Double[dropChances.size()]);
        for(int i = 0; i < dropChances.size(); i++){
            double lastBound = 0;
            if (i > 0)
                lastBound = dropBounds.get(items[i - 1])[1];

            Double[] bounds = new Double[2];
            if(dropFilters.get(items[i]).contains(filter)) {
                bounds[0] = lastBound;
                bounds[1] = lastBound + chances[i] * chanceMultiplier;

                dropBounds.put(items[i], bounds);
                //System.out.println(items[i].getUnlocalizedName() + " [" + bounds[0] + ", " + bounds[1] + "]");
            } else {
                bounds[0] = lastBound;
                bounds[1] = lastBound;

                dropBounds.put(items[i], bounds);
                //System.out.println(items[i].getUnlocalizedName() + " [" + bounds[0] + ", " + bounds[1] + "]");
            }
        }

        return dropBounds;
    }


    private ArrayList<ItemStack> getOutputStack(ItemStack inputStack, ItemStack filterStack){
        ArrayList<ItemStack> outputStack = new ArrayList<ItemStack>();
        double inputMultiplier = getBeanWasteValue(inputStack);
        double filterMultiplier = 1;
        if(filterStack.getItemDamage() == 0)
            filterMultiplier *= 0.6;
        else if(filterStack.getItemDamage() == 1)
            filterMultiplier *= 1;
        else if(filterStack.getItemDamage() == 2)
            filterMultiplier *= 0.25;

        HashMap<Item, Double[]> dropBounds = getDropBounds(filterMultiplier, EnumRecyclerFilterVariants.byMetadata(filterStack.getMetadata()));
        Item[] items = dropBounds.keySet().toArray(new Item[dropBounds.size()]);
        Random rand = new Random();
        for(int i = 0; i < inputMultiplier; i++){
            Double seed = rand.nextDouble();
            for(int b = 0; b < dropBounds.size(); b++){
                if(seed > dropBounds.get(items[b])[0] && seed <= dropBounds.get(items[b])[1])
                    outputStack.add(new ItemStack(items[b], 1));
            }
        }
        return outputStack;
    }

    private void consumeInput(){
        inventory.getStackInSlot(getBeanSlot()).shrink(1);
    }

    private boolean hasSpaceFor(ArrayList<ItemStack> items){
        for(int s = 0; s < items.size(); s++) {
            ItemStack outputStack = items.get(s);
            int countInOutputStack = outputStack.getCount();
            //Iterate through output slots and subtract from the countInOutputStack when we can combine stacks, or place into an empty slot.
            for (int i = 0; i < outputInventory.getSlots() && countInOutputStack > 0; i++) {
                ItemStack stackInSlot = inventory.getStackInSlot(i);
                if (outputStack.getItem().getClass().isInstance(stackInSlot.getItem())){
                    countInOutputStack -= (stackInSlot.getMaxStackSize() - stackInSlot.getCount());
                } else if(stackInSlot.isEmpty()){
                    countInOutputStack -= outputStack.getMaxStackSize();
                }
            }
            if(countInOutputStack > 0)
                return false;
        }
        return true;
    }

    public void finishRecycle(ArrayList<ItemStack> outputItems, ItemStack input, ItemStack filter){
        if(!world.isRemote) {
            System.out.print("Adding ");
            for (int s = 0; s < outputItems.size(); s++) {
                ItemStack outputStack = outputItems.get(s);
                int countInOutputStack = outputStack.getCount();
                System.out.print(outputStack.getItem().getUnlocalizedName() + "(" + countInOutputStack + "), ");
                //Iterate through output slots and subtract from the countInOutputStack when we can combine stacks, or place into an empty slot.
                for (int i = 0; i < outputInventory.getSlots() && countInOutputStack > 0; i++) {
                    ItemStack stackInSlot = outputInventory.getStackInSlot(i);
                    //If found stack of same type, merge.
                    //System.out.println(outputStack.getItem().getClass() + " |" + stackInSlot.getItem().getClass());
                    if (outputStack.getUnlocalizedName().equals(stackInSlot.getUnlocalizedName())) {
                        int spaceInSlotStack = (stackInSlot.getMaxStackSize() - stackInSlot.getCount());
                        //If the whole output stack doesn't fit, merge as much as does fit.
                        if (countInOutputStack > spaceInSlotStack) {
                            //System.out.println("Merging partial output " + s + " into inventory slot " + i + " (" + spaceInSlotStack + " free)");
                            countInOutputStack -= spaceInSlotStack;
                            outputInventory.setStackInSlot(i, new ItemStack(outputStack.getItem(), stackInSlot.getMaxStackSize()));
                        }
                        //If the whole stack fits, put it all in.
                        else {
                            //System.out.println("Merging whole output " + s + " into inventory slot " + i + " (" + spaceInSlotStack + " free)");
                            outputInventory.setStackInSlot(i, new ItemStack(outputStack.getItem(), countInOutputStack + stackInSlot.getCount()));
                            countInOutputStack = 0;
                        }
                        //If found an empty slot, deposit as much of the output stack as fits.
                    } else if (stackInSlot.isEmpty()) {
                        //System.out.println("Putting whole output " + s + " into empty inventory slot " + i + " (" + outputStack.getMaxStackSize() + " free)");
                        outputInventory.setStackInSlot(i, new ItemStack(outputStack.getItem(), countInOutputStack));
                        countInOutputStack -= outputStack.getMaxStackSize();
                    }
                }
            }
            System.out.println();
            System.out.println("items added in [" + toString() + "]");
        }
        consumeInput();
    }

    public double getBeanWasteValue(ItemStack itemStack){
        if(itemStack.getItem() instanceof ItemBeanPinto)
            return 1.0;
        else if(itemStack.getItem() instanceof ItemBeansCompressed)
            return 9.0;
        else if(Block.getBlockFromItem(itemStack.getItem()) instanceof BlockBean)
            return 36.0;
        else if(itemStack.getItem() instanceof ItemDenseBeanBar)
            return 36.0;
        else return 0;
    }

    public int getRecycleTime(){
        return recycleTime;
    }

    public int getMaxRecycleTime(){
        float filterEffect = 1;
        if(inventory.getStackInSlot(getFilterSlot()).getCount() > 0){
            if(inventory.getStackInSlot(getFilterSlot()).getItemDamage() == 2)
                filterEffect = 1F;
            else if(inventory.getStackInSlot(getFilterSlot()).getItemDamage() == 0 || inventory.getStackInSlot(getFilterSlot()).getItemDamage() == 3    )
                filterEffect = 0.8F;
            else if(inventory.getStackInSlot(getFilterSlot()).getItemDamage() == 1)
                filterEffect = 0.6F;
        }
        float inputTime = 50;
        if(inventory.getStackInSlot(getBeanSlot()).getCount() > 0){
            inputTime *= getBeanWasteValue(inventory.getStackInSlot(getBeanSlot()));
        }

        return (int)(inputTime * filterEffect);
    }

    public static void tryAddForeignModItem(String oreDictionary, double chance, ArrayList<EnumRecyclerFilterVariants> filters){
        NonNullList<ItemStack> references = OreDictionary.getOres(oreDictionary);
        if(references.size() > 0)
            addDropChance(references.get(0).getItem(), chance, filters);
    }

    public static void addDropChance(Item item, double chance, ArrayList<EnumRecyclerFilterVariants> filters){
        dropChances.put(item, chance);
        dropFilters.put(item, filters);
    }

    public void filterChanged(){
        recycleTime = getMaxRecycleTime();
        setBlockToUpdate();
    }

    public double getBeanWaste(){ return beanWaste; }
}
