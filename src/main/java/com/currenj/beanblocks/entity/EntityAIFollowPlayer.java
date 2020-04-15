package com.currenj.beanblocks.entity;

import com.currenj.beanblocks.entity.companionbean.EntityCompanionBean;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowPlayer extends EntityAIBase
{
    private final EntityCompanionBean entity;
    private EntityLivingBase owner;
    World world;
    private final double followSpeed;
    private final PathNavigate entityPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;

    public EntityAIFollowPlayer(EntityCompanionBean entityIn, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        this.entity = entityIn;
        this.world = entityIn.world;
        this.followSpeed = followSpeedIn;
        this.entityPathfinder = entityIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);

        if (!(entityIn.getNavigator() instanceof PathNavigateGround) && !(entityIn.getNavigator() instanceof PathNavigateFlying))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if(entity.getLinkedPlayerUUID() != null) {
            EntityLivingBase entitylivingbase = this.world.getPlayerEntityByUUID(entity.getLinkedPlayerUUID());

            if (entitylivingbase == null) {
                return false;
            } else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer) entitylivingbase).isSpectator()) {
                return false;
            } else if (this.entity.getDistanceSq(entitylivingbase) < (double) (this.minDist * this.minDist)) {
                return false;
            } else if(!entity.lockLocation){
                this.owner = entitylivingbase;
                System.out.println("Following!");
                return true;
            } else return false;
        } else return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.entityPathfinder.noPath() && this.entity.getDistanceSq(this.owner) > (double)(this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WALKABLE);
        this.entity.setPathPriority(PathNodeType.WALKABLE, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.owner = null;
        this.entityPathfinder.clearPath();
        this.entity.setPathPriority(PathNodeType.WALKABLE, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        this.entity.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.entity.getVerticalFaceSpeed());

            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;

                if (!this.entityPathfinder.tryMoveToEntityLiving(this.owner, this.followSpeed))
                {
                    if (!this.entity.getLeashed() && !this.entity.isRiding())
                    {
                        if (this.entity.getDistanceSq(this.owner) >= 144.0D)
                        {
                            int i = MathHelper.floor(this.owner.posX) - 2;
                            int j = MathHelper.floor(this.owner.posZ) - 2;
                            int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1))
                                    {
                                        this.entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.entity.rotationYaw, this.entity.rotationPitch);
                                        this.entityPathfinder.clearPath();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    protected boolean isTeleportFriendlyBlock(int x, int p_192381_2_, int y, int p_192381_4_, int p_192381_5_)
    {
        BlockPos blockpos = new BlockPos(x + p_192381_4_, y - 1, p_192381_2_ + p_192381_5_);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.entity) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
    }
}
