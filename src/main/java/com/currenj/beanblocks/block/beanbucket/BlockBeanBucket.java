package com.currenj.beanblocks.block.beanbucket;

import com.currenj.beanblocks.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBeanBucket extends BlockBase {

    public BlockBeanBucket() {
        super(Material.ROCK, "bean_bucket", CreativeTabs.DECORATIONS);
        setHardness(2);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
