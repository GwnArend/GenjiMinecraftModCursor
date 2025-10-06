package com.example.genji.client.render;

import com.example.genji.client.model.HandFPSModelForBlade;
import com.example.genji.content.DragonbladeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/** Hand-only renderer that uses the player's live skin texture. */
public class HandOnlyRendererForBlade extends GeoItemRenderer<DragonbladeItem> {
    public HandOnlyRendererForBlade() { super(new HandFPSModelForBlade()); }

    private static ResourceLocation liveSkin() {
        LocalPlayer lp = Minecraft.getInstance().player;
        if (lp != null) return ((AbstractClientPlayer) lp).getSkinTextureLocation();
        return ResourceLocation.withDefaultNamespace("textures/entity/steve.png");
    }

    @Override public ResourceLocation getTextureLocation(DragonbladeItem animatable) { return liveSkin(); }
}
