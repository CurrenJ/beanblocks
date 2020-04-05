package com.currenj.beanblocks.block;

import com.currenj.beanblocks.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockAncientBeans extends BlockBase {

    public BlockAncientBeans(Material material, String name) {
        super(material, name);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.pintoBean;
    }
}
