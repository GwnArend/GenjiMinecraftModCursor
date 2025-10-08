package com.example.genji.client.input;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Central registry for Genji key mappings.
 * ClientInit should call Keybinds.register(event) during RegisterKeyMappingsEvent.
 */
public final class Keybinds {
    private Keybinds() {}

    public static final String CATEGORY = "key.categories.genji"; // shown as "Genji" via lang

    /** Dragonblade (Ultimate) */
    public static KeyMapping BLADE;

    /** Legacy alias some code paths might still reference */
    public static KeyMapping ULTIMATE;

    /** Nano-Boost (new) */
    public static KeyMapping NANO;

    /** Deflect */
    public static KeyMapping DEFLECT;

    /** Dash */
    public static KeyMapping DASH;

    // Debug keybinds for adjusting deflect third-person model position
    public static KeyMapping DEFLECT_MODEL_UP;
    public static KeyMapping DEFLECT_MODEL_DOWN;
    public static KeyMapping DEFLECT_MODEL_LEFT;
    public static KeyMapping DEFLECT_MODEL_RIGHT;
    public static KeyMapping DEFLECT_MODEL_FORWARD;
    public static KeyMapping DEFLECT_MODEL_BACKWARD;
    public static KeyMapping DEFLECT_MODEL_ROTATE_X_UP;
    public static KeyMapping DEFLECT_MODEL_ROTATE_X_DOWN;
    public static KeyMapping DEFLECT_MODEL_ROTATE_Y_LEFT;
    public static KeyMapping DEFLECT_MODEL_ROTATE_Y_RIGHT;
    public static KeyMapping DEFLECT_MODEL_ROTATE_Z_LEFT;
    public static KeyMapping DEFLECT_MODEL_ROTATE_Z_RIGHT;
    public static KeyMapping DEFLECT_MODEL_SCALE_UP;
    public static KeyMapping DEFLECT_MODEL_SCALE_DOWN;

    /** Invoked by ClientInit during the MOD bus RegisterKeyMappingsEvent */
    public static void register(RegisterKeyMappingsEvent e) {
        BLADE    = new KeyMapping("key.genji.blade",   GLFW.GLFW_KEY_V,        CATEGORY);
        ULTIMATE = BLADE; // keep alias working

        NANO     = new KeyMapping("key.genji.nano",    GLFW.GLFW_KEY_B,        CATEGORY);
        DEFLECT  = new KeyMapping("key.genji.deflect", GLFW.GLFW_KEY_Q,        CATEGORY);
        DASH     = new KeyMapping("key.genji.dash",    GLFW.GLFW_KEY_LEFT_ALT, CATEGORY);

        // Debug keybinds for deflect model positioning (using numpad)
        DEFLECT_MODEL_UP = new KeyMapping("key.genji.deflect_model.up", 
            GLFW.GLFW_KEY_KP_8, CATEGORY);
        DEFLECT_MODEL_DOWN = new KeyMapping("key.genji.deflect_model.down", 
            GLFW.GLFW_KEY_KP_2, CATEGORY);
        DEFLECT_MODEL_LEFT = new KeyMapping("key.genji.deflect_model.left", 
            GLFW.GLFW_KEY_KP_4, CATEGORY);
        DEFLECT_MODEL_RIGHT = new KeyMapping("key.genji.deflect_model.right", 
            GLFW.GLFW_KEY_KP_6, CATEGORY);
        DEFLECT_MODEL_FORWARD = new KeyMapping("key.genji.deflect_model.forward", 
            GLFW.GLFW_KEY_KP_9, CATEGORY);
        DEFLECT_MODEL_BACKWARD = new KeyMapping("key.genji.deflect_model.backward", 
            GLFW.GLFW_KEY_KP_3, CATEGORY);
        
        // Rotation keybinds
        DEFLECT_MODEL_ROTATE_X_UP = new KeyMapping("key.genji.deflect_model.rotate_x_up", 
            GLFW.GLFW_KEY_KP_ADD, CATEGORY);
        DEFLECT_MODEL_ROTATE_X_DOWN = new KeyMapping("key.genji.deflect_model.rotate_x_down", 
            GLFW.GLFW_KEY_KP_SUBTRACT, CATEGORY);
        DEFLECT_MODEL_ROTATE_Y_LEFT = new KeyMapping("key.genji.deflect_model.rotate_y_left", 
            GLFW.GLFW_KEY_KP_7, CATEGORY);
        DEFLECT_MODEL_ROTATE_Y_RIGHT = new KeyMapping("key.genji.deflect_model.rotate_y_right", 
            GLFW.GLFW_KEY_KP_1, CATEGORY);
        DEFLECT_MODEL_ROTATE_Z_LEFT = new KeyMapping("key.genji.deflect_model.rotate_z_left", 
            GLFW.GLFW_KEY_LEFT_BRACKET, CATEGORY);
        DEFLECT_MODEL_ROTATE_Z_RIGHT = new KeyMapping("key.genji.deflect_model.rotate_z_right", 
            GLFW.GLFW_KEY_RIGHT_BRACKET, CATEGORY);
        
        // Scale keybinds
        DEFLECT_MODEL_SCALE_UP = new KeyMapping("key.genji.deflect_model.scale_up", 
            GLFW.GLFW_KEY_KP_MULTIPLY, CATEGORY);
        DEFLECT_MODEL_SCALE_DOWN = new KeyMapping("key.genji.deflect_model.scale_down", 
            GLFW.GLFW_KEY_KP_DIVIDE, CATEGORY);

        e.register(BLADE);
        e.register(NANO);
        e.register(DEFLECT);
        e.register(DASH);
        
        // Register debug keybinds
        e.register(DEFLECT_MODEL_UP);
        e.register(DEFLECT_MODEL_DOWN);
        e.register(DEFLECT_MODEL_LEFT);
        e.register(DEFLECT_MODEL_RIGHT);
        e.register(DEFLECT_MODEL_FORWARD);
        e.register(DEFLECT_MODEL_BACKWARD);
        e.register(DEFLECT_MODEL_ROTATE_X_UP);
        e.register(DEFLECT_MODEL_ROTATE_X_DOWN);
        e.register(DEFLECT_MODEL_ROTATE_Y_LEFT);
        e.register(DEFLECT_MODEL_ROTATE_Y_RIGHT);
        e.register(DEFLECT_MODEL_ROTATE_Z_LEFT);
        e.register(DEFLECT_MODEL_ROTATE_Z_RIGHT);
        e.register(DEFLECT_MODEL_SCALE_UP);
        e.register(DEFLECT_MODEL_SCALE_DOWN);
    }
}
