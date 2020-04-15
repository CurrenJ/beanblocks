package com.currenj.beanblocks.block.basicgenerator;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.block.ModBlocks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIBasicGenerator extends GuiContainer {
    private InventoryPlayer playerInv;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(BeanBlocks.modId, "textures/gui/basic_generator.png");
    private final TileEntityBasicGenerator basicGenerator;

    public GUIBasicGenerator(Container container, InventoryPlayer playerInv) {
        super(container);
        this.playerInv = playerInv;
        this.ySize = 186;
        this.basicGenerator = ((BasicGeneratorContainer) container).getTileEntity();
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

        int w = this.getEnergyScaled(62);
        this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 12 + 62 - w, 176, 62 - w, 16, w);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.blockBasicGenerator.getUnlocalizedName() + ".name");
        name = name.substring(0, name.length());
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    private int getEnergyScaled(int pixels)
    {
        int energyStored = basicGenerator.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN).getEnergyStored();
        int maxEnergyStored = basicGenerator.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN).getMaxEnergyStored();
        int p = (int) (energyStored * pixels / maxEnergyStored);
        return (p <= pixels) ? p : pixels;
    }
}
