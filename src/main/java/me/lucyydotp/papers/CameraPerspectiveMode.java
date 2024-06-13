package me.lucyydotp.papers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
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
            ((ArmorStandExt) armorStand).papers$shouldInterpolateHead(true);
            this.creationTime = armorStand.level().getGameTime();
            this.lastYRot = armorStand.yHeadRot;
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
            if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) return;

            final var player = Minecraft.getInstance().player;
            if (!player.isPassenger()) return;

            final var lerpedYRot = Mth.rotLerp(tickProgress, armorStand.yRotO, armorStand.getYRot());
            player.setYRot(player.getYRot() + lerpedYRot - lastYRot);
            lastYRot = lerpedYRot;

            final var pose = armorStand.getHeadPose();
            final var lastPose = ((ArmorStandExt) armorStand).papers$lastHeadRot();
            final var forward = Vec3.directionFromRotation(0, lerpedYRot).toVector3f();

            // fixme: properly slerp instead of doing whatever this is
            cameraPoseStack.rotateAround(
                    new Quaternionf()
                            .rotateAxis((float) Math.toRadians(Mth.rotLerp(tickProgress, lastPose.getZ(), pose.getZ())), forward)
                            .rotateLocalY((float) Math.toRadians(Mth.rotLerp(tickProgress, lastPose.getY(), pose.getY())))
                            .rotateAxis((float) Math.toRadians(Mth.rotLerp(tickProgress, lastPose.getX(), pose.getX())), forward.rotateY((float) (Math.PI / -2f))),
                    0,
                    (float) -player.getPassengersRidingOffset(),
                    0
            );
        }

        public boolean shouldGlow(ArmorStand entity) {
            if (!entity.is(armorStand)) return false;
            final var diff = armorStand.level().getGameTime() - creationTime;

            return diff < 5 ||
                    (7 < diff && diff < 12) ||
                    (15 < diff && diff < 20);

        }
    }
}
