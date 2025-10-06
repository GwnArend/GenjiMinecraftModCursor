package com.example.genji.events;

import com.example.genji.GenjiMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Server-side player tick hook voor Deflect. */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DeflectServerTicker {
    private DeflectServerTicker() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;

        // Extra safety tegen rare frames (death/logout): niet tick’en als removed of dood.
        if (sp.isRemoved() || !sp.isAlive()) return;

        DeflectCombat.perPlayerTick(sp);
    }
}
