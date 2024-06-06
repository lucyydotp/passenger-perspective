package me.lucyydotp.papers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

/**
 * A camera perspective mode.
 */
public sealed interface CameraPerspectiveMode {

    /**
     * Returns false if the perspective mode can no longer be used, if for example the target entity dies or is removed.
     */
    boolean canContinueToUse();

    /**
     * Transforms the camera to apply the perspective mode.
     *
     * @param cameraPoseStack a pose stack to mutate
     */
    void transform(PoseStack cameraPoseStack, float tickProgress, long time);

    /**
     * A perspective mode that derives rotation from an armour stand's body rotation and head pose.
     */
    final class ArmorStandHead implements CameraPerspectiveMode {

        private final ArmorStand armorStand;
        private final long creationTime;
        private float lastYRot;

        public ArmorStandHead(ArmorStand armorStand) {
            this.armorStand = armorStand;
            this.creationTime = armorStand.level.getGameTime();
            this.lastYRot = armorStand.getYRot();
        }

        /**
         * The target armour stand.
         */
        public @NotNull ArmorStand armorStand() {
            return armorStand;
        }

        @Override
        public boolean canContinueToUse() {
            return armorStand.isAlive();
        }

        @Override
        public void transform(PoseStack cameraPoseStack, float tickProgress, long time) {
            final var player = Minecraft.getInstance().player;
            if (!player.isPassenger()) return;

            // fixme: is doing this on the render thread bad?
            player.turn(
                    (armorStand.getYRot() - lastYRot) / 0.15,
                    0
            );

            lastYRot = armorStand.getYRot();

            final var pose = armorStand.getHeadPose();
            final var lastPose = ((ArmorStandExt) armorStand).paper$lastHeadRot();

            cameraPoseStack.rotateAround(
                    new Quaternionf()
                            .rotateAxis((float) Math.toRadians(Mth.lerp(tickProgress, lastPose.getZ(), pose.getZ())), player.getForward().toVector3f())
                            .rotateAxis((float) Math.toRadians(Mth.lerp(tickProgress, lastPose.getY(), pose.getY())), player.getUpVector(1).toVector3f())
                            .rotateAxis((float) Math.toRadians(Mth.lerp(tickProgress, -lastPose.getX(), -pose.getX())), player.getForward().toVector3f().rotateY((float) (Math.PI / 2f))),
                    0,
                    // fixme: this might be a little low?
                    (float) (-player.getBbHeight() - player.getMyRidingOffset()),
                    0
            );
        }

        public boolean shouldGlow(ArmorStand entity) {
            if (!entity.is(armorStand)) return false;
            final var diff = armorStand.level.getGameTime() - creationTime;

            return diff < 4 ||
                    (8 < diff && diff < 12) ||
                    (16 < diff && diff < 20);

        }
    }
}
