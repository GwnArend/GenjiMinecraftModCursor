package com.example.genji.client.render;

import com.example.genji.client.anim.FPDeflectAnim;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * GeckoLib animatable object for the third-person deflect effect.
 * This handles the tpdeflect animation specifically.
 * Uses a singleton pattern to maintain animation state across frames.
 */
public class DeflectTPSAnimatable implements GeoItem {
    private static DeflectTPSAnimatable INSTANCE = null;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation DEFLECT_ANIM = RawAnimation.begin().thenLoop("tpdeflect");

    private DeflectTPSAnimatable() {}

    public static DeflectTPSAnimatable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeflectTPSAnimatable();
        }
        return INSTANCE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "deflect_tps_controller", 0, state -> {
            // Always play the deflect animation when visible
            return state.setAndContinue(DEFLECT_ANIM);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
