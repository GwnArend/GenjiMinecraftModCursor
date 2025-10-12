package com.example.genji.client.hud;

import com.example.genji.GenjiMod;
import com.example.genji.client.ClientGenjiData;
import com.example.genji.registry.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT)
public final class UltimateCharge {
    private UltimateCharge() {}

    // Outer ring (Ultimate)
    private static final int OUTER_R_OUT = 27;
    private static final int OUTER_R_IN  = 20;

    // Inner ring (Nano) – modestly larger
    private static final int INNER_R_OUT = 20;
    private static final int INNER_R_IN  = 15;

    private static final int TXT_COLOR        = 0xFFFFFFFF;
    private static final int BG_COLOR         = 0xAA202020;
    private static final int ULT_COLOR_FILL   = 0xFF55FF55; // charging
    private static final int ULT_COLOR_READY  = 0xFFFFD24D; // ready gold
    private static final int NANO_COLOR_FILL  = 0xFF4090FF; // blue

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.options.hideGui || mc.isPaused()) return;

        // Only render while holding Genji kit
        ItemStack main = mc.player.getMainHandItem();
        ItemStack off  = mc.player.getOffhandItem();
        boolean holding = (!main.isEmpty() && (main.is(ModItems.SHURIKEN.get()) || main.is(ModItems.DRAGONBLADE.get())))
                || (!off.isEmpty()  && (off.is(ModItems.SHURIKEN.get())  || off.is(ModItems.DRAGONBLADE.get())));
        if (!holding) return;

        int sw = event.getWindow().getGuiScaledWidth();
        int sh = event.getWindow().getGuiScaledHeight();

        int cx = sw / 2;
        int cy = sh - 70;

        GuiGraphics g = event.getGuiGraphics();

        // Background base
        drawDonut(g, cx, cy, OUTER_R_OUT, OUTER_R_IN, 1.0f, BG_COLOR);
        drawDonut(g, cx, cy, INNER_R_OUT, INNER_R_IN, 1.0f, BG_COLOR);

        // Ultimate ring
        int ult = clamp01Int(ClientGenjiData.ult);
        float ultPct = clamp01(ult / 100.0f);
        if (ultPct > 0f) {
            int fg = (ultPct >= 1.0f) ? ULT_COLOR_READY : ULT_COLOR_FILL;
            drawDonut(g, cx, cy, OUTER_R_OUT, OUTER_R_IN, ultPct, fg);
        }

        // % text: only for Ultimate
        String label = ult + "%";
        int tw = mc.font.width(label);
        int th = mc.font.lineHeight;
        g.drawString(mc.font, label, cx - tw / 2, cy - th / 2, TXT_COLOR, false);

        // Nano inner ring (no text)
        int nano = clamp01Int(ClientGenjiData.nano);
        float nanoPct = clamp01(nano / 100.0f);
        if (nanoPct > 0f) {
            drawDonut(g, cx, cy, INNER_R_OUT, INNER_R_IN, nanoPct, NANO_COLOR_FILL);
        }
    }

    public static float clamp01(float v) { return v < 0f ? 0f : (v > 1f ? 1f : v); }
    private static int clamp01Int(int v) { return v < 0 ? 0 : Math.min(100, v); }

    /** Render a ring sector (0..progress..1) between rInner..rOuter. */
    public static void drawDonut(GuiGraphics g, int cx, int cy, int rOuter, int rInner, float progress, int argb) {
        progress = clamp01(progress);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        var mat = g.pose().last().pose();

        float a = ((argb >>> 24) & 0xFF) / 255f;
        float r = ((argb >>> 16) & 0xFF) / 255f;
        float gg = ((argb >>> 8)  & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;

        int steps = Math.max(12, rOuter * 4);
        float maxAngle = (float) (Math.PI * 2.0 * progress);

        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            float ang = t * maxAngle - (float) Math.PI / 2f;

            float cos = (float) Math.cos(ang);
            float sin = (float) Math.sin(ang);

            float ox = cx + cos * rOuter;
            float oy = cy + sin * rOuter;
            float ix = cx + cos * rInner;
            float iy = cy + sin * rInner;

            buf.vertex(mat, ox, oy, 0).color(r, gg, b, a).endVertex();
            buf.vertex(mat, ix, iy, 0).color(r, gg, b, a).endVertex();
        }
        tess.end();
        RenderSystem.disableBlend();
    }
}
