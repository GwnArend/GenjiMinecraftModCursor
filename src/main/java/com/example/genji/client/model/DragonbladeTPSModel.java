package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.DragonbladeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/** Third-person and GUI dragonblade model - STATIC (no animations). */
public class DragonbladeTPSModel extends GeoModel<DragonbladeItem> {
    @Override
    public ResourceLocation getModelResource(DragonbladeItem a) {
        // assets/genji/geo/dragonblade.tps.model.geo.json
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/dragonblade.tps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonbladeItem a) {
        // assets/genji/textures/item/dragonblade.png
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/item/dragonblade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonbladeItem a) {
        // Static animation file (no movement)
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/dragonblade.tps.static.json");
    }
}
