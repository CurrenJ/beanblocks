package com.currenj.beanblocks.block.beanpress;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.BlockTileEntity;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemBeansCompressed;
import com.currenj.beanblocks.item.ItemDenseBeanBar;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockBeanPress extends BlockTileEntity<TileEntityBeanPress> {

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityBeanPress tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            if (player.isSneaking()) {
                if (heldItem.isEmpty()) {
                    if(itemHandler.getStackInSlot(tile.getFilterSlot()).getCount() > 0)
                        player.setHeldItem(hand, itemHandler.extractItem(tile.getFilterSlot(), 64, false));
                }
                tile.markDirty();
            } else {
                if(heldItem.getItem() instanceof ItemPressFilter)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getFilterSlot(), heldItem, false));
                else if(heldItem.getItem() instanceof ItemBeanPinto)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));
                else if(heldItem.getItem() instanceof ItemBeansCompressed)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));
                else if(Block.getBlockFromItem(heldItem.getItem()) instanceof BlockBean)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));
                else if(heldItem.getItem() instanceof ItemDenseBeanBar)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));

                player.openGui(BeanBlocks.instance, ModGuiHandler.BEAN_PRESS, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBeanPress tile = getTileEntity(world, pos);
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

    public BlockBeanPress() {
        super(Material.ROCK, "bean_press");
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public Class<TileEntityBeanPress> getTileEntityClass() {
        return TileEntityBeanPress.class;
    }

    @Nullable
    @Override
    public TileEntityBeanPress createTileEntity(World world, IBlockState state) {
        return new TileEntityBeanPress();
    }

}
