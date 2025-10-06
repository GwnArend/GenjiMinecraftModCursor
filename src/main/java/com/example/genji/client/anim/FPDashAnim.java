package com.example.genji.client.anim;

import net.minecraft.client.Minecraft;

/** Client-only timeline for the dash first-person animation with variable duration. */
public final class FPDashAnim {
    private FPDashAnim() {}

    private static long startTick = Long.MIN_VALUE;
    private static long endTick = Long.MIN_VALUE;
    private static int durationTicks = 5;
    private static boolean wasJustStarted = false;
    private static long lastTickChecked = Long.MIN_VALUE;

    /** Called when the dash begins with a specific duration (from S2CStartDash packet). */
    public static void start(int duration) {
        long currentTick = gameTicks();
        
        System.out.println("=== FPDashAnim.start() ===");
        System.out.println("Old state - startTick: " + startTick + ", endTick: " + endTick + ", duration: " + durationTicks);
        System.out.println("New dash - duration: " + duration + ", currentTick: " + currentTick);
        
        // Set new state
        durationTicks = Math.max(2, duration);
        startTick = currentTick;
        endTick = currentTick + durationTicks;
        wasJustStarted = true;
        lastTickChecked = currentTick;
        
        System.out.println("Animation started - startTick: " + startTick + ", endTick: " + endTick + ", duration: " + durationTicks);
    }

    /** Called when the dash begins with default duration (legacy support). */
    public static void start() {
        start(5);
    }

    /** True while the dash clip should be playing. */
    public static boolean isActive() {
        if (startTick == Long.MIN_VALUE) return false;
        
        long currentTick = gameTicks();
        
        // Update state once per tick only (prevents multiple-call issues)
        if (currentTick != lastTickChecked) {
            lastTickChecked = currentTick;
            
            // Keep "just started" flag for 2 ticks to ensure animation controllers see it
            if (wasJustStarted && currentTick > startTick + 1) {
                wasJustStarted = false;
                System.out.println("FPDashAnim: Cleared justStarted flag at tick " + currentTick + " (startTick was " + startTick + ")");
            }
            
            // Check if animation should end
            if (currentTick >= endTick) {
                System.out.println("FPDashAnim: Animation ended at tick " + currentTick + " (endTick was " + endTick + ")");
                clear();
                return false;
            }
        }
        
        // Animation is active if we're between start and end
        return currentTick >= startTick && currentTick < endTick;
    }

    /** True only on the very first tick after .start() (used to force-reset controllers). */
    public static boolean justStarted() {
        return wasJustStarted && startTick != Long.MIN_VALUE;
    }

    private static void clear() {
        System.out.println("FPDashAnim: Clearing state");
        startTick = Long.MIN_VALUE;
        endTick = Long.MIN_VALUE;
        wasJustStarted = false;
    }

    private static long gameTicks() {
        var mc = Minecraft.getInstance();
        if (mc.level == null) {
            System.out.println("WARNING: FPDashAnim.gameTicks() called but level is null!");
            return 0L;
        }
        return mc.level.getGameTime();
    }
    
    /** Force clear the animation state (for debugging). */
    public static void forceStop() {
        System.out.println("FPDashAnim.forceStop() called");
        clear();
        durationTicks = 5;
    }
}
