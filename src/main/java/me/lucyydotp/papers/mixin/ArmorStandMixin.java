package me.lucyydotp.papers.mixin;

import me.lucyydotp.papers.CameraPerspectiveMode;
import me.lucyydotp.papers.PassengerPerspectiveMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends Entity {

    public ArmorStandMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new AbstractMethodError("Mixin abstract class constructor");
    }

    /**
     * Makes the current target armour stand glow.
     */
    @Override
    public boolean isCurrentlyGlowing() {
        final var perspectiveMode = PassengerPerspectiveMod.getPerspectiveMode();

        //noinspection ConstantValue
        return super.isCurrentlyGlowing() || (
                perspectiveMode instanceof CameraPerspectiveMode.ArmorStandHead ash &&
                        ash.shouldGlow((ArmorStand) (Object) this)
        );
    }
}
