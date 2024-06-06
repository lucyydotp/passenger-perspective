package me.lucyydotp.papers.util;


import net.minecraft.util.Mth;

public class RotationInterpolator {

    public static float lerpDegrees(float f, float start, float end) {
        if (f <= 0.0) return start;
        if (f >= 1.0) return end;
        if (Math.abs(end - start) > 180) {
            return Mth.lerp(f, start, end + 360);
        }
        return Mth.lerp(f, start, end);
    }
}
