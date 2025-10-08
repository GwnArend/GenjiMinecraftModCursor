package com.example.genji.client.model;

import com.example.genji.GenjiMod;
import com.example.genji.client.render.DeflectTPSAnimatable;
import com.example.genji.content.DragonbladeItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * Third-person deflect model for DragonbladeItem compatibility.
 * This version works with DragonbladeItem for compatibility with GeoItemRenderer.
 * It uses the DeflectTPSAnimatable's animation state instead of DragonbladeItem's.
 */
public class DeflectTPSModelForDragonblade extends GeoModel<DragonbladeItem> {
    private final DeflectTPSModel deflectModel = new DeflectTPSModel();
    
    @Override
    public ResourceLocation getModelResource(DragonbladeItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "geo/deflect.tps.model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonbladeItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "textures/item/deflect/genji_deflect_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonbladeItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "animations/deflect.tps.animation.deflect.json");
    }

    @Override
    public RenderType getRenderType(DragonbladeItem animatable, ResourceLocation texture) {
        // Use entityTranslucent to support alpha transparency
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(DragonbladeItem animatable, long instanceId, AnimationState<DragonbladeItem> animationState) {
        // Use the singleton DeflectTPSAnimatable's animation state instead
        DeflectTPSAnimatable deflectAnimatable = DeflectTPSAnimatable.getInstance();
        long deflectInstanceId = deflectAnimatable.hashCode();
        
        // Create animation state for the deflect animatable
        var deflectAnimState = new software.bernie.geckolib.core.animation.AnimationState<>(
            deflectAnimatable, 0, 0, (float) animationState.getAnimationTick(), false
        );
        
        // Update animation controllers and apply to THIS model's baked bones
        deflectModel.addAdditionalStateData(deflectAnimatable, deflectInstanceId, deflectAnimState::setData);
        deflectModel.handleAnimations(deflectAnimatable, deflectInstanceId, deflectAnimState);
        
        // Now apply the bone transformations from the deflect model to this model's baked model
        var deflectBakedModel = deflectModel.getBakedModel(deflectModel.getModelResource(deflectAnimatable));
        var thisBakedModel = this.getBakedModel(this.getModelResource(animatable));
        
        // Copy bone transformations from deflect model to this model
        for (var deflectBone : deflectBakedModel.topLevelBones()) {
            var thisBone = thisBakedModel.getBone(deflectBone.getName());
            if (thisBone.isPresent()) {
                copyBoneTransforms(deflectBone, thisBone.get());
            }
        }
    }
    
    private void copyBoneTransforms(software.bernie.geckolib.cache.object.GeoBone source, software.bernie.geckolib.cache.object.GeoBone target) {
        target.updateRotation(source.getRotX(), source.getRotY(), source.getRotZ());
        target.updatePosition(source.getPosX(), source.getPosY(), source.getPosZ());
        target.updateScale(source.getScaleX(), source.getScaleY(), source.getScaleZ());
        
        // Recursively copy child bone transforms
        for (var sourceChild : source.getChildBones()) {
            for (var targetChild : target.getChildBones()) {
                if (sourceChild.getName().equals(targetChild.getName())) {
                    copyBoneTransforms(sourceChild, targetChild);
                    break;
                }
            }
        }
    }
}
