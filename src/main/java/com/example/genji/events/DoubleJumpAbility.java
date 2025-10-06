package com.example.genji.events;

import com.example.genji.GenjiMod;
import com.example.genji.capability.GenjiDataProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Double jump ability for Genji - allows a small hop in mid-air when pressing space.
 * Resets when the player hits the ground.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DoubleJumpAbility {

    // Double jump velocity (small hop)
    private static final double DOUBLE_JUMP_VELOCITY = 0.4;

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;

        sp.getCapability(GenjiDataProvider.CAPABILITY).ifPresent(data -> {
            // Reset double jump when player hits the ground
            if (sp.onGround() && data.isDoubleJumpUsed()) {
                data.resetDoubleJump();
                System.out.println("DOUBLE JUMP: Player " + sp.getName() + " reset double jump (hit ground)");
            }
        });
    }

    /**
     * Server-side method to execute double jump
     */
    public static void executeDoubleJump(ServerPlayer sp) {
        sp.getCapability(GenjiDataProvider.CAPABILITY).ifPresent(data -> {
            System.out.println("DOUBLE JUMP: Checking conditions - onGround=" + sp.onGround() + ", inWater=" + sp.isInWater() + ", passenger=" + sp.isPassenger() + ", doubleJumpUsed=" + data.isDoubleJumpUsed());
            // Check if player is in air and hasn't used double jump yet
            if (!sp.onGround() && !sp.isInWater() && !sp.isPassenger() && !data.isDoubleJumpUsed()) {
                // Apply double jump velocity (small hop)
                sp.setDeltaMovement(sp.getDeltaMovement().x, DOUBLE_JUMP_VELOCITY, sp.getDeltaMovement().z);
                
                // Mark double jump as used
                data.useDoubleJump();
                
                System.out.println("DOUBLE JUMP: Player " + sp.getName() + " used double jump - applied velocity " + DOUBLE_JUMP_VELOCITY);
            } else {
                System.out.println("DOUBLE JUMP: Conditions not met for " + sp.getName());
            }
        });
    }
}
