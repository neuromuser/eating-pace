package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private float saturationLevel;
    @Shadow private int foodLevel;

    @Unique
    private static final float NEW_SATURATION_CAP = 30.0f;
    @ModifyArg(method = "add", at = @At(value = "INVOKE",
            target = "Ljava/lang/Math;min(FF)F"), index = 1)
    private float modifySaturationCap(float originalCap) {
        return NEW_SATURATION_CAP;
    }

    @Inject(method = "setFoodLevel", at = @At("TAIL"))
    private void onSetFoodLevel(int foodLevel, CallbackInfo ci) {
        if (this.saturationLevel > NEW_SATURATION_CAP) {
            this.saturationLevel = NEW_SATURATION_CAP;
        }
    }


    @ModifyReturnValue(method = "getSaturationLevel", at = @At("RETURN"))
    private float ensureWithinNewCap(float original) {
        return Math.min(original, NEW_SATURATION_CAP);
    }
}