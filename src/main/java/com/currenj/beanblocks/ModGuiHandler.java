package com.currenj.beanblocks;

import com.currenj.beanblocks.block.basicgenerator.BasicGeneratorContainer;
import com.currenj.beanblocks.block.basicgenerator.GUIBasicGenerator;
import com.currenj.beanblocks.block.basicgenerator.TileEntityBasicGenerator;
import com.currenj.beanblocks.block.beanhole.BeanHoleContainer;
import com.currenj.beanblocks.block.beanhole.GUIBeanHole;
import com.currenj.beanblocks.block.beanhole.TileEntityBeanHole;
import com.currenj.beanblocks.block.beanhydrator.BeanHydratorContainer;
import com.currenj.beanblocks.block.beanhydrator.GUIBeanHydrator;
import com.currenj.beanblocks.block.beanhydrator.TileEntityBeanHydrator;
import com.currenj.beanblocks.block.beanpress.BeanPressContainer;
import com.currenj.beanblocks.block.beanpress.GUIBeanPress;
import com.currenj.beanblocks.block.beanpress.TileEntityBeanPress;
import com.currenj.beanblocks.block.beanrecycler.BeanRecyclerContainer;
import com.currenj.beanblocks.block.beanrecycler.GUIBeanRecycler;
import com.currenj.beanblocks.block.beanrecycler.TileEntityBeanRecycler;
import com.currenj.beanblocks.block.brandassembler.BrandAssemblerContainer;
import com.currenj.beanblocks.block.brandassembler.GUIBrandAssembler;
import com.currenj.beanblocks.block.brandassembler.TileEntityBrandAssembler;
import com.currenj.beanblocks.entity.companionbean.CompanionBeanContainer;
import com.currenj.beanblocks.entity.companionbean.EntityCompanionBean;
import com.currenj.beanblocks.entity.companionbean.GUICompanionBean;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ModGuiHandler implements IGuiHandler {
    public static final int BEAN_PRESS = 0;
    public static final int BEAN_RECYCLER = 1;
    public static final int BEAN_HYDRATOR = 2;
    public static final int BEAN_HOLE = 3;
    public static final int BRAND_ASSEMBLER = 4;
    public static final int COMPANION_BEAN = 5;
    public static final int BASIC_GENERATOR = 6;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BEAN_PRESS:
                return new BeanPressContainer(player.inventory, (TileEntityBeanPress) world.getTileEntity(new BlockPos(x, y, z)));
            case BEAN_RECYCLER:
                return new BeanRecyclerContainer(player.inventory, (TileEntityBeanRecycler) world.getTileEntity(new BlockPos(x, y, z)));
            case BEAN_HYDRATOR:
                return new BeanHydratorContainer(player.inventory, (TileEntityBeanHydrator) world.getTileEntity(new BlockPos(x, y, z)));
            case BEAN_HOLE:
                return new BeanHoleContainer(player.inventory, (TileEntityBeanHole) world.getTileEntity(new BlockPos(x, y, z)));
            case BRAND_ASSEMBLER:
                return new BrandAssemblerContainer(player.inventory, (TileEntityBrandAssembler) world.getTileEntity(new BlockPos(x, y, z)));
            case COMPANION_BEAN:
                return new CompanionBeanContainer(player.inventory, (EntityCompanionBean)world.getEntityByID(x));
            case BASIC_GENERATOR:
                return new BasicGeneratorContainer(player.inventory, (TileEntityBasicGenerator) world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BEAN_PRESS:
                return new GUIBeanPress(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            case BEAN_RECYCLER:
                return new GUIBeanRecycler(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            case BEAN_HYDRATOR:
                return new GUIBeanHydrator(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            case BEAN_HOLE:
                return new GUIBeanHole(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            case BRAND_ASSEMBLER:
                return new GUIBrandAssembler(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            case COMPANION_BEAN:
                return new GUICompanionBean(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            case BASIC_GENERATOR:
                return new GUIBasicGenerator(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            default:
                return null;
        }
    }
}
