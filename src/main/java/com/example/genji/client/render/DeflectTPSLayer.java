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
    
    // Position and rotation for the deflect model
    private static final double OFFSET_X = 0.450;
    private static final double OFFSET_Y = 0.900;
    private static final double OFFSET_Z = -0.550;
    private static final float ROTATION_X = 180.0f;
    private static final float ROTATION_Y = -180.0f;
    private static final float ROTATION_Z = 0.0f;
    private static final float SCALE = 1.000f;

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
        poseStack.translate(OFFSET_X, OFFSET_Y, OFFSET_Z);
        
        // Apply rotations
        poseStack.mulPose(Axis.XP.rotationDegrees(ROTATION_X));
        poseStack.mulPose(Axis.YP.rotationDegrees(ROTATION_Y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(ROTATION_Z));
        
        // Apply scale
        poseStack.scale(SCALE, SCALE, SCALE);

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
