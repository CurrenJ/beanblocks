package com.currenj.beanblocks;

import com.currenj.beanblocks.block.beanhydrator.BeanHydratorContainer;
import com.currenj.beanblocks.block.beanhydrator.GUIBeanHydrator;
import com.currenj.beanblocks.block.beanhydrator.TileEntityBeanHydrator;
import com.currenj.beanblocks.block.beanpress.BeanPressContainer;
import com.currenj.beanblocks.block.beanpress.GUIBeanPress;
import com.currenj.beanblocks.block.beanpress.TileEntityBeanPress;
import com.currenj.beanblocks.block.beanrecycler.BeanRecyclerContainer;
import com.currenj.beanblocks.block.beanrecycler.GUIBeanRecycler;
import com.currenj.beanblocks.block.beanrecycler.TileEntityBeanRecycler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
    public static final int BEAN_PRESS = 0;
    public static final int BEAN_RECYCLER = 1;
    public static final int BEAN_HYDRATOR = 2;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BEAN_PRESS:
                return new BeanPressContainer(player.inventory, (TileEntityBeanPress) world.getTileEntity(new BlockPos(x, y, z)));
            case BEAN_RECYCLER:
                return new BeanRecyclerContainer(player.inventory, (TileEntityBeanRecycler) world.getTileEntity(new BlockPos(x, y, z)));
            case BEAN_HYDRATOR:
                return new BeanHydratorContainer(player.inventory, (TileEntityBeanHydrator) world.getTileEntity(new BlockPos(x, y, z)));
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
            default:
                return null;
        }
    }
}
