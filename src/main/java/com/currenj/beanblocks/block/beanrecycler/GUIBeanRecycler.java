package com.currenj.beanblocks.block.beanrecycler;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.ModBlocks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIBeanRecycler extends GuiContainer {
    private InventoryPlayer playerInv;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(BeanBlocks.modId, "textures/gui/bean_recycler.png");

    public GUIBeanRecycler(Container container, InventoryPlayer playerInv) {
        super(container);
        this.playerInv = playerInv;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if(((BeanRecyclerContainer)playerInv.player.openContainer).getTileEntity().isRecycling())
        {
            int k = this.getRecycleTimeLeftScaled(12);
            this.drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 37, 176, 0, 15, k);
        }

        int w = this.getBeanWasteScaled(16);
        this.drawTexturedModalRect(this.guiLeft + 51, this.guiTop + 17 + 16 - w, 176, 12, 4, w);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.blockBeanRecycler.getUnlocalizedName() + ".name");
        name = name.substring(0, name.length());
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    private int getRecycleTimeLeftScaled(int pixels)
    {
        int maxTime = ((BeanRecyclerContainer)playerInv.player.openContainer).getTileEntity().getMaxRecycleTime();
        return (maxTime-((BeanRecyclerContainer)playerInv.player.openContainer).getTileEntity().getRecycleTime()) * pixels / maxTime;
    }

    private int getBeanWasteScaled(int pixels)
    {
        int p = (int) ((((BeanRecyclerContainer)playerInv.player.openContainer).getTileEntity().getBeanWaste()) * pixels / BlockBeanRecycler.beanWasteMax);
        return (p <= pixels) ? p : pixels;
    }
}
