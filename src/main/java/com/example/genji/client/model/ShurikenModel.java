package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.ShurikenEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShurikenModel extends GeoModel<ShurikenEntity> {
    @Override
    public ResourceLocation getModelResource(ShurikenEntity a) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/shuriken_projectile.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(ShurikenEntity a) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/entity/shuriken_projectile.png");
    }
    @Override
    public ResourceLocation getAnimationResource(ShurikenEntity a) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/shuriken_projectile.animation.json");
    }
}
