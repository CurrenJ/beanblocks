package com.currenj.beanblocks;

import com.currenj.beanblocks.block.beanrecycler.TileEntityBeanRecycler;
import com.currenj.beanblocks.entity.EntityBeanBase;
import com.currenj.beanblocks.item.filter.recycler.EnumRecyclerFilterVariants;
import com.currenj.beanblocks.proxy.ClientProxy;
import com.currenj.beanblocks.proxy.CommonProxy;
import com.currenj.beanblocks.recipe.ModRecipes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

        if(event.getSide().isClient()) {
            ClientProxy.registerEntityRenderingHandlers();
        }
        LootTableList.register(EntityBeanBase.LOOT);

        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        BeanBlocksSoundHandler.registerSounds();
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //Foreign metals.
        TileEntityBeanRecycler.tryAddForeignModItem("nuggetCopper", 0.15, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("nuggetTin", 0.15, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("nuggetAluminum", 0.10, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("nuggetLead", 0.08, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});
        TileEntityBeanRecycler.tryAddForeignModItem("nuggetSilver", 0.10, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.GEMFILTER); add(EnumRecyclerFilterVariants.METALFILTER); }});

        //Vanilla and foreign treees.
        TileEntityBeanRecycler.tryAddAllForeignModItems("plankWood", 0.05, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.ORGANICFILTER); }});
        TileEntityBeanRecycler.tryAddAllForeignModItems("logWood", 0.05, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.ORGANICFILTER); }});
        TileEntityBeanRecycler.tryAddAllForeignModItems("treeLeaves", 0.05, new ArrayList<EnumRecyclerFilterVariants>() {{ add(EnumRecyclerFilterVariants.ORGANICFILTER); }});
    }

}
