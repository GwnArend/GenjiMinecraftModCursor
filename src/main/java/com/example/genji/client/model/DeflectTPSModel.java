package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.client.render.DeflectTPSAnimatable;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * Third-person deflect model with wakizashi arms animation.
 */
public class DeflectTPSModel extends GeoModel<DeflectTPSAnimatable> {
    @Override
    public ResourceLocation getModelResource(DeflectTPSAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/deflect.tps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DeflectTPSAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/item/deflect/genji_deflect_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DeflectTPSAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/deflect.tps.animation.deflect.json");
    }
}

