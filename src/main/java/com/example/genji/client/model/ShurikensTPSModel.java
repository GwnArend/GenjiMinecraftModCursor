package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.ShurikenItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * Third-person and GUI model for shurikens - STATIC (no animations).
 */
public class ShurikensTPSModel extends GeoModel<ShurikenItem> {
    @Override
    public ResourceLocation getModelResource(ShurikenItem a) {
        // assets/genji/geo/shurikens.tps.model.geo.json
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/shurikens.tps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShurikenItem a) {
        // assets/genji/textures/entity/shuriken_projectile.png
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/entity/shuriken_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShurikenItem a) {
        // Static animation file (no movement)
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/shurikens.tps.static.json");
    }
}