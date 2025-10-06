package com.example.genji.client.render;

import com.example.genji.client.anim.DragonbladeFPAnim;
import com.example.genji.client.model.DragonbladeTPSModel;
import com.example.genji.content.DragonbladeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Third-person renderer for dragonblade - STATIC MODEL (no animations for held items).
 */
public class DragonbladeTPSRenderer extends GeoItemRenderer<DragonbladeItem> implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DragonbladeTPSRenderer() {
        super(new DragonbladeTPSModel());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // No animations - static model for held items and GUI
        // Player arm swing animations are handled by DragonbladeCombat
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
