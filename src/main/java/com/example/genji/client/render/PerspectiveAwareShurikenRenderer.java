package com.example.genji.client.render;

import com.example.genji.client.fx.DragonbladeFxState;
import com.example.genji.client.model.HandFPSModel;
import com.example.genji.client.model.ShurikensFPSModel;
import com.example.genji.client.render.ShurikensTPSRenderer;
import com.example.genji.content.ShurikenItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nonnull;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * Perspective-aware composite renderer for shurikens.
 * - First person: renders hand + shurikens models (same as before)
 * - Third person: renders only shurikens model without positioning manipulation
 */
public class PerspectiveAwareShurikenRenderer extends BlockEntityWithoutLevelRenderer {

    // First-person renderers (hand + shurikens)
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

    private final GeoItemRenderer<ShurikenItem> shurikenFPRenderer =
            new GeoItemRenderer<>(new ShurikensFPSModel()) {};

    // Third-person renderer (shurikens only, with animations)
    private final ShurikensTPSRenderer shurikenTPSRenderer = new ShurikensTPSRenderer();


    public PerspectiveAwareShurikenRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels()
        );
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack,
                             @Nonnull ItemDisplayContext displayContext,
                             @Nonnull PoseStack poseStack,
                             @Nonnull MultiBufferSource buffer,
                             int packedLight,
                             int packedOverlay) {

        if (!(stack.getItem() instanceof ShurikenItem)) return;

        System.out.println("SHURIKEN RENDERER: Called with context=" + displayContext);

        // For ground, fixed, and other contexts, let Minecraft handle the default rendering
        if (displayContext == ItemDisplayContext.GROUND ||
            displayContext == ItemDisplayContext.FIXED ||
            displayContext == ItemDisplayContext.HEAD ||
            displayContext == ItemDisplayContext.NONE) {
            // Let Minecraft handle the default rendering for these contexts
            System.out.println("SHURIKEN RENDERER: Ground/Fixed/Head/None context - letting Minecraft handle");
            return;
        }

        // For first-person rendering, let the FirstPersonShurikenOverlay handle it
        if (isFirstPersonContext(displayContext)) {
            // First person is handled by FirstPersonShurikenOverlay
            System.out.println("SHURIKEN RENDERER: First person context - letting overlay handle");
            return;
        }

        // For GUI and third-person contexts, render the GeckoLib TPS model (static)
        if (displayContext == ItemDisplayContext.GUI || isThirdPersonContext(displayContext)) {
            System.out.println("SHURIKEN RENDERER: GUI/Third person context - rendering TPS model");
            System.out.println("SHURIKEN RENDERER: Display context = " + displayContext);

            // Render GeckoLib TPS model
            try {
                shurikenTPSRenderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
                System.out.println("SHURIKEN RENDERER: TPS model render completed successfully");
            } catch (Exception e) {
                System.out.println("SHURIKEN RENDERER: TPS model render failed: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        // For any other context, let Minecraft handle it
        System.out.println("SHURIKEN RENDERER: Unknown context - letting Minecraft handle: " + displayContext);
    }

    private boolean isFirstPersonContext(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ||
               displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
    }

    private boolean isThirdPersonContext(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND ||
               displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }
}
