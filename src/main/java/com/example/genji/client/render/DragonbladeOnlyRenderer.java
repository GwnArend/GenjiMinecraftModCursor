package com.example.genji.client.render;

import com.example.genji.client.model.DragonbladeFPSModel;
import com.example.genji.content.DragonbladeItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DragonbladeOnlyRenderer extends GeoItemRenderer<DragonbladeItem> {
    public DragonbladeOnlyRenderer() { super(new DragonbladeFPSModel()); }
}
