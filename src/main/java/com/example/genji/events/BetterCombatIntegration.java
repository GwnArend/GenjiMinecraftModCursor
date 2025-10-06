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
 * Integration with Better Combat mod.
 * Better Combat provides the static katana pose when holding the dragonblade.
 * ALL swing animations and timing are handled by DragonbladeCombat system.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BetterCombatIntegration {

    // NOTE: Better Combat integration is PASSIVE - it only provides the static katana pose.
    // The weapon_attributes/dragonblade.json file tells Better Combat to use katana animations.
    // 
    // We do NOT override any attack events here because that was causing double-toggling
    // of swing direction when hitting mobs (air swings worked, mob hits glitched).
    //
    // DragonbladeCombat.perPlayerTick() is the SINGLE source of truth for:
    // - Swing direction (LEFT/RIGHT)
    // - Combo window timing
    // - Animation triggering
    // - Damage application
}
