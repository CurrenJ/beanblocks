package com.currenj.beanblocks.block.beanhydrator;

import com.currenj.beanblocks.block.beanpress.TileEntityBeanPress;
import com.currenj.beanblocks.slot.SlotFilterItemHandler;
import com.currenj.beanblocks.slot.SlotOutputItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BeanHydratorContainer extends Container {

    private TileEntityBeanHydrator tileEntityBeanHydrator;

    public BeanHydratorContainer(InventoryPlayer playerInv, final TileEntityBeanHydrator beanHydrator) {
        IItemHandler inventory = beanHydrator.getInventory();
        IItemHandler outputSlot = beanHydrator.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        tileEntityBeanHydrator = beanHydrator;
        addSlotToContainer(new SlotItemHandler(inventory, 0, 100, 23) {
            @Override
            public void onSlotChanged() {
                beanHydrator.markDirty();
            }
        });

        addSlotToContainer(new SlotOutputItemHandler(inventory, 1, 100, 62) {
            @Override
            public void onSlotChanged() {
                beanHydrator.markDirty();
            }
        });

        addSlotToContainer(new SlotItemHandler(inventory, 2, 63, 65) {
            @Override
            public void onSlotChanged() {
                beanHydrator.markDirty();
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 162));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            //from container to inventory
            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            //from inventory to container
            else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public TileEntityBeanHydrator getTileEntity() {
        return tileEntityBeanHydrator;
    }
}
