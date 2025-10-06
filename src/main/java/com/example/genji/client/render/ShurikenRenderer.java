package com.example.genji.client.render;

import com.example.genji.client.model.ShurikenModel;
import com.example.genji.content.ShurikenEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShurikenRenderer extends GeoEntityRenderer<ShurikenEntity> {
    
    public ShurikenRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ShurikenModel());
        this.shadowRadius = 0.15f;
    }

    @Override
    public void render(ShurikenEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // No entity-level transforms - let GeckoLib model handle bone rotation
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
