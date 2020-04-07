package com.currenj.beanblocks.block.beanrecycler;

import com.currenj.beanblocks.item.filter.recycler.EnumRecyclerFilterVariants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class RecyclerDrop {

    private ItemStack itemStack;
    private Item item;
    private ArrayList<Integer> metadataList;
    private double chance;
    private ArrayList<EnumRecyclerFilterVariants> filters;
    private Double[] bounds;

    public RecyclerDrop(Item item, int metadata, double chance, ArrayList<EnumRecyclerFilterVariants> filters){
        this.itemStack = null;
        this.item = item;
        metadataList = new ArrayList<Integer>();
        metadataList.add(metadata);
        this.chance = chance;
        this.filters = filters;
        this.bounds = new Double[2];
    }

    public RecyclerDrop(Item item, double chance, ArrayList<EnumRecyclerFilterVariants> filters){
        this.itemStack = null;
        this.item = item;
        metadataList = new ArrayList<Integer>();
        metadataList.add(0);
        this.chance = chance;
        this.filters = filters;
        this.bounds = new Double[2];
    }

    public RecyclerDrop(ItemStack itemStack, double chance, ArrayList<EnumRecyclerFilterVariants> filters){
        this.itemStack = itemStack;
        metadataList = new ArrayList<Integer>();
        metadataList.add(0);
        this.chance = chance;
        this.filters = filters;
        this.bounds = new Double[2];
    }

    public RecyclerDrop(Item item, double chance, ArrayList<Integer> metadata, ArrayList<EnumRecyclerFilterVariants> filters){
        this.itemStack = null;
        this.item = item;
        this.chance = chance;
        this.metadataList = metadata;
        this.filters = filters;
        this.bounds = new Double[2];
    }

    public ItemStack getItemStack(int count){
        if(itemStack == null) {
            int metadata = metadataList.get((new Random()).nextInt(metadataList.size()));
            return new ItemStack(item, count, metadata);
        } else return itemStack;
    }

    public String getIdentifier(){
        return item.getUnlocalizedName() + metadataList.toString();
    }

    public ArrayList<EnumRecyclerFilterVariants> getFilters(){
        return filters;
    }

    public double getChance(){
        return chance;
    }

    public void setBounds(double from, double to){
        bounds[0] = from;
        bounds[1] = to;
    }

    public void setBounds(Double[] bounds){
        this.bounds = bounds;
    }

    public Double[] getBounds(){
        return bounds;
    }
}
