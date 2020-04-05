package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.entity.model.ModelBean;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBean extends RenderLiving<EntityBean>
{
    private static final ResourceLocation BEAN_TEXTURE = new ResourceLocation("beanblocks","textures/entity/bean.png");

    public RenderBean(RenderManager p_i47197_1_)
    {
        super(p_i47197_1_, new ModelBean(), 0.7F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBean entity)
    {
        return BEAN_TEXTURE;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityBean entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityBean entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
