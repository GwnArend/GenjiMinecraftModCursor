package com.example.genji.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class GenjiConfig {
    private GenjiConfig() {}

    public static final ForgeConfigSpec SPEC;

    // ===== CHARGE THRESHOLDS =====
    public static final ForgeConfigSpec.DoubleValue ULT_DAMAGE_FOR_FULL_CHARGE;
    public static final ForgeConfigSpec.DoubleValue NANO_DAMAGE_FOR_FULL_CHARGE;

    // ===== DEFLECT (SECONDS) =====
    public static final ForgeConfigSpec.IntValue DEFLECT_MAX_DURATION_SECONDS;
    public static final ForgeConfigSpec.IntValue DEFLECT_COOLDOWN_SECONDS;

    // ===== DEFLECT GEOMETRY/BEHAVIOR =====
    public static final ForgeConfigSpec.DoubleValue DEFLECT_REACH;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_WIDTH;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_CONE_DOT_MIN;
    public static final ForgeConfigSpec.IntValue    DEFLECT_REREFLECT_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_PING_VOLUME;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_MIN_SPEED_ARROW;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_MIN_SPEED_HURTING;
    public static final ForgeConfigSpec.DoubleValue DEFLECT_MIN_SPEED_DEFAULT;

    // ===== DASH (SECONDS) =====
    public static final ForgeConfigSpec.IntValue DASH_COOLDOWN_SECONDS;

    // ===== DRAGONBLADE (SECONDS) =====
    public static final ForgeConfigSpec.IntValue DRAGONBLADE_DURATION_SECONDS;
    public static final ForgeConfigSpec.IntValue DRAGONBLADE_CAST_SECONDS;
    public static final ForgeConfigSpec.IntValue DRAGONBLADE_SHEATHE_SECONDS;

    // ===== DRAGONBLADE COMBAT =====
    public static final ForgeConfigSpec.IntValue    DRAGONBLADE_COMBO_WINDOW_TICKS;
    public static final ForgeConfigSpec.DoubleValue DRAGONBLADE_REACH;
    public static final ForgeConfigSpec.DoubleValue DRAGONBLADE_WIDTH;
    public static final ForgeConfigSpec.DoubleValue DRAGONBLADE_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_PER_DRAGONBLADE_SWING;

    // ===== DAMAGE VALUES (Overwatch Scaling) =====
    public static final ForgeConfigSpec.DoubleValue SHURIKEN_DAMAGE_PER_STAR;
    public static final ForgeConfigSpec.DoubleValue DASH_DAMAGE;

    // ===== NANO-BOOST (NEW) =====
    public static final ForgeConfigSpec.IntValue    NANO_DURATION_SECONDS;
    public static final ForgeConfigSpec.DoubleValue NANO_DAMAGE_MULTIPLIER;            // e.g. 2.0 = double dmg
    public static final ForgeConfigSpec.DoubleValue NANO_SLASH_SPEED_MULTIPLIER;       // >1 = faster blade swing timings
    public static final ForgeConfigSpec.DoubleValue NANO_SHURIKEN_FIRERATE_MULTIPLIER; // reserved hook for shuriken cadence
    public static final ForgeConfigSpec.DoubleValue NANO_PITCH_MULTIPLIER;             // 1.0 = unchanged SFX pitch

    // Potion amplifiers (0 = level I). Use -1 to disable some effects.
    public static final ForgeConfigSpec.IntValue NANO_SPEED_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue NANO_RESISTANCE_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue NANO_FIRE_RES_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue NANO_ABSORPTION_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue NANO_INSTANT_HEALTH_AMPLIFIER;

    // ===== DEPRECATED TICK ALIASES (legacy reads) =====
    public static final ForgeConfigSpec.IntValue DEFLECT_MAX_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue DEFLECT_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.IntValue DASH_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.IntValue DRAGONBLADE_DURATION_TICKS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        // Charge
        b.push("Charge");
        ULT_DAMAGE_FOR_FULL_CHARGE  = b.defineInRange("UltimateDamageForFull", 50.0, 1.0, 1_000_000.0);
        NANO_DAMAGE_FOR_FULL_CHARGE = b.defineInRange("NanoDamageForFull",    120.0, 1.0, 1_000_000.0);
        b.pop();

        // Deflect timings (seconds)
        b.push("Deflect");
        DEFLECT_MAX_DURATION_SECONDS = b.defineInRange("DurationSeconds", 2, 0, 600);
        DEFLECT_COOLDOWN_SECONDS     = b.defineInRange("CooldownSeconds", 8, 0, 3600);
        b.pop();

        // Deflect geometry/behavior
        b.push("DeflectGeometry");
        DEFLECT_REACH        = b.defineInRange("Reach",            3.5, 0.0, 64.0);
        DEFLECT_WIDTH        = b.defineInRange("Width",            2.5, 0.0, 64.0);
        DEFLECT_HEIGHT       = b.defineInRange("Height",           2.0, 0.0, 64.0);
        DEFLECT_CONE_DOT_MIN = b.defineInRange("ConeDotMin",       0.35, -1.0, 1.0);
        DEFLECT_REREFLECT_COOLDOWN_TICKS = b.defineInRange("ReReflectCooldownTicks", 6, 0, 200);
        DEFLECT_PING_VOLUME  = b.defineInRange("PingVolume",       0.8, 0.0, 2.0);
        DEFLECT_MIN_SPEED_ARROW   = b.defineInRange("MinSpeed.Arrow",   0.08, 0.0, 10.0);
        DEFLECT_MIN_SPEED_HURTING = b.defineInRange("MinSpeed.Hurting", 0.05, 0.0, 10.0);
        DEFLECT_MIN_SPEED_DEFAULT = b.defineInRange("MinSpeed.Default", 0.06, 0.0, 10.0);
        b.pop();

        // Dash
        b.push("Dash");
        DASH_COOLDOWN_SECONDS = b.defineInRange("CooldownSeconds", 8, 0, 3600);
        b.pop();

        // Dragonblade timings (seconds)
        b.push("Dragonblade");
        DRAGONBLADE_DURATION_SECONDS = b.defineInRange("DurationSeconds", 6, 0, 600);
        DRAGONBLADE_CAST_SECONDS     = b.defineInRange("CastSeconds",     1, 0, 60);
        DRAGONBLADE_SHEATHE_SECONDS  = b.defineInRange("SheatheSeconds",  1, 0, 60);
        b.pop();

        // Dragonblade combat
        b.push("DragonbladeCombat");
        DRAGONBLADE_COMBO_WINDOW_TICKS = b.defineInRange("ComboWindowTicks", 7, 0, 200);
        DRAGONBLADE_REACH   = b.defineInRange("Reach",          3.2, 0.0, 64.0);
        DRAGONBLADE_WIDTH   = b.defineInRange("Width",          2.6, 0.0, 64.0);
        DRAGONBLADE_HEIGHT  = b.defineInRange("Height",         2.4, 0.0, 64.0);
        DAMAGE_PER_DRAGONBLADE_SWING = b.defineInRange("DamagePerSwing", 8.8, 0.0, 1_000_000.0); // Overwatch scaling: 110 HP → 8.8 HP
        b.pop();

        // Damage Values (Overwatch Scaling)
        b.push("DamageValues");
        SHURIKEN_DAMAGE_PER_STAR = b.defineInRange("ShurikenDamagePerStar", 2.16, 0.0, 100.0); // Overwatch: 27 HP → 2.16 HP
        DASH_DAMAGE = b.defineInRange("DashDamage", 4.0, 0.0, 100.0); // Overwatch: 50 HP → 4.0 HP
        b.pop();

        // Nano-Boost
        b.push("NanoBoost");
        NANO_DURATION_SECONDS             = b.defineInRange("DurationSeconds",             9,   1, 600);
        NANO_DAMAGE_MULTIPLIER            = b.defineInRange("DamageMultiplier",           2.0, 1.0, 100.0);
        NANO_SLASH_SPEED_MULTIPLIER       = b.defineInRange("SlashSpeedMultiplier",       1.0, 1.0, 10.0);
        NANO_SHURIKEN_FIRERATE_MULTIPLIER = b.defineInRange("ShurikenFireRateMultiplier", 1.0, 1.0, 10.0);
        NANO_PITCH_MULTIPLIER             = b.defineInRange("PitchMultiplier",            1.15,1.0, 3.0);

        NANO_SPEED_AMPLIFIER          = b.defineInRange("Effects.SpeedAmplifier",          1,  -1, 10);
        NANO_RESISTANCE_AMPLIFIER     = b.defineInRange("Effects.ResistanceAmplifier",     1,  -1, 10);
        NANO_FIRE_RES_AMPLIFIER       = b.defineInRange("Effects.FireResistanceAmplifier", 0,  -1, 10);
        NANO_ABSORPTION_AMPLIFIER     = b.defineInRange("Effects.AbsorptionAmplifier",     1,  -1, 10);
        NANO_INSTANT_HEALTH_AMPLIFIER = b.defineInRange("Effects.InstantHealthAmplifier",  1,   0, 10);
        b.pop();

        // Deprecated tick aliases
        b.push("DeprecatedTickAliases");
        DEFLECT_MAX_DURATION_TICKS = b.defineInRange("DeflectMaxDurationTicks", 2 * 20, 0, 50_000);
        DEFLECT_COOLDOWN_TICKS     = b.defineInRange("DeflectCooldownTicks",    8 * 20, 0, 50_000);
        DASH_COOLDOWN_TICKS        = b.defineInRange("DashCooldownTicks",       8 * 20, 0, 50_000);
        DRAGONBLADE_DURATION_TICKS = b.defineInRange("DragonbladeDurationTicks",6 * 20, 0, 50_000);
        b.pop();

        SPEC = b.build();
    }

    // Helpers
    public static int secToTicks(int seconds) { return seconds <= 0 ? 0 : seconds * 20; }
    public static int secToTicksClamped(ForgeConfigSpec.IntValue secondsValue) { return secToTicks(secondsValue.get()); }
}
