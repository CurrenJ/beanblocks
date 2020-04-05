package com.currenj.beanblocks.block;

import com.currenj.beanblocks.item.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

public class BlockCropPinto extends BlockCrops {

    public BlockCropPinto() {
        setUnlocalizedName("crop_pinto");
        setRegistryName("crop_pinto");
    }

    @Override
    protected Item getSeed() {
        return ModItems.pintoBean;
    }

    @Override
    protected Item getCrop() {
        return ModItems.pintoBean;
    }

}
