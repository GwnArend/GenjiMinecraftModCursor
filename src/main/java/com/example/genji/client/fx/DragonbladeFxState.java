package com.example.genji.client.fx;

import com.example.genji.GenjiMod;
import com.example.genji.client.ClientGenjiData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-only view of FP windows:
 * - unsheath:   from ClientGenjiData.bladeCastTicks
 * - active:     from ClientGenjiData.bladeTicks
 * - sheathe:    short local window after active ends (matches your animation)
 * - grace:      small bridge after unsheath ends to avoid 1-frame vanilla item flash
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DragonbladeFxState {

    public static final int SHEATHE_TICKS_TOTAL    = 16; // ~0.8s
    public static final int UNSHEATH_GRACE_TICKS   = 4;  // bridge to suppress flicker

    private static int sheatheTicks = 0;
    private static int graceTicks   = 0;

    private static boolean wasActive    = false;
    private static boolean wasUnsheath  = false;

    private DragonbladeFxState() {}

    public static boolean isUnsheathing() { return ClientGenjiData.bladeCastTicks > 0; }
    public static boolean isActive()      { return ClientGenjiData.bladeTicks > 0; }
    public static boolean isSheathing()   { return sheatheTicks > 0; }
    public static boolean isGrace()       { return graceTicks > 0; }

    public static boolean shouldRender()  { return isUnsheathing() || isActive() || isSheathing() || isGrace(); }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        boolean unsheath = isUnsheathing();
        boolean active   = isActive();

        // Start sheathe when we just left active (and aren't immediately unsheathing)
        if (!active && !unsheath && wasActive) {
            sheatheTicks = SHEATHE_TICKS_TOTAL;
        }

        // Short bridge right after unsheath ends (prevents 1-frame vanilla item draw)
        if (!unsheath && wasUnsheath && !active) {
            graceTicks = UNSHEATH_GRACE_TICKS;
        }

        if (sheatheTicks > 0) sheatheTicks--;
        if (graceTicks   > 0) graceTicks--;

        wasActive   = active;
        wasUnsheath = unsheath;
    }
}
