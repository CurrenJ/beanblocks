package com.currenj.beanblocks.block.brandassembler;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.ModGuiHandler;
import com.currenj.beanblocks.block.BlockTileEntity;
import com.currenj.beanblocks.block.beanpress.TileEntityBeanPress;
import com.currenj.beanblocks.item.ItemBeanBrandingToolCool;
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

public class BlockBrandAssembler extends BlockTileEntity<TileEntityBrandAssembler> {

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityBrandAssembler tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            if (player.isSneaking()) {

            } else {
                if(heldItem.getItem() instanceof ItemBeanBrandingToolCool){
                    player.setHeldItem(EnumHand.MAIN_HAND, ItemBeanBrandingToolCool.getHotBrand(heldItem, world.getTotalWorldTime()));
                } else player.openGui(BeanBlocks.instance, ModGuiHandler.BRAND_ASSEMBLER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBrandAssembler tile = getTileEntity(world, pos);
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

    public BlockBrandAssembler() {
        super(Material.ROCK, "brand_assembler");
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public Class<TileEntityBrandAssembler> getTileEntityClass() {
        return TileEntityBrandAssembler.class;
    }

    @Nullable
    @Override
    public TileEntityBrandAssembler createTileEntity(World world, IBlockState state) {
        return new TileEntityBrandAssembler();
    }

}
