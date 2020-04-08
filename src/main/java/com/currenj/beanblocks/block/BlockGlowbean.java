package com.currenj.beanblocks.block;

import com.currenj.beanblocks.item.ModItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class BlockGlowbean extends BlockBase {

    public BlockGlowbean(Material material, String name) {
        super(material, name);
        setHardness(1F);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHarvestLevel("pickaxe", 0);
        setLightLevel(1.0F);
        setSoundType(SoundType.GLASS);
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return MathHelper.clamp(this.quantityDropped(random) + random.nextInt(fortune + 1), 1, 4);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 2 + random.nextInt(3);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModItems.itemGlowbeanDust;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
}
