package me.lucyydotp.passengerperspective;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public sealed interface CameraPerspectiveMode {

    boolean canContinueToUse();

    void transform(PoseStack cameraPoseStack);

    final class ArmorStandHead implements CameraPerspectiveMode {

        private final ArmorStand armorStand;
        private float lastYRot;

        public ArmorStandHead(ArmorStand armorStand) {
            this.armorStand = armorStand;
            this.lastYRot = armorStand.getYRot();
        }

        public @NotNull ArmorStand armorStand() {
            return armorStand;
        }

        @Override
        public boolean canContinueToUse() {
            return armorStand.isAlive();
        }

        @Override
        public void transform(PoseStack cameraPoseStack) {
            final var player = Minecraft.getInstance().player;
            if (!player.isPassenger()) return;

            final var pose = armorStand.getHeadPose();

            player.turn(
                    (armorStand.getYRot() - lastYRot) / 0.15,
                    0
            );

            lastYRot = armorStand.getYRot();

            cameraPoseStack.rotateAround(
                    new Quaternionf()
                            .rotateAxis((float) Math.toRadians(pose.getZ()), player.getForward().toVector3f())
                            .rotateAxis((float) Math.toRadians(pose.getY()), player.getUpVector(1).toVector3f())
                            .rotateAxis((float) Math.toRadians(-pose.getX()), player.getForward().toVector3f().rotateY((float) (Math.PI / 2f))),
                    0,
                    (float) (-player.getBbHeight() - player.getMyRidingOffset()),
                    0
            );
        }
    }
}
