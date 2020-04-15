package com.currenj.beanblocks.block.brandassembler;

import com.currenj.beanblocks.item.ModItems;
import com.currenj.beanblocks.item.brand.ItemBeanBrandRod;
import com.currenj.beanblocks.slot.SlotFilterItemHandler;
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
import net.minecraftforge.items.SlotItemHandler;

public class BrandAssemblerContainer extends Container {

    private TileEntityBrandAssembler tileEntityBrandAssembler;

    public BrandAssemblerContainer(InventoryPlayer playerInv, final TileEntityBrandAssembler brandAssembler) {
        IItemHandler rodInventory = brandAssembler.getInventory(TileEntityBrandAssembler.INVENTORIES.ROD);
        IItemHandler headInventory = brandAssembler.getInventory(TileEntityBrandAssembler.INVENTORIES.HEAD);
        IItemHandler bindingInventory = brandAssembler.getInventory(TileEntityBrandAssembler.INVENTORIES.BIND);
        IItemHandler modifiersInventory = brandAssembler.getInventory(TileEntityBrandAssembler.INVENTORIES.MOD);
        IItemHandler outputInventory = brandAssembler.getInventory(TileEntityBrandAssembler.INVENTORIES.OUT);
        IItemHandler outputSlot = brandAssembler.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        tileEntityBrandAssembler = brandAssembler;
        //brand rod slot
        addSlotToContainer(new SlotWhitelistItemHandler(ModItems.itemBeanBrandRod.getClass(), rodInventory, 0, 18, 36) {
            @Override
            public void onSlotChanged() {
                brandAssembler.updateOutput();
                brandAssembler.markDirty();
            }
        });

        //brand head slot
        addSlotToContainer(new SlotWhitelistItemHandler(ModItems.itemBeanBrandHead.getClass(), headInventory, 0, 36, 36) {
            @Override
            public void onSlotChanged() {
                brandAssembler.updateOutput();
                brandAssembler.markDirty();
            }
        });

        //binding agent slot
        addSlotToContainer(new SlotWhitelistItemHandler(ModItems.compressedBeans.getClass(), bindingInventory, 0, 62, 36) {
            @Override
            public void onSlotChanged() {
                brandAssembler.updateOutput();
                brandAssembler.markDirty();
            }
        });

        //output slot
        addSlotToContainer(new SlotOutputItemHandler(outputSlot, 0, 140, 36) {
            @Override
            public void onSlotChanged() {
                brandAssembler.markDirty();
            }

            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
            {
                brandAssembler.updateOutput(stack);
                brandAssembler.takeOutput();
                brandAssembler.setBlockToUpdate();
                return stack;
            }
        });

        //modifier slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                addSlotToContainer(new SlotItemHandler(modifiersInventory, j + i * 2, 90 + j * 18, 18 + i * 18) {
                    @Override
                    public void onSlotChanged() {
                        brandAssembler.updateOutput();
                        brandAssembler.markDirty();
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

    public TileEntityBrandAssembler getTileEntity() {
        return tileEntityBrandAssembler;
    }
}
