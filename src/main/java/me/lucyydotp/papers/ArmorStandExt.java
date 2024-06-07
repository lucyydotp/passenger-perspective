package me.lucyydotp.papers;

import net.minecraft.core.Rotations;

public interface ArmorStandExt {
    /**
     * Gets the armour stand's head rotation from the previous tick.
     */
    Rotations papers$lastHeadRot();

    /**
     * Gets whether this armour stand should interpolate movement for its head pose.
     */
    boolean papers$shouldInterpolateHead();

    /**
     * Sets whether this armour stand should interpolate movement for its head pose.
     */
    void papers$shouldInterpolateHead(boolean value);
}
