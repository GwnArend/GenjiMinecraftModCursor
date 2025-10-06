package com.example.genji.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Single source of truth for attach/clone.
 * GenjiMod registers: MinecraftForge.EVENT_BUS.register(new GenjiDataAttach());
 */
public class GenjiDataAttach {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof Player) {
            // Attach exactly once, with the same key as used for persistence
            e.addCapability(GenjiDataProvider.ID, new GenjiDataProvider());
        }
    }

    // Copy on respawn (schema-safe via save/load). Guarded so it never throws.
    @SubscribeEvent
    public void onClone(PlayerEvent.Clone e) {
        if (!e.isWasDeath()) return;

        var oldCap = e.getOriginal().getCapability(GenjiDataProvider.CAPABILITY).orElse(null);
        var newCap = e.getEntity().getCapability(GenjiDataProvider.CAPABILITY).orElse(null);
        if (oldCap != null && newCap != null) {
            try {
                newCap.load(oldCap.save());
            } catch (Throwable ignored) {}
        }
    }
}
