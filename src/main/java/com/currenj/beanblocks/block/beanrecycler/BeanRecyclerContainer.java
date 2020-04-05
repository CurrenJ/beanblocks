package com.currenj.beanblocks.block.beanrecycler;

import com.currenj.beanblocks.block.beanpress.TileEntityBeanPress;
import com.currenj.beanblocks.block.beanrecycler.slots.SlotRecylerEmptyBucketItemHandler;
import com.currenj.beanblocks.block.beanrecycler.slots.SlotRecylerFilterItemHandler;
import com.currenj.beanblocks.block.beanrecycler.slots.SlotRecylerFullBucketItemHandler;
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

public class BeanRecyclerContainer extends Container {

    private TileEntityBeanRecycler tileEntityBeanRecycler;

    public BeanRecyclerContainer(InventoryPlayer playerInv, final TileEntityBeanRecycler beanRecycler) {
        IItemHandler inventory = beanRecycler.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        tileEntityBeanRecycler = beanRecycler;
        //Input
        addSlotToContainer(new SlotItemHandler(inventory, 0, 26, 15) {
            @Override
            public void onSlotChanged() {
                beanRecycler.setBlockToUpdate();
            }
        });
        //Filter
        addSlotToContainer(new SlotRecylerFilterItemHandler(inventory, 1, 26, 55) {
            @Override
            public void onSlotChanged() {
                beanRecycler.setBlockToUpdate();
            }
        });
        //Empty Buckets
        addSlotToContainer(new SlotRecylerEmptyBucketItemHandler(inventory, 2, 58, 17) {
            @Override
            public void onSlotChanged() {
                beanRecycler.setBlockToUpdate();
            }
        });
        //Full Buckets
        addSlotToContainer(new SlotRecylerFullBucketItemHandler(inventory, 3, 58, 53) {
            @Override
            public void onSlotChanged() {
                beanRecycler.setBlockToUpdate();
            }
        });
        int mySlots = 4;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                addSlotToContainer(new SlotItemHandler(inventory, mySlots, 80 + j * 18, 17 + i * 18));
                mySlots++;
            }
        }

        int playerSlots = 0;
        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, playerSlots, 8 + k * 18, 142));
            playerSlots++;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, playerSlots, 8 + j * 18, 84 + i * 18));
                playerSlots++;
            }
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

    public TileEntityBeanRecycler getTileEntity() {
        return tileEntityBeanRecycler;
    }
}
