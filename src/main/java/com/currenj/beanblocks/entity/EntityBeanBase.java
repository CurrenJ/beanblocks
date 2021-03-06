package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.BeanBlocks;
import com.currenj.beanblocks.item.ItemBeanPinto;
import com.currenj.beanblocks.item.ModItems;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class EntityBeanBase extends EntityAnimal
{
    private int warningSoundTicks;
    public static final ResourceLocation LOOT = new ResourceLocation(BeanBlocks.modId, "livingbean");
    protected ResourceLocation beanTexture;

    public EntityBeanBase(World worldIn)
    {
        super(worldIn);
        this.setSize(0.8F, 0.4F);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() instanceof ItemBeanPinto;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        beanTexture = this.getTexture();
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, ModItems.pintoBean, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityBeanBase.AIHurtByTarget());
        this.targetTasks.addTask(2, new EntityBeanBase.AIAttackPlayer());
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    protected SoundEvent getAmbientSound()
    {
        return this.isChild() ? SoundEvents.ENTITY_POLAR_BEAR_BABY_AMBIENT : SoundEvents.ENTITY_POLAR_BEAR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SMALL_SLIME_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SMALL_SLIME_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.BLOCK_SLIME_HIT, 0.15F, 1.0F);
    }

    protected void playWarningSound()
    {
        if (this.warningSoundTicks <= 0)
        {
            this.playSound(SoundEvents.ENTITY_SLIME_JUMP, 1.0F, 1.0F);
            this.warningSoundTicks = 40;
        }
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LOOT;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.warningSoundTicks > 0)
        {
            --this.warningSoundTicks;
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag)
        {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    protected float getWaterSlowDown()
    {
        return 0.98F;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        if (livingdata instanceof EntityBeanBase.GroupData)
        {
            if (((EntityBeanBase.GroupData)livingdata).madeParent)
            {
                this.setGrowingAge(-24000);
            }
        }
        else
        {
            EntityBeanBase.GroupData EntityBean$groupdata = new EntityBeanBase.GroupData();
            EntityBean$groupdata.madeParent = true;
            livingdata = EntityBean$groupdata;
        }

        return livingdata;
    }

    class AIAttackPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AIAttackPlayer()
        {
            super(EntityBeanBase.this, EntityPlayer.class, 20, true, true, (Predicate)null);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (EntityBeanBase.this.isChild())
            {
                return false;
            }
            else
            {
                if (super.shouldExecute())
                {
                    //Child protection
//                    for (EntityBean EntityBean : EntityBean.this.world.getEntitiesWithinAABB(EntityBean.class, EntityBean.this.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D)))
////                    {
////                        if (EntityBean.isChild())
////                        {
////                            return true;
////                        }
////                    }

                    //Upon provocation
                    //return true;
                }
                //Always.
                if((targetEntity != null && targetEntity.getName().equals("Tulip58")))
                    return true;


                EntityBeanBase.this.setAttackTarget((EntityLivingBase)null);
                return false;
            }
        }

        protected double getTargetDistance()
        {
            return super.getTargetDistance() * 0.5D;
        }
    }

    class AIHurtByTarget extends EntityAIHurtByTarget
    {
        public AIHurtByTarget()
        {
            super(EntityBeanBase.this, false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            super.startExecuting();

            if (EntityBeanBase.this.isChild())
            {
                this.alertOthers();
                this.resetTask();
            }
        }

        protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
        {
            if (creatureIn instanceof EntityBeanBase && !creatureIn.isChild())
            {
                super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
            }
        }
    }

    class AIMeleeAttack extends EntityAIAttackMelee
    {
        public AIMeleeAttack()
        {
            super(EntityBeanBase.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_)
        {
            double d0 = this.getAttackReachSqr(p_190102_1_);

            if (p_190102_2_ <= d0 && this.attackTick <= 0)
            {
                this.attackTick = 20;
                this.attacker.attackEntityAsMob(p_190102_1_);
            }
            else if (p_190102_2_ <= d0 * 2.0D)
            {
                if (this.attackTick <= 0)
                {
                    this.attackTick = 20;
                }

                if (this.attackTick <= 10)
                {
                    EntityBeanBase.this.playWarningSound();
                }
            }
            else
            {
                this.attackTick = 20;
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            super.resetTask();
        }

        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        {
            return (double)(4.0F + attackTarget.width);
        }
    }

    class AIPanic extends EntityAIPanic
    {
        public AIPanic()
        {
            super(EntityBeanBase.this, 2.0D);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return !EntityBeanBase.this.isChild() && !EntityBeanBase.this.isBurning() ? false : super.shouldExecute();
        }
    }

    static class GroupData implements IEntityLivingData
    {
        public boolean madeParent;

        private GroupData()
        {
        }
    }

    public static ResourceLocation makeTexture(final String MODID, final String TEXTURE) {
        return new ResourceLocation(MODID + ":textures/entity/" + TEXTURE + ".png");
    }

    protected abstract ResourceLocation getTexture();
}
