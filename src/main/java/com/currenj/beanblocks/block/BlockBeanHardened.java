package com.currenj.beanblocks.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBeanHardened extends BlockBase{

    public BlockBeanHardened(Material material, String name) {
        super(material, name);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 0);
    }
}
