package com.example.genji.client.hud;

import com.example.genji.GenjiMod;
import com.example.genji.client.ClientGenjiData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Renders the animated nanoboost overlay when nano is active.
 */
@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT)
public final class NanoBoostHud {
    private NanoBoostHud() {}

    private static final ResourceLocation NANOBOOST_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/nanoboost/nanoboost_loop.png");

    private static final int FRAME_COUNT = 13;
    private static final int FRAME_WIDTH = 256;
    private static final int FRAME_HEIGHT = 144;
    private static final float FPS = 10.0f;
    private static final int TICKS_PER_FRAME = Math.max(1, Math.round(20.0f / FPS));

    private static boolean wasActive = false;
    private static long startTick = 0L;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.level == null) {
            wasActive = false;
            return;
        }

        if (!ClientGenjiData.isNanoActive()) {
            wasActive = false;
            return;
        }

        long gameTime = mc.level.getGameTime();
        if (!wasActive) {
            startTick = gameTime;
            wasActive = true;
        }
        long elapsed = Math.max(0L, gameTime - startTick);
        int currentFrame = (int) ((elapsed / TICKS_PER_FRAME) % FRAME_COUNT);

        GuiGraphics g = event.getGuiGraphics();
        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();

        // Force nearest filtering (no blur, no mipmaps) for crisp pixels
        var texObj = mc.getTextureManager().getTexture(NANOBOOST_TEXTURE);
        if (texObj != null) texObj.setFilter(false, false);

        float scale = Math.max(screenWidth / (float) FRAME_WIDTH, screenHeight / (float) FRAME_HEIGHT);
        float drawWidth = FRAME_WIDTH * scale;
        float drawHeight = FRAME_HEIGHT * scale;
        float drawX = (screenWidth - drawWidth) * 0.5f;
        float drawY = (screenHeight - drawHeight) * 0.5f;

        float textureHeight = FRAME_HEIGHT * FRAME_COUNT;
        // Half-texel offsets to avoid sampling across frame borders
        float v0 = (currentFrame * FRAME_HEIGHT + 0.5f) / textureHeight;
        float v1 = (((currentFrame + 1) * FRAME_HEIGHT) - 0.5f) / textureHeight;
        float u0 = (0.5f) / (float) FRAME_WIDTH;
        float u1 = 1.0f - (0.5f) / (float) FRAME_WIDTH;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, NANOBOOST_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        var poseMat = g.pose().last().pose();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.vertex(poseMat, drawX,             drawY + drawHeight, 0).uv(u0, v1).endVertex();
        buf.vertex(poseMat, drawX + drawWidth, drawY + drawHeight, 0).uv(u1, v1).endVertex();
        buf.vertex(poseMat, drawX + drawWidth, drawY,              0).uv(u1, v0).endVertex();
        buf.vertex(poseMat, drawX,             drawY,              0).uv(u0, v0).endVertex();
        tess.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
