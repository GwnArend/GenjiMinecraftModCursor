package com.example.genji.events.client;

import com.example.genji.GenjiMod;
import com.example.genji.client.input.Keybinds;
import com.example.genji.network.ModNetwork;
import com.example.genji.network.packet.C2SActivateNanoBoost;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT)
public final class NanoClientKeys {
    private NanoClientKeys() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        if (Keybinds.NANO != null && Keybinds.NANO.consumeClick()) {
            ModNetwork.CHANNEL.sendToServer(new C2SActivateNanoBoost());
        }
    }
}
