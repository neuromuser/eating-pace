package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.neuromuser.eatingpace.EatingPace;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private float saturationLevel;
    @Shadow private int foodLevel;

    @Unique
    private static final float VANILLA_SATURATION_CAP = 20.0f;

    @ModifyArg(method = "add", at = @At(value = "INVOKE",
            target = "Ljava/lang/Math;min(FF)F"), index = 1)
    private float modifySaturationCap(float originalCap) {
        if (EatingPace.isServerSideActive()) {
            return EatingPace.CONFIG.saturationCap;
        }
        return VANILLA_SATURATION_CAP;
    }

    @Inject(method = "setFoodLevel", at = @At("TAIL"))
    private void onSetFoodLevel(int foodLevel, CallbackInfo ci) {
        if (EatingPace.isServerSideActive()) {
            if (this.saturationLevel > EatingPace.CONFIG.saturationCap) {
                this.saturationLevel = EatingPace.CONFIG.saturationCap;
            }
        } else {
            if (this.saturationLevel > VANILLA_SATURATION_CAP) {
                this.saturationLevel = VANILLA_SATURATION_CAP;
            }
        }
    }

    @ModifyReturnValue(method = "getSaturationLevel", at = @At("RETURN"))
    private float ensureWithinCap(float original) {
        if (EatingPace.isServerSideActive()) {
            return Math.min(original, EatingPace.CONFIG.saturationCap);
        }
        return Math.min(original, VANILLA_SATURATION_CAP);
    }
}