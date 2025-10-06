package com.example.genji.client.anim;

import net.minecraft.client.Minecraft;

/** Client-only first-person animation state for shuriken throws. */
public final class ShurikenFPAnim {

    public enum Type { M1_SHOT, M2_FAN }

    // timings (ticks @20 TPS)
    private static final float M1_DUR_TICKS = 4.0f;   // ~0.20s quick flick per shot
    private static final float M2_DUR_TICKS = 6.0f;   // ~0.30s wider fan motion

    private static long m1StartTick = Long.MIN_VALUE;
    private static long m2StartTick = Long.MIN_VALUE;

    private ShurikenFPAnim() {}

    public static void play(Type type) {
        long now = gameTicks();
        if (type == Type.M1_SHOT) m1StartTick = now;
        else m2StartTick = now;
    }

    /** t in [0,1] or <0 when inactive */
    public static float progress(Type type, float partialTick) {
        long start = (type == Type.M1_SHOT) ? m1StartTick : m2StartTick;
        if (start == Long.MIN_VALUE) return -1f;

        float dur = (type == Type.M1_SHOT) ? M1_DUR_TICKS : M2_DUR_TICKS;
        float t = (gameTicks() + partialTick - start) / dur;
        return (t >= 0f && t <= 1f) ? t : -1f;
    }

    private static long gameTicks() {
        var level = Minecraft.getInstance().level;
        return level == null ? 0L : level.getGameTime();
    }
}
