package com.example.genji.client.render;

import com.example.genji.client.model.HandFPSModel;
import com.example.genji.client.model.ShurikensFPSModel;
import com.example.genji.content.ShurikenItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * Render BOTH first-person geos (hand + shurikens) through the exact same FP pipeline:
 *  - Same PoseStack (no extra transforms)
 *  - Same ItemDisplayContext
 *  - No custom instance-id hacks or render type overrides
 *
 * Hand uses the player's BASE skin; shurikens use their own texture.
 * Both models read animations from animations/shurikens.fps.animations.json.
 */
public class FPSShurikenCompositeRenderer extends BlockEntityWithoutLevelRenderer {

    private final GeoItemRenderer<ShurikenItem> handRenderer =
            new GeoItemRenderer<>(new HandFPSModel()) {
                @Override
                public ResourceLocation getTextureLocation(ShurikenItem animatable) {
                    AbstractClientPlayer p = Minecraft.getInstance().player;
                    return (p != null)
                            ? p.getSkinTextureLocation() // base skin in 1.20.1
                            : ResourceLocation.withDefaultNamespace("textures/entity/steve.png");
                }
            };

    private final GeoItemRenderer<ShurikenItem> shurikenRenderer =
            new GeoItemRenderer<>(new ShurikensFPSModel()) {
                // No overrides; identical pipeline to the hand pass
            };

    public FPSShurikenCompositeRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels()
        );
    }

    @Override
    public void renderByItem(ItemStack stack,
                             ItemDisplayContext displayContext,
                             PoseStack poseStack,
                             MultiBufferSource buffer,
                             int packedLight,
                             int packedOverlay) {

        if (!(stack.getItem() instanceof ShurikenItem)) return;

        // Identical FP transform space for both passes (no pose changes between them)
        handRenderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        shurikenRenderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }
}
