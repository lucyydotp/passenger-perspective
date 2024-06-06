package me.lucyydotp.papers.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.lucyydotp.papers.ArmorStandExt;
import me.lucyydotp.papers.CameraPerspectiveMode;
import me.lucyydotp.papers.PassengerPerspectiveMod;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends Entity implements ArmorStandExt {

    @Shadow
    @Final
    private static Rotations DEFAULT_HEAD_POSE;

    @Shadow
    private Rotations headPose;

    @Unique
    private Rotations papers$lastHeadRot = DEFAULT_HEAD_POSE;

    public ArmorStandMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new AbstractMethodError("Mixin abstract class constructor");
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void clearLastHeadPose(CallbackInfo ci) {
        papers$lastHeadRot = headPose;
    }

    /**
     * Makes the current target armour stand glow.
     */
    @Override
    public boolean isCurrentlyGlowing() {
        final var perspectiveMode = PassengerPerspectiveMod.getPerspectiveMode();

        //noinspection ConstantValue
        return super.isCurrentlyGlowing() || (
                perspectiveMode instanceof CameraPerspectiveMode.ArmorStandHead ash &&
                        ash.shouldGlow((ArmorStand) (Object) this)
        );
    }

    @Override
    public Rotations paper$lastHeadRot() {
        return papers$lastHeadRot;
    }
}
