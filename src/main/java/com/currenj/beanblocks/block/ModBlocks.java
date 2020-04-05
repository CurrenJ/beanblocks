package com.currenj.beanblocks.block;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucketFull;
import com.currenj.beanblocks.block.beanpress.BlockBeanPress;
import com.currenj.beanblocks.block.beanrecycler.BlockBeanRecycler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ModBlocks {

    public static BlockBean beanBlock = new BlockBean(Material.SPONGE, "bean_block", CreativeTabs.BUILDING_BLOCKS);
    public static BlockBeanHardened beanBlockHardened = new BlockBeanHardened(Material.SPONGE, "bean_block_hardened");
    public static BlockBeanPolished beanBlockPolished = new BlockBeanPolished(Material.SPONGE, "bean_block_polished");

    public static BlockCropPinto blockCropPinto = new BlockCropPinto();
    public static BlockBeanBucket blockBeanBucket = new BlockBeanBucket();
    public static BlockBeanBucketFull blockBeanBucketFull = new BlockBeanBucketFull();
    public static BlockBeanPress blockBeanPress = new BlockBeanPress();
    public static BlockBeanRecycler blockBeanRecycler = new BlockBeanRecycler();
    public static BlockNoiseMachine blockNoiseMachine = new BlockNoiseMachine(Material.ROCK, "noise_machine", CreativeTabs.BUILDING_BLOCKS);

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                beanBlock,
                beanBlockHardened,
                beanBlockPolished,
                blockCropPinto,
                blockBeanBucket,
                blockBeanBucketFull,
                blockBeanPress,
                blockBeanRecycler,
                blockNoiseMachine
        );

        registerTileEntity(blockBeanPress.getTileEntityClass(), "bean_press");
        registerTileEntity(blockBeanRecycler.getTileEntityClass(), "bean_recycler");
        registerTileEntity(blockBeanBucketFull.getTileEntityClass(), "bean_bucket_full");
    }

    private static void registerTileEntity(@Nonnull final Class<? extends TileEntity> clazz, @Nonnull final String name) {
        GameRegistry.registerTileEntity(clazz, new ResourceLocation(BeanBlocks.modId, name));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                beanBlock.createItemBlock(),
                beanBlockHardened.createItemBlock(),
                beanBlockPolished.createItemBlock(),
                blockBeanBucket.createItemBlock(),
                blockBeanBucketFull.createItemBlock(),
                blockBeanPress.createItemBlock(),
                blockBeanRecycler.createItemBlock(),
                blockNoiseMachine.createItemBlock()
        );
    }

    public static void registerModels() {
        beanBlock.registerItemModel(Item.getItemFromBlock(beanBlock));
        beanBlockHardened.registerItemModel(Item.getItemFromBlock(beanBlockHardened));
        beanBlockPolished.registerItemModel(Item.getItemFromBlock(beanBlockPolished));
        blockBeanBucket.registerItemModel(Item.getItemFromBlock(blockBeanBucket));
        blockBeanBucketFull.registerItemModel(Item.getItemFromBlock(blockBeanBucketFull));
        blockBeanPress.registerItemModel(Item.getItemFromBlock(blockBeanPress));
        blockBeanRecycler.registerItemModel(Item.getItemFromBlock(blockBeanRecycler));
        blockNoiseMachine.registerItemModel(Item.getItemFromBlock(blockNoiseMachine));
    }

}