package com.currenj.beanblocks.block.beanhydrator;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.ModBlocks;
import com.currenj.beanblocks.block.beanpress.BeanPressContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIBeanHydrator extends GuiContainer {
    private InventoryPlayer playerInv;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(BeanBlocks.modId, "textures/gui/bean_hydrator.png");

    public GUIBeanHydrator(Container container, InventoryPlayer playerInv) {
        super(container);
        this.playerInv = playerInv;
        this.ySize = 186;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if(((BeanHydratorContainer)playerInv.player.openContainer).getTileEntity().isPressing())
        {
            int k = this.getPressTimeLeftScaled(14);
            this.drawTexturedModalRect(this.guiLeft + 100, this.guiTop + 44, 176, 0, 15, k);
        }

        int w = this.getWaterScaled(40);
        this.drawTexturedModalRect(this.guiLeft + 65, this.guiTop + 21 + 40 - w, 176, 14, 12, w);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.blockBeanHydrator.getUnlocalizedName() + ".name");
        name = name.substring(0, name.length());
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    private int getPressTimeLeftScaled(int pixels)
    {
        return (100-((BeanHydratorContainer)playerInv.player.openContainer).getTileEntity().getHydrateTime()) * pixels / 100;
    }

    private int getWaterScaled(int pixels)
    {
        int p = (int) ((((BeanHydratorContainer)playerInv.player.openContainer).getTileEntity().getWaterLevel()) * pixels / TileEntityBeanHydrator.getWaterCapacity());
        return (p <= pixels) ? p : pixels;
    }
}
