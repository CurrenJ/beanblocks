package com.currenj.beanblocks.block.beanhydrator;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.block.BlockTileEntity;
import com.sun.deploy.panel.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.nio.file.Watchable;

public class BlockBeanHydrator extends BlockTileEntity<TileEntityBeanHydrator> {

    public static final PropertyInteger WATER_LEVEL = PropertyInteger.create("waterlevel", 0, 10);;

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityBeanHydrator tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            if (player.isSneaking()) {

            } else {
                if(player.getHeldItemMainhand().getUnlocalizedName().equals(Items.WATER_BUCKET.getUnlocalizedName())){
                    if(getTileEntity(world, pos).fillFromHand(player.getHeldItemMainhand()))
                        player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                }
                else
                    player.openGui(BeanBlocks.instance, ModGuiHandler.BEAN_HYDRATOR, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBeanHydrator tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack[] stacks = {itemHandler.getStackInSlot(0), itemHandler.getStackInSlot(1), itemHandler.getStackInSlot(2)};
        for(ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                world.spawnEntity(item);
            }
        }

        super.breakBlock(world, pos, state);
    }

    public BlockBeanHydrator() {
        super(Material.ROCK, "bean_hydrator");
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getStateFromMeta(meta).withProperty(WATER_LEVEL, 10);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(WATER_LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.blockState.getBaseState().withProperty(WATER_LEVEL, Integer.valueOf(meta));
    }

    @Override
    public Class<TileEntityBeanHydrator> getTileEntityClass() {
        return TileEntityBeanHydrator.class;
    }

    @Nullable
    @Override
    public TileEntityBeanHydrator createTileEntity(World world, IBlockState state) {
        return new TileEntityBeanHydrator();
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
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, WATER_LEVEL);
    }
}

