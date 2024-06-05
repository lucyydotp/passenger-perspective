package me.lucyydotp.papers.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.lucyydotp.papers.PassengerPerspectiveMod;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**
     * Applies the active perspective mode's camera transformation.
     */
    @ModifyArg(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;prepareCullFrustum(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;Lorg/joml/Matrix4f;)V",
                    ordinal = 0
            )
    )
    public PoseStack addRidePerspectiveRotation(PoseStack poseStack) {
        final var perspectiveMode = PassengerPerspectiveMod.getPerspectiveMode();
        if (perspectiveMode != null) {
            perspectiveMode.transform(poseStack);
        }
        return poseStack;
    }
}
