package com.currenj.beanblocks.underbean;

/**
 Copyright (C) 2017 by jabelar

 This file is part of jabelar's Minecraft Forge modding examples; as such,
 you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
 */
import java.util.Random;

import com.currenj.beanblocks.block.BlockBean;
import com.currenj.beanblocks.block.BlockGlowbean;
import com.currenj.beanblocks.block.ModBlocks;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

// TODO: Auto-generated Javadoc
public class WorldGenFlowersUnderbean extends WorldGenerator
{

    /**
     * Instantiates a new world gen flowers cloud.
     */
    public WorldGenFlowersUnderbean()
    {
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.gen.feature.WorldGenerator#generate(net.minecraft.world.World, java.util.Random, net.minecraft.util.math.BlockPos)
     */
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        BlockGlowbean flower = ModBlocks.blockGlowbean;

        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < 255) && worldIn.getBlockState(blockpos.down()).getBlock() instanceof BlockBean)
            {
                worldIn.setBlockState(blockpos, flower.getDefaultState(), 2);
            }
        }

        return true;
    }
}
