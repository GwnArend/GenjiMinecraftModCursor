package com.example.genji.events;

import com.example.genji.capability.GenjiData;
import com.example.genji.capability.GenjiDataProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Death-safe tick listener.
 *
 * NOTE:
 * - Previously referenced non-existent GenjiData methods (getBladeBackTicks, etc.) have been removed
 *   to fix compilation. This class now just guards capability access so it cannot crash on death/respawn.
 * - If you want the original "swap back" timer logic, tell me the actual GenjiData field/method names
 *   and I will restore that logic exactly, with the same guard pattern.
 */
@Mod.EventBusSubscriber
public class BladeSwapBack {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        if (!(e.player instanceof ServerPlayer sp)) return;

        // Death/respawn window safe: skip when the capability isn't attached yet
        GenjiData data = GenjiDataProvider.getOrNull(sp);
        if (data == null) return;

        // ------------------------------------------------------------
        // PLACEHOLDER: No-op to keep build stable.
        // Your swap-back logic can go here once we know the real API:
        //   e.g., if (data.isBladeEquipped() && data.getSwapBackAtTick() <= sp.tickCount) { ... }
        // ------------------------------------------------------------
    }
}
