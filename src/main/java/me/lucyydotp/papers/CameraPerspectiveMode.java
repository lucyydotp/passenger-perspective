package me.lucyydotp.papers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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

        public ArmorStandHead(ArmorStand armorStand) {
            this.armorStand = armorStand;
            this.creationTime = armorStand.level.getGameTime();
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

            player.setYRot(Mth.rotLerp(tickProgress, armorStand.yRotO, armorStand.getYRot()));

            final var pose = armorStand.getHeadPose();
            final var lastPose = ((ArmorStandExt) armorStand).paper$lastHeadRot();
            final var forward = Vec3.directionFromRotation(0, player.getYRot()).toVector3f();

            cameraPoseStack.rotateAround(
                    new Quaternionf()
                            .rotateAxis((float) Math.toRadians(Mth.rotLerp(tickProgress, lastPose.getZ(), pose.getZ())), forward)
                            .rotateAxis((float) Math.toRadians(Mth.rotLerp(tickProgress, lastPose.getY(), pose.getY())), player.getUpVector(1).toVector3f())
                            .rotateAxis((float) Math.toRadians(Mth.rotLerp(tickProgress, lastPose.getX(), pose.getX())), forward.rotateY((float) (Math.PI / -2f))),
                    0,
                    // fixme: i completely made this value up. it feels okay-ish but isn't right
                    (float) -1,
                    0
            );
        }

        public boolean shouldGlow(ArmorStand entity) {
            if (!entity.is(armorStand)) return false;
            final var diff = armorStand.level.getGameTime() - creationTime;

            return diff < 5 ||
                    (7 < diff && diff < 12) ||
                    (15 < diff && diff < 20);

        }
    }
}
