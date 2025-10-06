package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.ShurikenItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * Wrapper model that uses dragonblade TPS model for shuriken items.
 * This allows rendering dragonblade model when shurikens item is equipped.
 */
public class DragonbladeTPSModelForShuriken extends GeoModel<ShurikenItem> {
    @Override
    public ResourceLocation getModelResource(ShurikenItem a) {
        // Use dragonblade TPS model
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/dragonblade.tps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShurikenItem a) {
        // Use dragonblade texture
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/item/dragonblade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShurikenItem a) {
        // Use empty animation file for third-person (no animations)
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/dragonblade.tps.animations.json");
    }
}

