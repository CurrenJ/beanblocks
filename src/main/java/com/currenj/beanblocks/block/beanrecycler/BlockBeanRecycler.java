package com.currenj.beanblocks.block.beanrecycler;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.block.BlockTileEntity;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockBeanRecycler extends BlockTileEntity<TileEntityBeanRecycler> implements ITileEntityProvider {

    public static final int beanWasteMax = 36;

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityBeanRecycler tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            if (!player.isSneaking()) {
                if (heldItem.isEmpty()) {
                    if(itemHandler.getStackInSlot(0).getCount() > 0)
                        player.setHeldItem(hand, itemHandler.extractItem(2, 64, false));
                } else {
                    if(heldItem.getItem() instanceof ItemPressFilter)
                        player.setHeldItem(hand, itemHandler.insertItem(1, heldItem, false));
                    else player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
                }
                tile.markDirty();
            } else {
                player.openGui(BeanBlocks.instance, ModGuiHandler.BEAN_RECYCLER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBeanRecycler tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        for(int s = 0; s < itemHandler.getSlots(); s++) {
            ItemStack stack = itemHandler.getStackInSlot(s);
            if (!stack.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                world.spawnEntity(item);
            }
        }

        super.breakBlock(world, pos, state);
    }

    public BlockBeanRecycler() {
        super(Material.ROCK, "bean_recycler");
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public Class<TileEntityBeanRecycler> getTileEntityClass() {
        return TileEntityBeanRecycler.class;
    }

    @Nullable
    @Override
    public TileEntityBeanRecycler createTileEntity(World world, IBlockState state) {
        return new TileEntityBeanRecycler();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBeanRecycler();
    }
}
