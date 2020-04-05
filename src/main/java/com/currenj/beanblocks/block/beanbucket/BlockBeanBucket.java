package com.currenj.beanblocks.block.beanbucket;

import com.currenj.beanblocks.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBeanBucket extends BlockBase {

    public static final AxisAlignedBB BEAN_BUCKET_AABB = new AxisAlignedBB(0.25F, 0F, 0.25F, 0.75F, 0.8125F, 0.75F);

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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return BEAN_BUCKET_AABB;
    }
}
