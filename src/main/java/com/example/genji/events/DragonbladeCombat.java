package com.example.genji.events;

import com.example.genji.capability.GenjiDataProvider;
import com.example.genji.config.GenjiConfig;
import com.example.genji.content.DragonbladeItem;
import com.example.genji.registry.ModSounds;
import com.example.genji.network.ModNetwork;
import com.example.genji.network.packet.S2CDragonbladeFPAnim;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class DragonbladeCombat {
    private DragonbladeCombat() {}

    private static final Set<UUID> HELD_PRIMARY   = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> HELD_SECONDARY = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> STARTUP_IN_PROGRESS = ConcurrentHashMap.newKeySet();

    private static final Map<UUID, Long> COMBO_WINDOW_UNTIL = new HashMap<>();
    private static final Set<UUID> WAS_READY = new HashSet<>();

    public static void perPlayerTick(ServerPlayer sp) {
        var data = GenjiDataProvider.get(sp);
        if (!data.isBladeActive()) {
            STARTUP_IN_PROGRESS.remove(sp.getUUID());
            return;
        }
        
        System.out.println("DRAGONBLADE: Player " + sp.getName() + " has blade active, ticks remaining: " + data.getBladeTicks()); // Debug log

        // Startup -> land -> recovery
        if (STARTUP_IN_PROGRESS.contains(sp.getUUID()) && data.getSwingStartupTicks() == 0) {
            onSwingLand(sp);
            STARTUP_IN_PROGRESS.remove(sp.getUUID());
            return;
        }

        final UUID id = sp.getUUID();
        final long nowTicks = sp.level().getGameTime();

        final boolean canSwingNow = data.canSwingNow();
        final boolean wasReady = WAS_READY.contains(id);

        final int comboWindow = GenjiConfig.DRAGONBLADE_COMBO_WINDOW_TICKS.get();
        if (canSwingNow && !wasReady) {
            COMBO_WINDOW_UNTIL.put(id, nowTicks + comboWindow);
            WAS_READY.add(id);
        } else if (!canSwingNow && wasReady) {
            WAS_READY.remove(id);
        }

        // Use our custom timing system to trigger attacks
        // Better Combat will handle the animations and damage when we call attack()
        boolean held = HELD_PRIMARY.contains(id) || HELD_SECONDARY.contains(id);
        boolean canSwing = data.canSwingNow();
        System.out.println("DRAGONBLADE: held=" + held + ", canSwing=" + canSwing + ", startupInProgress=" + STARTUP_IN_PROGRESS.contains(id));
        
        if (held && canSwing && STARTUP_IN_PROGRESS.add(id)) {
            System.out.println("DRAGONBLADE: TRIGGERING ATTACK!");
            boolean rightToLeft = data.nextSwingIsRight();
            data.startSwingStartup(rightToLeft);         // keep our timing system
            data.setNextSwingRight(!rightToLeft);

            // Trigger first-person animation
            ModNetwork.CHANNEL.sendTo(
                    new S2CDragonbladeFPAnim(rightToLeft ? S2CDragonbladeFPAnim.Dir.RIGHT : S2CDragonbladeFPAnim.Dir.LEFT),
                    sp.connection.connection,
                    NetworkDirection.PLAY_TO_CLIENT
            );

            // Trigger third-person player swing animation
            sp.swing(sp.getUsedItemHand());
            
            // Trigger attack - Better Combat will handle third-person animations and damage
            triggerAttack(sp);
            
            // Play our custom sound
            float pitch = 0.9f + sp.getRandom().nextFloat() * 0.2f;
            sp.serverLevel().playSound(null, sp, ModSounds.DRAGONBLADE_SLICE.get(), SoundSource.PLAYERS, 1.0f, pitch);
        }
    }

    public static void setPrimaryHeld(ServerPlayer sp, boolean down) {
        if (down) {
            HELD_PRIMARY.add(sp.getUUID());
            System.out.println("DRAGONBLADE: Primary held set to TRUE for " + sp.getName());
        } else {
            HELD_PRIMARY.remove(sp.getUUID());
            System.out.println("DRAGONBLADE: Primary held set to FALSE for " + sp.getName());
        }
    }
    public static void setSecondaryHeld(ServerPlayer sp, boolean down) {
        if (down) {
            HELD_SECONDARY.add(sp.getUUID());
            System.out.println("DRAGONBLADE: Secondary held set to TRUE for " + sp.getName());
        } else {
            HELD_SECONDARY.remove(sp.getUUID());
            System.out.println("DRAGONBLADE: Secondary held set to FALSE for " + sp.getName());
        }
    }

    public static void onSwingLand(ServerPlayer sp) {
        var data  = GenjiDataProvider.get(sp);
        if (!data.isBladeActive()) return;

        boolean lastWasRightToLeft = !data.nextSwingIsRight();
        data.startSwingRecovery(lastWasRightToLeft);

        // Let vanilla/Better Combat handle damage - no custom damage system
        // Vanilla will handle sweeping edge and area damage naturally
    }

    private static void triggerAttack(ServerPlayer sp) {
        // OLD CUSTOM DAMAGE SYSTEM: Trace-based damage in front of player
        // 5 block range, multiple targets, +50% damage with nanoboost
        
        System.out.println("DRAGONBLADE: triggerAttack called for " + sp.getName());
        
        // Get the dragonblade item
        ItemStack mainHand = sp.getMainHandItem();
        if (mainHand.isEmpty() || !(mainHand.getItem() instanceof DragonbladeItem)) {
            System.out.println("DRAGONBLADE: No dragonblade item in main hand!");
            return;
        }
        
        System.out.println("DRAGONBLADE: Found dragonblade item, performing trace-based damage...");
        
        // Trigger third-person swing animation
        System.out.println("DRAGONBLADE: Triggering swing animation for hand: " + sp.getUsedItemHand());
        sp.swing(sp.getUsedItemHand());
        System.out.println("DRAGONBLADE: Swing animation triggered");
        
        // Perform trace-based damage in front of player
        damageInFrontWithLOS(sp);
        
        // Play our custom sound
        float pitch = 0.9f + sp.getRandom().nextFloat() * 0.2f;
        sp.serverLevel().playSound(null, sp, ModSounds.DRAGONBLADE_SLICE.get(), SoundSource.PLAYERS, 1.0f, pitch);
    }
    
    private static void damageInFrontWithLOS(ServerPlayer sp) {
        ServerLevel level = sp.serverLevel();
        Vec3 playerPos = sp.getEyePosition();
        Vec3 lookVec = sp.getLookAngle();
        
        // 5 block range for dragonblade
        double range = 5.0;
        Vec3 endPos = playerPos.add(lookVec.scale(range));
        
        // Get all entities in a cone in front of the player
        AABB searchBox = sp.getBoundingBox().inflate(range, 1.0, range);
        var entities = level.getEntitiesOfClass(LivingEntity.class, searchBox, 
            entity -> entity != sp && entity.isAlive() && !entity.isDeadOrDying() && sp.canAttack(entity));
        
        System.out.println("DRAGONBLADE: Found " + entities.size() + " entities in search area");
        
        // Check nanoboost status for damage multiplier
        var data = GenjiDataProvider.get(sp);
        boolean nanoboostActive = data.isNanoActive();
        float damageMultiplier = nanoboostActive ? 1.5f : 1.0f; // +50% damage with nanoboost
        float baseDamage = 8.8f; // Base dragonblade damage
        float finalDamage = baseDamage * damageMultiplier;
        
        System.out.println("DRAGONBLADE: Base damage: " + baseDamage + ", Multiplier: " + damageMultiplier + ", Final: " + finalDamage);
        
        int hitCount = 0;
        for (LivingEntity entity : entities) {
            Vec3 entityPos = entity.getEyePosition();
            Vec3 toEntity = entityPos.subtract(playerPos);
            double distance = toEntity.length();
            
            // Check if entity is within range
            if (distance > range) continue;
            
            // Check if entity is in front of player (dot product check)
            Vec3 toEntityNormalized = toEntity.normalize();
            double dot = lookVec.dot(toEntityNormalized);
            if (dot < 0.7) continue; // Only consider entities roughly in front
            
            // Line of sight check
            if (level.clip(new net.minecraft.world.level.ClipContext(
                playerPos, entityPos, 
                net.minecraft.world.level.ClipContext.Block.COLLIDER, 
                net.minecraft.world.level.ClipContext.Fluid.NONE, sp)).getType() != 
                net.minecraft.world.phys.HitResult.Type.MISS) {
                continue; // Blocked by something
            }
            
            // Apply damage
            System.out.println("DRAGONBLADE: Hitting " + entity.getName() + " for " + finalDamage + " damage");
            entity.hurt(level.damageSources().playerAttack(sp), finalDamage);
            hitCount++;
        }
        
        System.out.println("DRAGONBLADE: Hit " + hitCount + " targets with trace-based damage");
    }

}
