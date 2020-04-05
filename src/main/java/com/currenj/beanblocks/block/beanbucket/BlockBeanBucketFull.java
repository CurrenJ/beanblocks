package com.currenj.beanblocks.block.beanbucket;

import com.currenj.beanblocks.block.BlockTileEntity;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBeanBucketFull extends BlockTileEntity<TileEntityBeanBucketFull> {

    public BlockBeanBucketFull() {
        super(Material.ROCK, "bean_bucket_full");
        setHardness(2);
        setSoundType(SoundType.METAL);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setTickRandomly(true);
        setHarvestLevel("pickaxe", 1);
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        spew(world, pos, state, new Random());
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
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        spew(worldIn, pos, state, rand);
    }

    public void spew(World worldIn, BlockPos pos, IBlockState state, Random rand){
        if(!worldIn.isRemote){
            if(!((TileEntityBeanBucketFull)worldIn.getTileEntity(pos)).isEmpty()) {
                ItemStack item = ((TileEntityBeanBucketFull)worldIn.getTileEntity(pos)).retrieveItem();
                System.out.println("Spewing item " + item.getUnlocalizedName());
                EntityItem itemEntity = new EntityItem((worldIn), pos.getX() + 0.5 + (rand.nextDouble() - 0.5) / 2, pos.getY() + 1, pos.getZ() + 0.5 + (rand.nextDouble() - 0.5) / 2, item);
                double multiplier = 0.3;
                itemEntity.addVelocity((rand.nextDouble() - 0.5) * multiplier, (rand.nextDouble()) * 2.0 * multiplier, (rand.nextDouble() - 0.5) * multiplier);
                worldIn.spawnEntity(itemEntity);

                if(((TileEntityBeanBucketFull)worldIn.getTileEntity(pos)).isEmpty()){
                    worldIn.destroyBlock(pos, false);
                    worldIn.setBlockState(pos, ModBlocks.blockBeanBucket.getDefaultState());
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBeanBucketFull tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (!stack.isEmpty()) {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(item);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return new ItemStack(ModBlocks.blockBeanBucket, 1).getItem();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 1;
    }

    @Override
    public Class<TileEntityBeanBucketFull> getTileEntityClass() {
        return TileEntityBeanBucketFull.class;
    }

    @Nullable
    @Override
    public TileEntityBeanBucketFull createTileEntity(World world, IBlockState state) {
        TileEntityBeanBucketFull tileEntityBeanBucketFull = new TileEntityBeanBucketFull();
        tileEntityBeanBucketFull.setInventory(new ItemStack(ModItems.pintoBean, 9));
        System.out.println(tileEntityBeanBucketFull.retrieveItem().getUnlocalizedName());
        return tileEntityBeanBucketFull;
    }
}
