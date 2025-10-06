package com.example.genji.client.render;

import com.example.genji.client.model.HandFPSModel;
import com.example.genji.content.ShurikenItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HandOnlyRenderer extends GeoItemRenderer<ShurikenItem> {
    public HandOnlyRenderer() { super(new HandFPSModel()); }

    private static ResourceLocation liveSkin() {
        LocalPlayer lp = Minecraft.getInstance().player;
        if (lp != null) return ((AbstractClientPlayer) lp).getSkinTextureLocation();
        return ResourceLocation.withDefaultNamespace("textures/entity/steve.png");
    }

    @Override public ResourceLocation getTextureLocation(ShurikenItem animatable) { return liveSkin(); }
}
