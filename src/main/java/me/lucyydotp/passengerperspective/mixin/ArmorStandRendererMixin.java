package me.lucyydotp.passengerperspective.mixin;

import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorStandRenderer.class)
public class ArmorStandRendererMixin {

    @Redirect(
            method = "getRenderType(Lnet/minecraft/world/entity/decoration/ArmorStand;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ArmorStand;isMarker()Z"
            )
    )
    public boolean alwaysRenderMarkers(ArmorStand instance) {
        return false;
    }
}
