package me.lucyydotp.papers.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.lucyydotp.papers.PassengerPerspectiveMod;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Unique
    private float papers$f;

    @Unique
    private long papers$l;

    @Inject(
            method = "renderLevel",
            at = @At("HEAD")
    )
    public void setF(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        papers$f = f;
        papers$l = l;
    }

    /**
     * Applies the active perspective mode's camera transformation.
     */
    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "NEW",
                    target = "org/joml/Matrix3f"
            )
    )
    public void addRidePerspectiveRotation(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        final var perspectiveMode = PassengerPerspectiveMod.getPerspectiveMode();
        if (perspectiveMode != null) {
            perspectiveMode.transform(poseStack, papers$f, papers$l);
        }
    }
}
