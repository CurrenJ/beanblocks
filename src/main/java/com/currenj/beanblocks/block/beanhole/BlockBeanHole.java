package com.currenj.beanblocks.block.beanhole;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.BlockTileEntity;
import com.currenj.beanblocks.block.beanpress.TileEntityBeanPress;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemBeansCompressed;
import com.currenj.beanblocks.item.ItemDenseBeanBar;
import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBeanHole extends BlockTileEntity<TileEntityBeanHole> {

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityBeanHole tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            player.openGui(BeanBlocks.instance, ModGuiHandler.BEAN_HOLE, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBeanHole tile = getTileEntity(world, pos);
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

    public BlockBeanHole() {
        super(Material.GROUND, "bean_hole");
        setHardness(1.0F);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setSoundType(SoundType.GROUND);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.DIRT);
    }

    @Override
    public Class<TileEntityBeanHole> getTileEntityClass() {
        return TileEntityBeanHole.class;
    }

    @Nullable
    @Override
    public TileEntityBeanHole createTileEntity(World world, IBlockState state) {
        return new TileEntityBeanHole();
    }

}
