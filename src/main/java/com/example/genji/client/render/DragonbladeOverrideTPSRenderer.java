package com.example.genji.client.render;

import com.example.genji.client.anim.DragonbladeFPAnim;
import com.example.genji.client.model.DragonbladeTPSModelForShuriken;
import com.example.genji.content.ShurikenItem;
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
 * Third-person renderer for dragonblade when shurikens item is equipped.
 * This handles the case where dragonblade is casted but player is still holding shurikens item.
 */
public class DragonbladeOverrideTPSRenderer extends GeoItemRenderer<ShurikenItem> implements GeoItem {
    
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    private static final RawAnimation SWING_LEFT = RawAnimation.begin().thenPlay("swing_left");
    private static final RawAnimation SWING_RIGHT = RawAnimation.begin().thenPlay("swing_right");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    public DragonbladeOverrideTPSRenderer() {
        super(new DragonbladeTPSModelForShuriken());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "swing_ctrl", 0, state -> {
            if (state.isCurrentAnimation(null) && IDLE != null) {
                state.setAndContinue(IDLE);
            }
            
            // Hook into DragonbladeFPAnim to sync third-person animations with first-person
            if (DragonbladeFPAnim.isLeft()) {
                state.getController().forceAnimationReset();
                state.setAndContinue(SWING_LEFT);
            } else if (DragonbladeFPAnim.isRight()) {
                state.getController().forceAnimationReset();
                state.setAndContinue(SWING_RIGHT);
            }
            
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
