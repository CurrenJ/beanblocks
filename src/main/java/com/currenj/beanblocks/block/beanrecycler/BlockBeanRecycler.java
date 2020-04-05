package com.currenj.beanblocks.block.beanrecycler;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.BlockTileEntity;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ItemBeansCompressed;
import com.currenj.beanblocks.item.ItemDenseBeanBar;
import com.currenj.beanblocks.item.filter.press.ItemPressFilter;
import com.currenj.beanblocks.item.filter.recycler.ItemRecyclerFilter;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
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
import java.util.ArrayList;

public class BlockBeanRecycler extends BlockTileEntity<TileEntityBeanRecycler> implements ITileEntityProvider {

    public static final int beanWasteMax = 36;

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityBeanRecycler tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            if (player.isSneaking()) {
                if (heldItem.isEmpty()) {
                    if(itemHandler.getStackInSlot(tile.getFullBucketSlot()).getCount() > 0)
                        player.setHeldItem(hand, itemHandler.extractItem(tile.getFullBucketSlot(), 64, false));
                    else if(itemHandler.getStackInSlot(tile.getFilterSlot()).getCount() > 0)
                        player.setHeldItem(hand, itemHandler.extractItem(tile.getFilterSlot(), 64, false));
                }
                tile.markDirty();
            } else {
                if(heldItem.getItem() instanceof ItemRecyclerFilter)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getFilterSlot(), heldItem, false));
                else if(Block.getBlockFromItem(heldItem.getItem()) instanceof BlockBeanBucket)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getEmptyBucketSlot(), heldItem, false));
                else if(heldItem.getItem() instanceof ItemBeanPinto)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));
                else if(heldItem.getItem() instanceof ItemBeansCompressed)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));
                else if(Block.getBlockFromItem(heldItem.getItem()) instanceof BlockBean)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));
                else if(heldItem.getItem() instanceof ItemDenseBeanBar)
                    player.setHeldItem(hand, itemHandler.insertItem(tile.getBeanSlot(), heldItem, false));

                player.openGui(BeanBlocks.instance, ModGuiHandler.BEAN_RECYCLER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBeanRecycler tile = getTileEntity(world, pos);
        ArrayList<IItemHandler> allInventories = new ArrayList<IItemHandler>();
        allInventories.add(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP));
        allInventories.add(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN));
        for(int i = 0; i < allInventories.size(); i++) {
            IItemHandler itemHandler = allInventories.get(i);
            for (int s = 0; s < itemHandler.getSlots(); s++) {
                ItemStack stack = itemHandler.getStackInSlot(s);
                if (!stack.isEmpty()) {
                    EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    world.spawnEntity(item);
                }
            }
        }

        super.breakBlock(world, pos, state);
    }

    public BlockBeanRecycler() {
        super(Material.ROCK, "bean_recycler");
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
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
