package com.example.genji.client.render;

import com.example.genji.client.anim.FPDeflectAnim;
import com.example.genji.client.model.DeflectTPSModelForDragonblade;
import com.example.genji.content.DragonbladeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.Nonnull;

/**
 * Third-person layer that renders the deflect wakizashi effect on the player when deflecting.
 */
public class DeflectTPSLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    
    private final GeoItemRenderer<DragonbladeItem> renderer = new GeoItemRenderer<>(new DeflectTPSModelForDragonblade());
    private static ItemStack dummyStack = null;
    
    // Adjustable position and rotation (debug mode)
    public static double offsetX = 0.450;
    public static double offsetY = 0.900;
    public static double offsetZ = -0.550;
    public static float rotationX = 180.0f;
    public static float rotationY = -180.0f;
    public static float rotationZ = 0.0f;
    public static float scale = 1.000f;

    public DeflectTPSLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight,
                       @Nonnull AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        
        // Only render when deflect is active
        if (!FPDeflectAnim.isVisible()) {
            return;
        }

        // Create a dummy ItemStack if needed (GeckoLib expects an ItemStack context)
        if (dummyStack == null) {
            dummyStack = new ItemStack(com.example.genji.registry.ModItems.DRAGONBLADE.get());
        }

        poseStack.pushPose();

        // Position relative to player body
        poseStack.translate(offsetX, offsetY, offsetZ);
        
        // Apply rotations
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
        
        // Apply scale
        poseStack.scale(scale, scale, scale);

        // Use GeckoLib's standard renderByItem method
        renderer.renderByItem(
            dummyStack,
            ItemDisplayContext.NONE,
            poseStack,
            buffer,
            packedLight,
            OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }
}
