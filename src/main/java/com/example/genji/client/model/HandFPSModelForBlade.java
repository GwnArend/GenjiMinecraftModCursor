package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.DragonbladeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/** First-person hand model (typed for Dragonblade item). */
public class HandFPSModelForBlade extends GeoModel<DragonbladeItem> {
    @Override
    public ResourceLocation getModelResource(DragonbladeItem a) {
        // assets/genji/geo/hand.fps.model.geo.json
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/hand.fps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonbladeItem a) {
        // Fallback (renderer swaps to live skin)
        return ResourceLocation.withDefaultNamespace("textures/entity/steve.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonbladeItem a) {
        // Shared FP animation file
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/shurikens.fps.animations.json");
    }
}
