package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.ShurikenItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * First-person "3 shuriken" model.
 * Shares the same animation file as the hand: animations/shurikens.fps.animations.json
 */
public class ShurikensFPSModel extends GeoModel<ShurikenItem> {
    @Override
    public ResourceLocation getModelResource(ShurikenItem a) {
        // assets/genji/geo/shurikens.fps.model.geo.json
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/shurikens.fps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShurikenItem a) {
        // assets/genji/textures/entity/shuriken_projectile.png
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/entity/shuriken_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShurikenItem a) {
        // Shared FP animation file
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/shurikens.fps.animations.json");
    }
}
