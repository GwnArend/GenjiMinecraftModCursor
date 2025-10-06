package com.example.genji.client.anim;

import net.minecraft.client.Minecraft;

/** Client-only timeline for the 0.25s dash first-person animation. */
public final class FPDashAnim {
    private FPDashAnim() {}

    // dash clip length: 0.25 sec = 5 ticks
    private static final int LENGTH_TICKS = 5;

    private static long startTick = Long.MIN_VALUE;

    /** Called when the dash begins (from packet or cooldown rising-edge). */
    public static void start() {
        startTick = gameTicks();
    }

    /** True while the dash clip should be playing. */
    public static boolean isActive() {
        if (startTick == Long.MIN_VALUE) return false;
        long dt = gameTicks() - startTick;
        if (dt >= 0 && dt < LENGTH_TICKS) return true;
        startTick = Long.MIN_VALUE; // auto-clear after the window
        return false;
    }

    /** True only on the very first tick after .start() (used to force-reset controllers). */
    public static boolean justStarted() {
        return startTick != Long.MIN_VALUE && gameTicks() == startTick;
    }

    private static long gameTicks() {
        var level = Minecraft.getInstance().level;
        return level == null ? 0L : level.getGameTime();
    }
}
