package com.currenj.beanblocks;

import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.block.beanrecycler.TileEntityBeanRecycler;
import com.currenj.beanblocks.entity.EntityBean;
import com.currenj.beanblocks.entity.ModEntities;
import com.currenj.beanblocks.entity.RenderBean;
import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.item.filter.recycler.EnumRecyclerFilterVariants;
import com.currenj.beanblocks.proxy.CommonProxy;
import com.currenj.beanblocks.recipe.ModRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

@Mod(modid = BeanBlocks.modId, name = BeanBlocks.name, version = BeanBlocks.version)
public class BeanBlocks {

    public static final String modId = "beanblocks";
    public static final String name = "Beans";
    public static final String version = "1.0.0";

    public static final ItemArmor.ArmorMaterial beanArmor = EnumHelper.addArmorMaterial("BEAN", modId + ":bean_armor", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    @SidedProxy(serverSide = "com.currenj.beanblocks.proxy.CommonProxy", clientSide = "com.currenj.beanblocks.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance(modId)
    public static BeanBlocks instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(name + " is loading!");

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        if (event.getSide() == Side.CLIENT) {
            System.out.println("preInit: Client");
            RenderingRegistry.registerEntityRenderingHandler(EntityBean.class, RenderBean::new);
        }
        LootTableList.register(EntityBean.LOOT);

        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        BeanBlocksSoundHandler.registerSounds();
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        TileEntityBeanRecycler.tryAddForeignModItem("ingotCopper", 0.03, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("ingotTin", 0.03, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("ingotAluminum", 0.03, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("ingotLead", 0.02, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("ingotSilver", 0.02, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});

    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void entityRegistration(RegistryEvent.Register<EntityEntry> event) {
            ModEntities.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void pickupItem(EntityItemPickupEvent event) {

//            EntityPlayer player = event.getEntityPlayer();
//            if (!event.getEntityPlayer().getEntityWorld().isRemote) {
//                ItemStack pandaEggStack = new ItemStack(Items.SPAWN_EGG);
//                ResourceLocation pandaResource = new ResourceLocation("beanblocks", "bean");
//                System.out.println(pandaResource);
//                ItemMonsterPlacer.applyEntityIdToItemStack(pandaEggStack, pandaResource);
//                player.inventory.addItemStackToInventory(pandaEggStack);
//            }
//            System.out.println(event.getItem().getName() + " picked up by " + event.getEntityPlayer().getName());
        }
    }

}
