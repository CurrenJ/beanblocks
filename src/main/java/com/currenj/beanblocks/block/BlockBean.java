package com.currenj.beanblocks.block;

import com.currenj.beanblocks.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBean extends BlockBase {

    public BlockBean(Material material, String name, CreativeTabs tab) {
        super(material, name, tab);
        setHardness(0.7F);
        setSoundType(SoundType.SLIME);
        setHarvestLevel("shovel", 0);
    }
}
