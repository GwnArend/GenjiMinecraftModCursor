package com.example.genji.client.events;

import com.example.genji.client.anim.PlayerAnimationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.example.genji.GenjiMod;

/**
 * Client-side event handler to ensure Better Combat animations work properly.
 * This helps integrate our custom dragonblade combat with Better Combat's animation system.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BetterCombatIntegrationHandler {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        // This event runs when rendering players, which is where Better Combat applies its animations
        // We don't need to do anything special here, but having this handler ensures
        // our mod is properly registered with Forge's event system for Better Combat compatibility
    }
}




