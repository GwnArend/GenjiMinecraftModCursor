package com.example.genji.client.events;

import com.example.genji.GenjiMod;
import com.example.genji.client.DashInterpolation;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles camera locking and smooth interpolation during dash.
 * - Locks all player movement input
 * - Locks camera rotation
 * - Smoothly interpolates camera position
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DashCameraControl {
    private DashCameraControl() {}

    /** Lock all movement input during dash. */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (!DashInterpolation.isActive()) return;
        
        // Zero out all movement inputs
        event.getInput().up = false;
        event.getInput().down = false;
        event.getInput().left = false;
        event.getInput().right = false;
        event.getInput().jumping = false;
        event.getInput().shiftKeyDown = false;
        event.getInput().forwardImpulse = 0;
        event.getInput().leftImpulse = 0;
    }

    /** Lock camera rotation during dash. */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("null")
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!DashInterpolation.isActive()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        // Force camera to locked rotation
        float lockedYaw = DashInterpolation.getLockedYaw();
        float lockedPitch = DashInterpolation.getLockedPitch();
        
        event.setYaw(lockedYaw);
        event.setPitch(lockedPitch);
        
        // Also update player rotation to prevent camera drift (null-checked above)
        mc.player.setYRot(lockedYaw);
        mc.player.setXRot(lockedPitch);
        mc.player.yRotO = lockedYaw;
        mc.player.xRotO = lockedPitch;
    }

    private static boolean wasActiveLast = false;

    /** Override server position updates on client tick. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("null")
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        boolean isActive = DashInterpolation.isActive();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        if (isActive) {
            // Override any server position updates by immediately setting to our interpolated position
            Vec3 smoothPos = DashInterpolation.getCurrentPosition(0.0f);
            if (smoothPos != null) {
                mc.player.setPos(smoothPos);
                mc.player.setDeltaMovement(Vec3.ZERO);
            }
            wasActiveLast = true;
        } else if (wasActiveLast) {
            // Dash just ended - ensure we're at the exact end position
            Vec3 endPos = DashInterpolation.getEndPosition();
            if (endPos != null) {
                mc.player.setPos(endPos);
                mc.player.xOld = endPos.x;
                mc.player.yOld = endPos.y;
                mc.player.zOld = endPos.z;
                mc.player.setDeltaMovement(Vec3.ZERO);
            }
            wasActiveLast = false;
        }
    }

    /** Apply smooth position interpolation on render tick for maximum smoothness. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("null")
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (!DashInterpolation.isActive()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        // Get smoothly interpolated position with partial tick for ultra-smooth rendering
        Vec3 smoothPos = DashInterpolation.getCurrentPosition(event.renderTickTime);
        if (smoothPos == null) return;
        
        // Set both current and old positions to the smooth position to prevent vanilla interpolation
        // This ensures the player renders exactly where we want with no additional smoothing
        mc.player.setPos(smoothPos);
        mc.player.xOld = smoothPos.x;
        mc.player.yOld = smoothPos.y;
        mc.player.zOld = smoothPos.z;
        
        // Reset velocity to prevent physics from interfering
        mc.player.setDeltaMovement(Vec3.ZERO);
    }
}

