package com.example.genji.client.render;

import com.example.genji.client.model.DragonbladeFPSModel;
import com.example.genji.content.DragonbladeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * Composite BEWLR that renders the FP hand first, then the dragonblade.
 * (Your FP overlay handles first-person during blade usage; this BEWLR
 * remains useful for inventory/third-person cases and keeps item code happy.)
 */
public class FPSDragonbladeCompositeRenderer extends BlockEntityWithoutLevelRenderer {

    private final GeoItemRenderer<DragonbladeItem> handRenderer = new HandOnlyRendererForBlade();
    private final GeoItemRenderer<DragonbladeItem> bladeRenderer =
            new GeoItemRenderer<>(new DragonbladeFPSModel()) {};

    public FPSDragonbladeCompositeRenderer() {
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

        if (!(stack.getItem() instanceof DragonbladeItem)) return;

        // Same transform stack for both passes so bones stay aligned.
        handRenderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        bladeRenderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }
}
