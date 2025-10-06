package com.example.genji.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Client-side smooth dash interpolation.
 * Provides smooth camera movement instead of teleporting.
 */
public final class DashInterpolation {
    private DashInterpolation() {}

    private static Vec3 startPos = null;
    private static Vec3 endPos = null;
    private static int totalTicks = 0;
    private static long startTime = Long.MIN_VALUE;
    private static long startRealTime = 0; // Real time (millis) for smooth interpolation
    
    // Camera lock - store rotation at start
    private static float lockedYaw = 0;
    private static float lockedPitch = 0;

    /** Called when a dash starts (from S2CStartDash packet). */
    public static void start(Vec3 start, Vec3 end, int duration) {
        startPos = start;
        endPos = end;
        totalTicks = duration;
        startTime = gameTicks();
        startRealTime = System.currentTimeMillis();
        
        // Lock camera to current rotation
        var player = Minecraft.getInstance().player;
        if (player != null) {
            lockedYaw = player.getYRot();
            lockedPitch = player.getXRot();
        }
    }

    /** Returns true while the dash is active. */
    public static boolean isActive() {
        if (startTime == Long.MIN_VALUE || startPos == null) return false;
        long elapsed = gameTicks() - startTime;
        // Add +1 tick buffer to ensure smooth completion
        if (elapsed >= 0 && elapsed <= totalTicks) return true;
        
        // Auto-clear after duration with buffer
        clear();
        return false;
    }

    /** Returns the current interpolated position based on elapsed time. Uses real time for smoothness. */
    public static Vec3 getCurrentPosition(float partialTick) {
        if (!isActive()) return null;
        
        // Use real time (milliseconds) for ultra-smooth interpolation
        long elapsedMillis = System.currentTimeMillis() - startRealTime;
        float durationMillis = totalTicks * 50.0f; // 50ms per tick
        float t = elapsedMillis / durationMillis;
        t = Mth.clamp(t, 0.0f, 1.0f);
        
        // Smooth easing function (ease-in-out-cubic for smooth start AND end)
        float smoothT = easeInOutCubic(t);
        
        return lerp(startPos, endPos, smoothT);
    }

    /** Returns the locked yaw (horizontal rotation) during dash. */
    public static float getLockedYaw() {
        return lockedYaw;
    }

    /** Returns the locked pitch (vertical rotation) during dash. */
    public static float getLockedPitch() {
        return lockedPitch;
    }

    /** Returns the final end position (for ensuring we reach it exactly). */
    public static Vec3 getEndPosition() {
        return endPos;
    }

    private static void clear() {
        startPos = null;
        endPos = null;
        totalTicks = 0;
        startTime = Long.MIN_VALUE;
        startRealTime = 0;
    }

    private static Vec3 lerp(Vec3 a, Vec3 b, float t) {
        return new Vec3(
            Mth.lerp(t, a.x, b.x),
            Mth.lerp(t, a.y, b.y),
            Mth.lerp(t, a.z, b.z)
        );
    }

    /** Ease-in-out cubic for smooth acceleration AND deceleration. */
    private static float easeInOutCubic(float t) {
        if (t < 0.5f) {
            // Ease in (first half)
            return 4.0f * t * t * t;
        } else {
            // Ease out (second half)
            float f = 2.0f * t - 2.0f;
            return 1.0f + 0.5f * f * f * f;
        }
    }

    private static long gameTicks() {
        var level = Minecraft.getInstance().level;
        return level == null ? 0L : level.getGameTime();
    }
}

