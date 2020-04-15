package com.currenj.beanblocks.block;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.basicgenerator.BlockBasicGenerator;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucket;
import com.currenj.beanblocks.block.beanbucket.BlockBeanBucketFull;
import com.currenj.beanblocks.block.beanhole.BlockBeanHole;
import com.currenj.beanblocks.block.beanhydrator.BlockBeanHydrator;
import com.currenj.beanblocks.block.beanpress.BlockBeanPress;
import com.currenj.beanblocks.block.beanrecycler.BlockBeanRecycler;
import com.currenj.beanblocks.block.brandassembler.BlockBrandAssembler;
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
    public static BlockBeanHydrator blockBeanHydrator = new BlockBeanHydrator();
    public static BlockAncientBeans blockAncientBeans = new BlockAncientBeans(Material.ROCK, "ancient_beans");
    public static BlockBeanHole blockBeanHole = new BlockBeanHole();
    public static BlockHoleDigger blockHoleDigger = new BlockHoleDigger(Material.ROCK, "hole_digger");
    public static BlockBeanVibesMachine blockBeanVibesMachine = new BlockBeanVibesMachine(Material.ROCK, "vibes_machine");
    public static BlockGlowbean blockGlowbean = new BlockGlowbean(Material.GLASS, "glowbean");
    public static BlockBrandAssembler blockBrandAssembler = new BlockBrandAssembler();
    public static BlockBasicGenerator blockBasicGenerator = new BlockBasicGenerator();

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
                blockNoiseMachine,
                blockBeanHydrator,
                blockAncientBeans,
                blockBeanHole,
                blockHoleDigger,
                blockBeanVibesMachine,
                blockGlowbean,
                blockBrandAssembler,
                blockBasicGenerator
        );

        registerTileEntity(blockBeanPress.getTileEntityClass(), "bean_press");
        registerTileEntity(blockBeanRecycler.getTileEntityClass(), "bean_recycler");
        registerTileEntity(blockBeanBucketFull.getTileEntityClass(), "bean_bucket_full");
        registerTileEntity(blockBeanHydrator.getTileEntityClass(), "bean_hydrator");
        registerTileEntity(blockBeanHole.getTileEntityClass(), "bean_hole");
        registerTileEntity(blockBrandAssembler.getTileEntityClass(), "brand_assembler");
        registerTileEntity(blockBasicGenerator.getTileEntityClass(), "basic_generator");
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
                blockNoiseMachine.createItemBlock(),
                blockBeanHydrator.createItemBlock(),
                blockAncientBeans.createItemBlock(),
                blockBeanHole.createItemBlock(),
                blockHoleDigger.createItemBlock(),
                blockBeanVibesMachine.createItemBlock(),
                blockGlowbean.createItemBlock(),
                blockBrandAssembler.createItemBlock(),
                blockBasicGenerator.createItemBlock()
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
        blockBeanHydrator.registerItemModel(Item.getItemFromBlock(blockBeanHydrator));
        blockAncientBeans.registerItemModel(Item.getItemFromBlock(blockAncientBeans));
        blockBeanHole.registerItemModel(Item.getItemFromBlock(blockBeanHole));
        blockHoleDigger.registerItemModel(Item.getItemFromBlock(blockHoleDigger));
        blockBeanVibesMachine.registerItemModel(Item.getItemFromBlock(blockBeanVibesMachine));
        blockGlowbean.registerItemModel(Item.getItemFromBlock(blockGlowbean));
        blockBrandAssembler.registerItemModel(Item.getItemFromBlock(blockBrandAssembler));
        blockBasicGenerator.registerItemModel(Item.getItemFromBlock(blockBasicGenerator));
    }

}