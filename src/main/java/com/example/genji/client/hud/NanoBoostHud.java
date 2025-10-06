package com.example.genji.client.hud;

import com.example.genji.GenjiMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Nano is rendered inside the Ultimate circle by UltimateCharge. No separate HUD here.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT)
public final class NanoBoostHud {
    private NanoBoostHud() {}
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        // no-op
    }
}
