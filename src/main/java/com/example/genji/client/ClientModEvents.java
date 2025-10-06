package com.example.genji.client;

import com.example.genji.GenjiMod;
import com.example.genji.client.render.ShurikenRenderer;
import com.example.genji.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GenjiMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Projectile entity renderer (your existing renderer)
            EntityRenderers.register(ModEntities.SHURIKEN.get(), ShurikenRenderer::new);
            // NOTE: Item renderer is attached via ShurikenItem#initializeClient()
        });
    }
}
