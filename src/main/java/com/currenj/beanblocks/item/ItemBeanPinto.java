package com.currenj.beanblocks.item;

import com.currenj.beanblocks.BeanBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBeanPinto extends ItemSeedFood {

    private String oreName;

    public ItemBeanPinto(int healAmount, float saturation, Block crops, String oreName) {
        super(healAmount, saturation, crops, Blocks.FARMLAND);
        setUnlocalizedName("bean_pinto");
        setRegistryName("bean_pinto");
        this.oreName = oreName;
    }

    public void registerItemModel() {
        BeanBlocks.proxy.registerItemRenderer(this, 0, "bean_pinto");
    }

    public void initOreDict() {
        OreDictionary.registerOre(oreName, this);
    }
}
