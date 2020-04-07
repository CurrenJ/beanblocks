package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.BeanBlocks;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityWildBean extends EntityBeanBase {
    public EntityWildBean(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityWildBean(world);
    }

    @Override
    protected ResourceLocation getTexture() {
        return makeTexture(BeanBlocks.modId, "bean");
    }
}
