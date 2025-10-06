package com.example.genji.registry;

import com.example.genji.GenjiMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GenjiMod.MODID);

    // ===== Dragonblade =====
    public static final RegistryObject<SoundEvent> DRAGONBLADE_START  = reg("dragonblade_start");
    public static final RegistryObject<SoundEvent> DRAGONBLADE_ENDING = reg("dragonblade_ending");
    public static final RegistryObject<SoundEvent> DRAGONBLADE_SLICE  = reg("dragonblade_slice");

    // ===== Deflect =====
    public static final RegistryObject<SoundEvent> DEFLECT_START = reg("deflect_start");
    public static final RegistryObject<SoundEvent> DEFLECT_END   = reg("deflect_end");
    public static final RegistryObject<SoundEvent> DEFLECT_PING  = reg("deflect_ping"); // uniform w/ helper + sounds.json

    // ===== Dash =====
    public static final RegistryObject<SoundEvent> DASH = reg("dash");

    // ===== Shuriken throws =====
    public static final RegistryObject<SoundEvent> SHURIKEN_ATTACK1 = reg("shuriken_attack1");
    public static final RegistryObject<SoundEvent> SHURIKEN_ATTACK2 = reg("shuriken_attack2");

    // ===== Hit sounds =====
    public static final RegistryObject<SoundEvent> SHURIKEN_HIT = reg("shuriken_hit");
    public static final RegistryObject<SoundEvent> SHURIKEN_HIT_NANO = reg("shuriken_hit_nano");
    public static final RegistryObject<SoundEvent> DRAGONBLADE_HIT = reg("dragonblade_hit");
    public static final RegistryObject<SoundEvent> KILL_SOUND = reg("kill_sound");
    public static final RegistryObject<SoundEvent> HEADSHOT_HIT = reg("headshot_hit");

    // ===== Nanoboost =====
    public static final RegistryObject<SoundEvent> NANOBOOST_CAST = reg("nanoboost_cast");

    // --- Helpers ---
    private static RegistryObject<SoundEvent> reg(String id) {
        return SOUND_EVENTS.register(id,
                () -> SoundEvent.createVariableRangeEvent(
                        // Keep same style you already use elsewhere:
                        ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, id)
                )
        );
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
