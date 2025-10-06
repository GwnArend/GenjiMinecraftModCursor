package com.example.genji.events;

import com.example.genji.GenjiMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenjiMod.MODID)
public final class ServerTicks {
    private ServerTicks() {}

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        // Drive all server-side ability loops every tick
        ShurikenCombat.serverTick(e.getServer());
        DashAbility.serverTick(e.getServer());     // <= THIS is required for the smooth dash
    }
}
