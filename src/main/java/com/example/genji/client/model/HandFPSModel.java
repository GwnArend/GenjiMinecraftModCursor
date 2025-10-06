package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.ShurikenItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * First-person hand model.
 * Uses the same animation file as the shurikens: animations/shurikens.fps.animations.json
 * (both models share identical clip names like throw_single / throw_fan).
 */
public class HandFPSModel extends GeoModel<ShurikenItem> {
    @Override
    public ResourceLocation getModelResource(ShurikenItem a) {
        // assets/genji/geo/hand.fps.model.geo.json
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/hand.fps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShurikenItem a) {
        // Fallback; renderer swaps to the live player base skin
        return ResourceLocation.withDefaultNamespace("textures/entity/steve.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShurikenItem a) {
        // Shared FP animation file
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/shurikens.fps.animations.json");
    }
}
