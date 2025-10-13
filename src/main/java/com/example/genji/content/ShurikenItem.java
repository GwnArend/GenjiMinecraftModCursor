package com.example.genji.content;

import com.example.genji.client.render.PerspectiveAwareShurikenRenderer;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * First-person Gecko item with two controllers (hand + shurikens).
 * Uses a client-side per-press token so BOTH controllers restart from t=0 every time.
 * Forge/Minecraft: 1.20.1, GeckoLib: 4.4.x
 */
public class ShurikenItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Clip names must match your .animations.json
    public static final RawAnimation THROW_SINGLE = RawAnimation.begin().thenPlay("throw_single");
    public static final RawAnimation THROW_FAN    = RawAnimation.begin().thenPlay("throw_fan");
    public static final RawAnimation IDLE         = RawAnimation.begin().thenLoop("idle"); // optional

    // --- Pending animation token per held-stack instance (client only) ---
    public static final class Pending {
        public final String kind; // "single" or "fan"
        public boolean handDone = false;
        public boolean shurikenDone = false;
        public boolean tpsDone = false;
        public Pending(String kind) { this.kind = kind; }
    }

    // Keep the map available on both dists; client guards prevent server access.
    private static final Long2ObjectOpenHashMap<Pending> PENDING = new Long2ObjectOpenHashMap<>();

    public ShurikenItem(Properties props) { super(props); }

    // Provide the BEWLR (two-pass renderer: shurikens + hand)
    @Override
    public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final PerspectiveAwareShurikenRenderer renderer = new PerspectiveAwareShurikenRenderer();

            @OnlyIn(Dist.CLIENT)
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // HAND controller
        controllers.add(new AnimationController<>(this, "hand_ctrl", 0, state -> {
            if (state.isCurrentAnimation(null) && IDLE != null) {
                state.setAndContinue(IDLE);
            }
            ItemStack stack = getClientHeldStack();
            if (!stack.isEmpty()) {
                long id = GeoItem.getId(stack);
                Pending tok = PENDING.get(id);
                if (tok != null && !tok.handDone) {
                    state.getController().forceAnimationReset(); // restart from t=0
                    if ("single".equals(tok.kind)) state.setAndContinue(THROW_SINGLE);
                    else if ("fan".equals(tok.kind)) state.setAndContinue(THROW_FAN);
                    tok.handDone = true;
                    if (tok.handDone && tok.shurikenDone && tok.tpsDone) PENDING.remove(id);
                }
            }
            return PlayState.CONTINUE;
        }));

        // SHURIKENS controller
        controllers.add(new AnimationController<>(this, "shuriken_ctrl", 0, state -> {
            if (state.isCurrentAnimation(null) && IDLE != null) {
                state.setAndContinue(IDLE);
            }
            ItemStack stack = getClientHeldStack();
            if (!stack.isEmpty()) {
                long id = GeoItem.getId(stack);
                Pending tok = PENDING.get(id);
                if (tok != null && !tok.shurikenDone) {
                    state.getController().forceAnimationReset(); // restart from t=0
                    if ("single".equals(tok.kind)) state.setAndContinue(THROW_SINGLE);
                    else if ("fan".equals(tok.kind)) state.setAndContinue(THROW_FAN);
                    tok.shurikenDone = true;
                    if (tok.handDone && tok.shurikenDone && tok.tpsDone) PENDING.remove(id);
                }
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
    @Override public UseAnim getUseAnimation(@Nonnull ItemStack stack) { return UseAnim.NONE; }
    @Override public int getUseDuration(@Nonnull ItemStack stack) { return 0; }

    // ----- Client helpers used by the packet -----
    @OnlyIn(Dist.CLIENT)
    public static void playThrowSingle() {
        ItemStack stack = getClientHeldStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof ShurikenItem)) return;
        long id = GeoItem.getId(stack);
        PENDING.put(id, new Pending("single")); // both controllers will consume this
    }

    @OnlyIn(Dist.CLIENT)
    public static void playThrowFan() {
        ItemStack stack = getClientHeldStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof ShurikenItem)) return;
        long id = GeoItem.getId(stack);
        PENDING.put(id, new Pending("fan")); // all controllers will consume this
    }

    // Helper methods for third-person renderer
    @OnlyIn(Dist.CLIENT)
    public static Pending getPending(long id) {
        return PENDING.get(id);
    }

    @OnlyIn(Dist.CLIENT)
    public static void removePending(long id) {
        PENDING.remove(id);
    }

    @OnlyIn(Dist.CLIENT)
    private static ItemStack getClientHeldStack() {
        var mc = Minecraft.getInstance();
        return (mc != null && mc.player != null) ? mc.player.getMainHandItem() : ItemStack.EMPTY;
    }
}
