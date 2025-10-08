package com.example.genji.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementHelper {
    
    /**
     * Grant an advancement to a player if they don't already have it.
     * @param player The player to grant the advancement to
     * @param advancementId The resource location of the advancement (e.g., "genji:first_dragonblade")
     */
    public static void grantAdvancement(ServerPlayer player, ResourceLocation advancementId) {
        if (player == null || player.server == null) return;
        
        Advancement advancement = player.server.getAdvancements().getAdvancement(advancementId);
        if (advancement == null) return;
        
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) return; // Already has this advancement
        
        // Grant all criteria (for "impossible" trigger advancements)
        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }
}

