package com.example.genji.client;

import com.example.genji.client.anim.FPDeflectAnim;

/**
 * Client mirror of server GenjiData. Updated by S2CSyncGenjiData.
 */
public final class ClientGenjiData {
    private ClientGenjiData() {}

    // Percent meters (0..100)
    public static int ult  = 0;
    public static int nano = 0; // NEW: Nano-Boost

    // Timers / cooldowns (ticks)
    public static int bladeTicks = 0;
    public static int deflectTicks = 0;
    public static int dashCooldown = 0;
    public static int deflectCooldown = 0;
    public static int bladeCastTicks = 0;
    public static int bladeSheatheTicks = 0;
    public static int nanoBoostTicks = 0; // NEW: Nano-Boost active timer

    private static int lastDashCooldown = 0;
    private static int lastDeflectTicks = 0;
    private static boolean suppressSheatheDueToDash = false;

    /**
     * Legacy update signature used by existing packets.
     * Nano is set separately by the packet before calling this.
     */
    public static void update(int u, int b, int d, int dash, int defCd, int cast, int sheathe) {
        boolean dashStartedNow    = (lastDashCooldown == 0 && dash > 0);
        boolean deflectStartedNow = (lastDeflectTicks == 0 && d > 0);
        boolean deflectEndedNow   = (lastDeflectTicks > 0 && d == 0);

        ult = clamp01(u);

        if (dashStartedNow && sheathe > 0) suppressSheatheDueToDash = true;
        // NOTE: Dash animation is now triggered by S2CStartDash packet with correct duration

        if (deflectStartedNow) FPDeflectAnim.start();
        if (deflectEndedNow)   FPDeflectAnim.end();

        bladeCastTicks = Math.max(0, cast);
        bladeTicks = Math.max(0, b);
        deflectTicks = Math.max(0, d);
        dashCooldown = Math.max(0, dash);
        deflectCooldown = Math.max(0, defCd);

        if (sheathe > 0 && suppressSheatheDueToDash) {
            bladeSheatheTicks = 0;
            if (b == 0 && cast == 0) suppressSheatheDueToDash = false;
        } else {
            bladeSheatheTicks = Math.max(0, sheathe);
        }

        lastDashCooldown  = dash;
        lastDeflectTicks  = d;
    }

    public static boolean isBladeActive() { return bladeTicks > 0; }
    public static boolean isSheathing()   { return bladeSheatheTicks > 0; }
    public static boolean isCasting()     { return bladeCastTicks > 0; }
    public static boolean ultReady()      { return ult >= 100 && !isBladeActive() && !isCasting() && !isSheathing(); }
    public static boolean isNanoActive()  { return nanoBoostTicks > 0; }

    private static int clamp01(int v) { return v < 0 ? 0 : Math.min(100, v); }
}
