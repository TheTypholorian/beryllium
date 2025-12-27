package net.typho.beryllium.mixin.goats;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Goat.class)
public abstract class GoatMixin extends Animal implements PlayerRideableJumping, Saddleable {
    @Unique
    private static final EntityDataAccessor<Boolean> beryllium$saddled = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);

    protected GoatMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isSaddleable() {
        return isAlive() && !isBaby();
    }

    @Override
    public void equipSaddle(@NotNull ItemStack stack, @Nullable SoundSource soundSource) {
        entityData.set(beryllium$saddled, true);

        if (soundSource != null) {
            level().playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5f, 1f);
        }
    }

    @Override
    public boolean isSaddled() {
        return entityData.get(beryllium$saddled);
    }

    @Inject(
            method = "defineSynchedData",
            at = @At("TAIL")
    )
    protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(beryllium$saddled, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsSaddled", isSaddled());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        entityData.set(beryllium$saddled, compound.getBoolean("IsSaddled"));
    }

    @Override
    public void onPlayerJump(int i) {
    }

    @Override
    public boolean canJump() {
        return isSaddled();
    }

    @Override
    public void handleStartJump(int i) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        //setRot(player.getYRot(), player.getXRot() * 0.5f);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!isBaby() && isSaddled()) {
            player.startRiding(this);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        Entity passenger = getFirstPassenger();

        if (passenger instanceof Player player) {
            return player;
        }

        return super.getControllingPassenger();
    }

    @Unique
    @Nullable
    private Vec3 beryllium$getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
        double d = getX() + direction.x;
        double e = getBoundingBox().minY;
        double f = getZ() + direction.z;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (Pose pose : passenger.getDismountPoses()) {
            mutableBlockPos.set(d, e, f);
            double g = getBoundingBox().maxY + 0.75;

            do {
                double h = level().getBlockFloorHeight(mutableBlockPos);
                if ((double)mutableBlockPos.getY() + h > g) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(h)) {
                    AABB aABB = passenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d, (double)mutableBlockPos.getY() + h, f);
                    if (DismountHelper.canDismountTo(level(), passenger, aABB.move(vec3))) {
                        passenger.setPose(pose);
                        return vec3;
                    }
                }

                mutableBlockPos.move(Direction.UP);
            } while (!((double)mutableBlockPos.getY() < g));
        }

        return null;
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector(
                getBbWidth(), passenger.getBbWidth(), getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F)
        );
        Vec3 vec32 = beryllium$getDismountLocationInDirection(vec3, passenger);
        if (vec32 != null) {
            return vec32;
        } else {
            Vec3 vec33 = getCollisionHorizontalEscapeVector(
                    getBbWidth(), passenger.getBbWidth(), getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F)
            );
            Vec3 vec34 = beryllium$getDismountLocationInDirection(vec33, passenger);
            return vec34 != null ? vec34 : position();
        }
    }
}
