package com.example.genji.events;

import com.example.genji.GenjiMod;
import com.example.genji.capability.GenjiDataProvider;
import com.example.genji.content.DragonbladeItem;
import com.example.genji.registry.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Integration with Better Combat's attack system.
 * Intercepts Better Combat's attack events to apply our custom logic.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BetterCombatIntegration {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingAttack(LivingAttackEvent event) {
        // Check if the attacker is a player with dragonblade active
        if (!(event.getSource().getEntity() instanceof ServerPlayer sp)) return;
        
        var data = GenjiDataProvider.get(sp);
        if (!data.isBladeActive()) return;
        
        // Check if the player is holding a dragonblade
        ItemStack mainHand = sp.getMainHandItem();
        if (mainHand.isEmpty() || !(mainHand.getItem() instanceof DragonbladeItem)) return;
        
        // Apply our custom logic when Better Combat triggers an attack
        // Update our timing system
        boolean rightToLeft = data.nextSwingIsRight();
        data.setNextSwingRight(!rightToLeft);
        
        // Let Better Combat handle the rest (animations, damage, sweeping edge)
        // Sound is already played by our main system
    }
}
