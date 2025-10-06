package com.example.genji.events;

import com.example.genji.capability.GenjiDataProvider;
import com.example.genji.content.ShurikenEntity;
import com.example.genji.network.ModNetwork;
import com.example.genji.network.packet.S2CShurikenFPAnim;
import com.example.genji.network.packet.S2CPlayerPunchAnim;
import com.example.genji.registry.ModSounds;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Shuriken input + cadence + spawning (Genji-style).
 * - Primary (M1): 3-round burst
 * - Secondary (M2): fan of 3 (±8°)
 * - Ballistics: eye-origin, zero drop, 75 m/s (3.75 b/tick)
 * - Sounds: shuriken_attack1 (randomized by sounds.json) and shuriken_attack2
 * - M2 recovery: 0.68s (13.6 ticks) via 14,14,14,13,13 cadence
 * - FP hand anims: S2CShurikenFPAnim (M1 per-shot, M2 once)
 */
public class ShurikenCombat {

    // ===== Tuning =====
    private static final int   PRIMARY_BURST_SHOTS    = 3;
    private static final int   PRIMARY_SPACING_TICKS  = 2;   // gap inside burst
    private static final int   PRIMARY_RECOVERY_TICKS = 18;  // ~0.90s between actions (primary only)

    // 75 m/s = 3.75 blocks per tick
    private static final float SHOT_SPEED             = 3.75f;

    // Fan spread (secondary)
    private static final float FAN_DEGREES            = 8.0f;

    // ===== Runtime state =====
    private static final Map<UUID, State> STATES = new HashMap<>();
    private static int tickCounter = 0;

    private static class State {
        boolean primaryHeld = false;
        boolean secondaryHeld = false;

        int sharedCd = 0;         // global cooldown gating starts
        int burstShotsLeft = 0;   // shots remaining in current M1 burst
        int nextBurstTick = 0;    // when to fire the next burst shot

        // For precise 0.68s M2 recovery (13.6 ticks): 14,14,14,13,13 repeat
        int secondaryCycle = 0;
    }

    private static State state(ServerPlayer sp) {
        return STATES.computeIfAbsent(sp.getUUID(), id -> new State());
    }

    // === Inputs (called from your input packets) ===
    public static void setPrimaryHeld(ServerPlayer sp, boolean held)   { state(sp).primaryHeld = held; }
    public static void setSecondaryHeld(ServerPlayer sp, boolean held) { state(sp).secondaryHeld = held; }

    // === Server driver (call once per tick from your existing ServerTicks) ===
    public static void serverTick(MinecraftServer server) {
        tickCounter++;

        for (ServerLevel lvl : server.getAllLevels()) {
            for (ServerPlayer sp : lvl.players()) {
                State s = state(sp);

                // HARD STOP while unsheathing, active blade, sheathing, dashing, or deflecting
                var data = GenjiDataProvider.get(sp);
                if (data.isCastingBlade() || data.isBladeActive() || data.isSheathing() 
                        || DashAbility.isDashing(sp) || data.isDeflectActive()) {
                    s.primaryHeld = false;
                    s.secondaryHeld = false;
                    s.burstShotsLeft = 0;
                    if (s.sharedCd > 0) s.sharedCd--;
                    continue;
                }

                if (s.sharedCd > 0) s.sharedCd--;

                // Progress an ongoing burst (primary)
                if (s.burstShotsLeft > 0 && tickCounter >= s.nextBurstTick) {
                    fireOne(sp); // also triggers FP anim packet per shot
                    s.burstShotsLeft--;
                    if (s.burstShotsLeft > 0) {
                        s.nextBurstTick = tickCounter + PRIMARY_SPACING_TICKS;
                    }
                }

                // Start new sequences only if not cooling down
                if (s.sharedCd == 0) {
                    if (s.primaryHeld) {
                        startPrimary(sp, s);
                    } else if (s.secondaryHeld) {
                        startSecondary(sp, s); // also triggers FP anim packet once
                    }
                }
            }
        }
    }

    // ===== Firing logic =====

    private static void startPrimary(ServerPlayer sp, State s) {
        s.burstShotsLeft = PRIMARY_BURST_SHOTS;
        s.nextBurstTick  = tickCounter;              // fire immediately
        s.sharedCd       = PRIMARY_RECOVERY_TICKS;   // primary's own recovery
        sp.level().playSound(
                null,
                sp.blockPosition(),
                ModSounds.SHURIKEN_ATTACK1.get(),    // randomized by sounds.json
                SoundSource.PLAYERS,
                3.5f,
                1.0f
        );
    }

    private static void startSecondary(ServerPlayer sp, State s) {
        fireFan(sp);

        // 0.68s average via 14,14,14,13,13 pattern
        int cd = nextSecondaryRecoveryTicks(s);
        s.sharedCd = cd;

        // Trigger FP hand animation once for the fan
        ModNetwork.CHANNEL.sendTo(
                new S2CShurikenFPAnim(S2CShurikenFPAnim.Type.M2_FAN),
                sp.connection.connection,
                NetworkDirection.PLAY_TO_CLIENT
        );

        // Trigger third-person player single air-punch animation for M2
        ModNetwork.CHANNEL.sendTo(
                new S2CPlayerPunchAnim(S2CPlayerPunchAnim.Type.SINGLE_PUNCH),
                sp.connection.connection,
                NetworkDirection.PLAY_TO_CLIENT
        );

        sp.level().playSound(
                null,
                sp.blockPosition(),
                ModSounds.SHURIKEN_ATTACK2.get(),
                SoundSource.PLAYERS,
                3.5f,
                1.0f
        );
    }

    // === Single shuriken (center) ===
    private static void fireOne(ServerPlayer sp) {
        ServerLevel level = sp.serverLevel();

        // Eye-based spawn; slight push forward to avoid self-hit
        Vec3 look = sp.getLookAngle().normalize();
        Vec3 eye  = sp.getEyePosition();
        Vec3 pos  = eye.add(look.scale(0.15));

        ShurikenEntity proj = new ShurikenEntity(level, sp);
        proj.setNoGravity(true);
        proj.moveTo(pos.x, pos.y, pos.z, sp.getYRot(), sp.getXRot());
        proj.setDeltaMovement(look.scale(SHOT_SPEED));

        level.addFreshEntity(proj);

        // Trigger FP hand animation per shot for M1
        ModNetwork.CHANNEL.sendTo(
                new S2CShurikenFPAnim(S2CShurikenFPAnim.Type.M1_SHOT),
                sp.connection.connection,
                NetworkDirection.PLAY_TO_CLIENT
        );

        // Trigger third-person player air-punch animation for M1 burst
        ModNetwork.CHANNEL.sendTo(
                new S2CPlayerPunchAnim(S2CPlayerPunchAnim.Type.BURST_PUNCH),
                sp.connection.connection,
                NetworkDirection.PLAY_TO_CLIENT
        );
    }

    // === Fan of 3 (left/center/right) ===
    private static void fireFan(ServerPlayer sp) {
        Vec3 look = sp.getLookAngle().normalize();
        Vec3 eye  = sp.getEyePosition();
        Vec3 pos  = eye.add(look.scale(0.15));

        // Add small offsets to prevent shurikens from spawning in the exact same position
        Vec3 centerPos = pos;
        Vec3 yUnit = new Vec3(0, 1, 0);
        Vec3 leftPos   = pos.add(look.cross(yUnit).scale(0.1));  // 0.1 block to the left
        Vec3 rightPos  = pos.add(look.cross(yUnit).scale(-0.1)); // 0.1 block to the right

        // center
        spawnWithDir(sp, centerPos, look);

        // yaw left/right around world Y
        Vec3 left  = yaw(look,  Mth.DEG_TO_RAD * FAN_DEGREES);
        Vec3 right = yaw(look, -Mth.DEG_TO_RAD * FAN_DEGREES);

        spawnWithDir(sp, leftPos, left);
        spawnWithDir(sp, rightPos, right);
    }

    private static void spawnWithDir(ServerPlayer sp, Vec3 pos, Vec3 dirNorm) {
        ServerLevel level = sp.serverLevel();

        ShurikenEntity proj = new ShurikenEntity(level, sp);
        proj.setNoGravity(true);
        proj.moveTo(pos.x, pos.y, pos.z, sp.getYRot(), sp.getXRot());
        proj.setDeltaMovement(dirNorm.normalize().scale(SHOT_SPEED));

        level.addFreshEntity(proj);
    }

    // Rotate vector v around world Y by radians (yaw-only)
    private static Vec3 yaw(Vec3 v, float radians) {
        double c = Math.cos(radians), s = Math.sin(radians);
        double x = v.x * c + v.z * s;
        double z = v.z * c - v.x * s;
        return new Vec3(x, v.y, z).normalize();
    }

    // Return 14,14,14,13,13 (average 13.6 ticks == 0.68s)
    private static int nextSecondaryRecoveryTicks(State s) {
        int idx = s.secondaryCycle % 5;
        s.secondaryCycle++;
        return (idx < 3) ? 14 : 13;
    }
}
