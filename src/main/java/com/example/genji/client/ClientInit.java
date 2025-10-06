package com.example.genji.client;

import com.example.genji.GenjiMod;
import com.example.genji.client.input.Keybinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientInit {

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent e) {
        Keybinds.register(e);
    }
}
