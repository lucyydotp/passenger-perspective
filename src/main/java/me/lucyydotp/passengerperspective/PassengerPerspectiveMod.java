package me.lucyydotp.passengerperspective;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.jetbrains.annotations.Nullable;

public class PassengerPerspectiveMod implements ClientModInitializer {

    private static @Nullable CameraPerspectiveMode perspectiveMode;

    public static @Nullable CameraPerspectiveMode getPerspectiveMode() {
        if (perspectiveMode == null) {
            return null;
        }
        if (!perspectiveMode.canContinueToUse()) {
            perspectiveMode = null;
        }
        return perspectiveMode;
    }

    public static void setPerspectiveMode(@Nullable CameraPerspectiveMode perspectiveMode) {
        PassengerPerspectiveMod.perspectiveMode = perspectiveMode;
    }

    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            dispatcher.register(RPCommand.ROOT);
        });
    }
}
