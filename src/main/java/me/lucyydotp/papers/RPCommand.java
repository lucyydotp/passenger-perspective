package me.lucyydotp.papers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RPCommand {

    private static final LiteralArgumentBuilder<FabricClientCommandSource> FOCUS =
            literal("focus")
                    .executes((context) -> {

                        final var player = context.getSource().getPlayer();

                        // Find the closest armour stand with any equipment.
                        final var entities = player.level().getEntitiesOfClass(
                                        ArmorStand.class,
                                        AABB.ofSize(player.position(), 10, 10, 10)
                                )
                                .stream()
                                .filter(e ->
                                        ((List<ItemStack>) e.getArmorSlots())
                                                .stream()
                                                .anyMatch(slot -> slot instanceof ItemStack && !slot.isEmpty())
                                )
                                .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(player)))
                                .toList();

                        if (entities.isEmpty()) {
                            context.getSource().sendError(Translations.translate(Translations.FOCUS_ERROR).withStyle(ChatFormatting.RED));
                            return 0;
                        }

                        PassengerPerspectiveMod.setPerspectiveMode(
                                new CameraPerspectiveMode.ArmorStandHead(entities.get(0))
                        );

                        context.getSource().sendFeedback(Translations.translate(Translations.FOCUS));

                        return 0;
                    });

    private static final LiteralArgumentBuilder<FabricClientCommandSource> CLEAR =
            literal("clear").executes((context) -> {
                PassengerPerspectiveMod.setPerspectiveMode(null);
                context.getSource().sendFeedback(Translations.translate(Translations.CLEAR_FOCUS));
                return 0;
            });

    public static final LiteralArgumentBuilder<FabricClientCommandSource> ROOT =
            literal("papers")
                    .then(FOCUS)
                    .then(CLEAR);
}
