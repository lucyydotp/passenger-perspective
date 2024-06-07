package me.lucyydotp.papers.mixin;

import me.lucyydotp.papers.ArmorStandExt;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandArmorModel.class)
public abstract class ArmorStandArmorModelMixin extends HumanoidModel<ArmorStand> {

    public ArmorStandArmorModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Inject(
            method = "setupAnim(Lnet/minecraft/world/entity/decoration/ArmorStand;FFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V"
            )
    )
    public void interpolateHeadIfNeeded(ArmorStand armorStand, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (((ArmorStandExt) armorStand).papers$shouldInterpolateHead()) {
            final var partialTickTime = h - (int) h;

            final var lastPose = ((ArmorStandExt) armorStand).papers$lastHeadRot();

            this.head.xRot = (float) Math.toRadians(Mth.rotLerp(
                    partialTickTime,
                    lastPose.getX(),
                    armorStand.getHeadPose().getX()
            ));

            this.head.yRot = (float) Math.toRadians(Mth.rotLerp(
                    partialTickTime,
                    lastPose.getY(),
                    armorStand.getHeadPose().getY()
            ));

            this.head.zRot = (float) Math.toRadians(Mth.rotLerp(
                    partialTickTime,
                    lastPose.getZ(),
                    armorStand.getHeadPose().getZ()
            ));
        }
    }
}
