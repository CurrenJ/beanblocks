package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.entity.model.ModelBean;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBean<T extends EntityBeanBase> extends RenderLiving<T>
{
    protected ResourceLocation beanTexture;
    private static final ResourceLocation fallbackTexture = new ResourceLocation("beanblocks","textures/entity/bean.png");

    public RenderBean(RenderManager p_i47197_1_)
    {
        super(p_i47197_1_, new ModelBean(), 0.7F);
    }

    protected void bindBeanTexture(final T bean) {
        beanTexture = bean.getTexture();
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(final T bean, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.bindBeanTexture(bean);
        super.doRender(bean, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(final T golem) {
        return this.beanTexture != null ? this.beanTexture : fallbackTexture;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
