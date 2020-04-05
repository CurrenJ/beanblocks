package com.currenj.beanblocks.block.beanpress;

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

public class GUIBeanPress extends GuiContainer {
    private InventoryPlayer playerInv;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(BeanBlocks.modId, "textures/gui/bean_press.png");

    public GUIBeanPress(Container container, InventoryPlayer playerInv) {
        super(container);
        this.playerInv = playerInv;
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

        if(((BeanPressContainer)playerInv.player.openContainer).getTileEntity().isPressing())
        {
            int k = this.getPressTimeLeftScaled(18);
            this.drawTexturedModalRect(this.guiLeft + 103, this.guiTop + 36, 176, 0, 25, k);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.blockBeanPress.getUnlocalizedName() + ".name");
        name = name.substring(0, name.length()-1);
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    private int getPressTimeLeftScaled(int pixels)
    {
        return (100-((BeanPressContainer)playerInv.player.openContainer).getTileEntity().getPressTime()) * pixels / 100;
    }
}
