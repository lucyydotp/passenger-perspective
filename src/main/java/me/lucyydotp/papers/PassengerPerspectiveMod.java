package me.lucyydotp.papers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.jetbrains.annotations.Nullable;

public class PassengerPerspectiveMod implements ClientModInitializer {

    private static @Nullable CameraPerspectiveMode perspectiveMode;

    /**
     * The current active camera perspective mode.
     */
    public static @Nullable CameraPerspectiveMode getPerspectiveMode() {
        if (perspectiveMode == null) {
            return null;
        }
        if (!perspectiveMode.canContinueToUse()) {
            perspectiveMode = null;
        }
        return perspectiveMode;
    }

    /**
     * Sets the current active camera perspective mode.
     * If null, camera is returned to vanilla behaviour.
     */
    public static void setPerspectiveMode(@Nullable CameraPerspectiveMode perspectiveMode) {
        PassengerPerspectiveMod.perspectiveMode = perspectiveMode;
    }

    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            dispatcher.register(RPCommand.ROOT);
        });
    }
}
