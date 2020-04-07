package com.currenj.beanblocks.block;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHoleDigger extends BlockBase {

    public static final AxisAlignedBB HOLE_DIGGER_AABB = new AxisAlignedBB(0.375F, 0F, 0.375F, 0.625F, 0.75F, 0.625F);

    public BlockHoleDigger(Material material, String name) {
        super(material, name);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos below = new BlockPos(pos.getX(), pos.getY()-1, pos.getZ());
        if(worldIn.getBlockState(below).getBlock() instanceof BlockDirt){
            worldIn.setBlockState(below, ModBlocks.blockBeanHole.getDefaultState());
            worldIn.destroyBlock(pos, false);
        }
        return true;
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
        return HOLE_DIGGER_AABB;
    }
}
