package com.example.genji.client.events;

import com.example.genji.GenjiMod;
import com.example.genji.client.input.Keybinds;
import com.example.genji.client.render.DeflectTPSLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-side event handler for adjusting the deflect third-person model position/rotation with keybinds.
 * This is a debug feature to help position the model correctly.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeflectModelAdjustmentHandler {
    
    private static final double POSITION_STEP = 0.05;
    private static final float ROTATION_STEP = 5.0f;
    private static final float SCALE_STEP = 0.05f;
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        boolean changed = false;
        String changeMsg = "Deflect Model: ";
        
        // Position adjustments
        if (Keybinds.DEFLECT_MODEL_UP.isDown()) {
            DeflectTPSLayer.offsetY += POSITION_STEP;
            changed = true;
            changeMsg += String.format("Y=%.2f ", DeflectTPSLayer.offsetY);
        }
        if (Keybinds.DEFLECT_MODEL_DOWN.isDown()) {
            DeflectTPSLayer.offsetY -= POSITION_STEP;
            changed = true;
            changeMsg += String.format("Y=%.2f ", DeflectTPSLayer.offsetY);
        }
        if (Keybinds.DEFLECT_MODEL_LEFT.isDown()) {
            DeflectTPSLayer.offsetX -= POSITION_STEP;
            changed = true;
            changeMsg += String.format("X=%.2f ", DeflectTPSLayer.offsetX);
        }
        if (Keybinds.DEFLECT_MODEL_RIGHT.isDown()) {
            DeflectTPSLayer.offsetX += POSITION_STEP;
            changed = true;
            changeMsg += String.format("X=%.2f ", DeflectTPSLayer.offsetX);
        }
        if (Keybinds.DEFLECT_MODEL_FORWARD.isDown()) {
            DeflectTPSLayer.offsetZ -= POSITION_STEP;
            changed = true;
            changeMsg += String.format("Z=%.2f ", DeflectTPSLayer.offsetZ);
        }
        if (Keybinds.DEFLECT_MODEL_BACKWARD.isDown()) {
            DeflectTPSLayer.offsetZ += POSITION_STEP;
            changed = true;
            changeMsg += String.format("Z=%.2f ", DeflectTPSLayer.offsetZ);
        }
        
        // Rotation adjustments
        if (Keybinds.DEFLECT_MODEL_ROTATE_X_UP.isDown()) {
            DeflectTPSLayer.rotationX += ROTATION_STEP;
            changed = true;
            changeMsg += String.format("RotX=%.1f ", DeflectTPSLayer.rotationX);
        }
        if (Keybinds.DEFLECT_MODEL_ROTATE_X_DOWN.isDown()) {
            DeflectTPSLayer.rotationX -= ROTATION_STEP;
            changed = true;
            changeMsg += String.format("RotX=%.1f ", DeflectTPSLayer.rotationX);
        }
        if (Keybinds.DEFLECT_MODEL_ROTATE_Y_LEFT.isDown()) {
            DeflectTPSLayer.rotationY -= ROTATION_STEP;
            changed = true;
            changeMsg += String.format("RotY=%.1f ", DeflectTPSLayer.rotationY);
        }
        if (Keybinds.DEFLECT_MODEL_ROTATE_Y_RIGHT.isDown()) {
            DeflectTPSLayer.rotationY += ROTATION_STEP;
            changed = true;
            changeMsg += String.format("RotY=%.1f ", DeflectTPSLayer.rotationY);
        }
        if (Keybinds.DEFLECT_MODEL_ROTATE_Z_LEFT.isDown()) {
            DeflectTPSLayer.rotationZ -= ROTATION_STEP;
            changed = true;
            changeMsg += String.format("RotZ=%.1f ", DeflectTPSLayer.rotationZ);
        }
        if (Keybinds.DEFLECT_MODEL_ROTATE_Z_RIGHT.isDown()) {
            DeflectTPSLayer.rotationZ += ROTATION_STEP;
            changed = true;
            changeMsg += String.format("RotZ=%.1f ", DeflectTPSLayer.rotationZ);
        }
        
        // Scale adjustments
        if (Keybinds.DEFLECT_MODEL_SCALE_UP.isDown()) {
            DeflectTPSLayer.scale += SCALE_STEP;
            changed = true;
            changeMsg += String.format("Scale=%.2f ", DeflectTPSLayer.scale);
        }
        if (Keybinds.DEFLECT_MODEL_SCALE_DOWN.isDown()) {
            DeflectTPSLayer.scale = Math.max(0.1f, DeflectTPSLayer.scale - SCALE_STEP);
            changed = true;
            changeMsg += String.format("Scale=%.2f ", DeflectTPSLayer.scale);
        }
        
        // Display feedback to player
        if (changed && mc.player != null) {
            mc.player.displayClientMessage(Component.literal(changeMsg), true);
            
            // Show full summary in chat (not action bar)
            String summary = String.format(
                "Deflect Model Position: X=%.3f, Y=%.3f, Z=%.3f | Rotation: X=%.1f°, Y=%.1f°, Z=%.1f° | Scale=%.3f",
                DeflectTPSLayer.offsetX, DeflectTPSLayer.offsetY, DeflectTPSLayer.offsetZ,
                DeflectTPSLayer.rotationX, DeflectTPSLayer.rotationY, DeflectTPSLayer.rotationZ,
                DeflectTPSLayer.scale
            );
            mc.player.sendSystemMessage(Component.literal(summary));
        }
    }
}