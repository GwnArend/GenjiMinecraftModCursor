package com.example.genji.client.anim;

import net.minecraft.client.Minecraft;

/**
 * Client-only timeline for Dragonblade slashes, driven by S2C packets.
 * Both left/right slashes are exactly 0.75s (15 ticks). When the window
 * ends, flags drop automatically so item falls back to idle.
 */
public final class DragonbladeFPAnim {
    private DragonbladeFPAnim() {}

    public enum Dir { NONE, LEFT, RIGHT }

    // 0.75s @ 20 TPS
    private static final int LENGTH_TICKS = 15;

    private static Dir current = Dir.NONE;
    private static long startTick = Long.MIN_VALUE;

    public static void startLeft()  { current = Dir.LEFT;  startTick = gameTicks(); }
    public static void startRight() { current = Dir.RIGHT; startTick = gameTicks(); }

    /** Call every frame to see if LEFT swing is active. */
    public static boolean isLeft()  { return is(Dir.LEFT); }

    /** Call every frame to see if RIGHT swing is active. */
    public static boolean isRight() { return is(Dir.RIGHT); }

    private static boolean is(Dir dir) {
        if (current != dir) return false;
        long now = gameTicks();
        if (startTick == Long.MIN_VALUE) return false;
        long dt = now - startTick;
        if (dt >= 0 && dt < LENGTH_TICKS) return true;

        // Auto-clear when done
        current = Dir.NONE;
        startTick = Long.MIN_VALUE;
        return false;
    }

    private static long gameTicks() {
        var level = Minecraft.getInstance().level;
        return level == null ? 0L : level.getGameTime();
    }
}
