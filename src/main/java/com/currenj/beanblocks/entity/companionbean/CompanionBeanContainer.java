package com.currenj.beanblocks.entity.companionbean;

import com.currenj.beanblocks.block.brandassembler.TileEntityBrandAssembler;
import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.slot.SlotOutputItemHandler;
import com.currenj.beanblocks.slot.SlotWhitelistItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CompanionBeanContainer extends Container {

    private final EntityCompanionBean companionBean;
    IItemHandler companionInventory;

    public CompanionBeanContainer(InventoryPlayer playerInv, final EntityCompanionBean companionBean) {
        companionInventory = companionBean.inventory;
        this.companionBean = companionBean;
        System.out.println("Companion bean opened.");

        //modifier slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotItemHandler(companionInventory, j + i * 3, 62 + j * 18, 19 + i * 18) {
                    @Override
                    public void onSlotChanged() {
                        super.onSlotChanged();
                        companionBean.updateAndPrint();
                    }
                });
            }
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        System.out.println("Companion bean closed.");
        companionBean.lockLocation = false;
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
}
