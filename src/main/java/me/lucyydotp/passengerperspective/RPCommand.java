package me.lucyydotp.passengerperspective;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RPCommand {

    private static final LiteralArgumentBuilder<FabricClientCommandSource> FOCUS =
            literal("focus")
                    .executes((context) -> {

                        final var player = context.getSource().getPlayer();

                        final var entities = player.level.getEntitiesOfClass(
                                        ArmorStand.class,
                                        AABB.ofSize(player.position(), 10, 10, 10)
                                )
                                .stream()
                                .filter(e ->
                                        ((List<ItemStack>) e.getArmorSlots())
                                                .stream()
                                                .anyMatch(slot -> slot instanceof ItemStack && !slot.isEmpty())
                                )
                                .toList();

                        if (entities.isEmpty()) {
                            context.getSource().sendError(Component.literal("Couldn't find an entity to focus on."));
                            return 0;
                        }

                        PassengerPerspectiveMod.setPerspectiveMode(
                                new CameraPerspectiveMode.ArmorStandHead(entities.get(0))
                        );

                        context.getSource().sendFeedback(Component.literal("Now focusing on entity " + entities.get(0).getUUID() + "."));
                        return 0;
                    });

    private static final LiteralArgumentBuilder<FabricClientCommandSource> CLEAR =
            literal("clear").executes((context) -> {
                PassengerPerspectiveMod.setPerspectiveMode(null);
                context.getSource().sendFeedback(Component.literal("Cleared focused entities."));
                return 0;
            });

    public static final LiteralArgumentBuilder<FabricClientCommandSource> ROOT =
            literal("perspective")
                    .then(FOCUS)
                    .then(CLEAR);
}
