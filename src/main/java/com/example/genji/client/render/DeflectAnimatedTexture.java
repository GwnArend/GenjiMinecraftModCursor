package com.example.genji.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Handles animated texture frames for the deflect effect.
 * The texture is a 32x320 flipbook with 10 frames at 20 FPS.
 * Each frame is 32x32 pixels.
 */
@OnlyIn(Dist.CLIENT)
public class DeflectAnimatedTexture {
    
    private static final ResourceLocation DEFLECT_TEXTURE = ResourceLocation.fromNamespaceAndPath("genji", "textures/item/deflect/genji_deflect_texture.png");
    private static final int FRAME_COUNT = 10;
    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 32;
    private static final double FRAME_DURATION = 0.05; // 20 FPS = 0.05 seconds per frame
    private static final double TOTAL_ANIMATION_DURATION = FRAME_COUNT * FRAME_DURATION; // 0.5 seconds
    
    /**
     * Get the current frame UV offset for the animated texture.
     * @return UV offset (0.0 to 1.0) for the current frame
     */
    public static double getCurrentFrameUV() {
        if (Minecraft.getInstance().level == null) return 0.0;
        
        double time = Minecraft.getInstance().level.getGameTime() + Minecraft.getInstance().getFrameTime();
        double animationTime = time * FRAME_DURATION;
        int currentFrame = (int) (animationTime % FRAME_COUNT);
        return (double) currentFrame / FRAME_COUNT;
    }
    
    /**
     * Get the texture resource location.
     */
    public static ResourceLocation getTextureLocation() {
        return DEFLECT_TEXTURE;
    }
    
    /**
     * Get the frame count.
     */
    public static int getFrameCount() {
        return FRAME_COUNT;
    }
    
    /**
     * Get the frame dimensions.
     */
    public static int getFrameWidth() {
        return FRAME_WIDTH;
    }
    
    public static int getFrameHeight() {
        return FRAME_HEIGHT;
    }
    
    /**
     * Get the total animation duration in seconds.
     */
    public static double getTotalAnimationDuration() {
        return TOTAL_ANIMATION_DURATION;
    }
}
