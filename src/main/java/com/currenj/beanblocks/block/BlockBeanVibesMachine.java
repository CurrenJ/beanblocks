package com.currenj.beanblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockBeanVibesMachine extends BlockBase {

    public BlockBeanVibesMachine(Material material, String name) {
        super(material, name);
        setHardness(1.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int xRange = 4;
        int zRange = 4;

        if(!worldIn.isRemote) {
            for (int x = -xRange; x <= xRange; x++) {
                for (int z = -zRange; z <= zRange; z++) {
                    IBlockState s = worldIn.getBlockState(pos.add(x, 0, z));
                    Block b = s.getBlock();
                    BlockPos bPos = pos.add(x, 0, z);

                    int timer = -1;

                    if (b instanceof IPlantable || b instanceof IGrowable) {
                        timer = 40;
                    } else if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND) {
                        timer = 20;
                    }

                    timer *= 4;

                    if (timer > 0) {
                        if (b.getTickRandomly()) {
                            worldIn.scheduleUpdate(bPos, b, worldIn.rand.nextInt(timer));
                        }
                    }
                }
            }
        } else {
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+0.25+worldIn.rand.nextDouble()/2, pos.getY(), pos.getZ()+0.25+worldIn.rand.nextDouble()/2, 0, 0.25, 0);
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+0.5, pos.getY()+0.25+worldIn.rand.nextDouble()/2, pos.getZ()+0.5, 0.25, 0, 0.25-worldIn.rand.nextDouble()/2);
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+0.5, pos.getY()+0.25+worldIn.rand.nextDouble()/2, pos.getZ()+0.5, -0.25, 0, 0.25-worldIn.rand.nextDouble()/2);
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+0.5, pos.getY()+0.25+worldIn.rand.nextDouble()/2, pos.getZ()+0.5, 0.25-worldIn.rand.nextDouble()/2, 0, 0.25);
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+0.5, pos.getY()+0.25+worldIn.rand.nextDouble()/2, pos.getZ()+0.5, 0.25-worldIn.rand.nextDouble()/2, 0, -0.25);
        }
        return true;
    }
}
