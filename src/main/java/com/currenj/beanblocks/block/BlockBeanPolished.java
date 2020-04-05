package com.currenj.beanblocks.block;

import com.currenj.beanblocks.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBeanPolished extends BlockBase {

    public BlockBeanPolished(Material material, String name) {
       super(material, name);
       setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
       setHardness(1.5F);
       setHarvestLevel("pickaxe", 0);
    }
}
