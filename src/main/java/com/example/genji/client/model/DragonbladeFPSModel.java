package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.content.DragonbladeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/** First-person dragonblade model. */
public class DragonbladeFPSModel extends GeoModel<DragonbladeItem> {
    @Override
    public ResourceLocation getModelResource(DragonbladeItem a) {
        // assets/genji/geo/dragonblade.fps.model.geo.json
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/dragonblade.fps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonbladeItem a) {
        // assets/genji/textures/item/dragonblade.png
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/item/dragonblade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonbladeItem a) {
        // Shared FP animation file with hand/shurikens
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/shurikens.fps.animations.json");
    }
}
