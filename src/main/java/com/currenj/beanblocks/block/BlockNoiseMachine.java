package com.currenj.beanblocks.block;

import com.currenj.beanblocks.BeanBlocksSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNoiseMachine extends BlockBase {

    public BlockNoiseMachine(Material material, String name, CreativeTabs tab) {
        super(material, name, tab);
        setHardness(0.7F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("axe", 0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            world.playSound(player, pos, BeanBlocksSoundHandler.HARRY_NOISE, SoundCategory.MUSIC, 1.0f, 1.0f);
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
            if (!worldIn.isBlockPowered(pos))
            {
                worldIn.scheduleUpdate(pos, this, 4);
            }
            else if (worldIn.isBlockPowered(pos))
            {
                System.out.println("Powered!");
                System.out.println("Playing sound at [" + pos.getX() + ", " +pos.getY() + ", " + pos.getZ() + "]");
                worldIn.playSound(worldIn.getClosestPlayer((float)pos.getX(), (float)pos.getY(), (float)pos.getZ(), 100, true), pos, BeanBlocksSoundHandler.HARRY_NOISE, SoundCategory.MUSIC, 1.0f, 1.0f);
            }
    }
}
