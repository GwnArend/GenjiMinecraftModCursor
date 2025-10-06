package com.example.genji.client;

import com.example.genji.GenjiMod;
import com.example.genji.client.anim.ShurikenFPAnim;
import com.example.genji.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.example.genji.client.anim.ShurikenFPAnim.Type;

@Mod.EventBusSubscriber(modid = GenjiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandRender {

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent e) {
        // Only main hand + holding our shuriken item
        if (e.getHand() != InteractionHand.MAIN_HAND) return;
        ItemStack stack = e.getItemStack();
        if (stack.isEmpty() || !stack.is(ModItems.SHURIKEN.get())) return;

        PoseStack ps = e.getPoseStack();

        float pt = e.getPartialTick();
        float tM1 = ShurikenFPAnim.progress(Type.M1_SHOT, pt);
        float tM2 = ShurikenFPAnim.progress(Type.M2_FAN, pt);

        // Apply at most one (give M1 per-shot priority)
        if (tM1 >= 0f) {
            applyM1Throw(ps, tM1);
        } else if (tM2 >= 0f) {
            applyM2Fan(ps, tM2);
        }
        // Don’t cancel; vanilla still renders the arm + item with our transforms
    }

    // Quick wrist flick forward (eased), tiny windup
    private static void applyM1Throw(PoseStack ps, float t) {
        float s = easeOutSine(t); // 0..1
        ps.translate(0.00, -0.02 * s, -0.05 * s); // forward push
        ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees( 55f * s));
        ps.mulPose(com.mojang.math.Axis.YP.rotationDegrees(  8f * s));
        ps.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-12f * s));
    }

    // Wider sweep with slight outward roll
    private static void applyM2Fan(PoseStack ps, float t) {
        float s = easeOutSine(t);
        ps.translate(0.02 * s, -0.03 * s, -0.06 * s);
        ps.mulPose(com.mojang.math.Axis.XP.rotationDegrees( 40f * s));
        ps.mulPose(com.mojang.math.Axis.YP.rotationDegrees( 18f * s));
        ps.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-20f * s));
    }

    private static float easeOutSine(float x) {
        return (float) Math.sin((x * Math.PI) / 2.0);
    }
}
