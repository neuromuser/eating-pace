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
    @Unique
    private static final float NEW_SATURATION_CAP = 30.0f;

    @ModifyArg(method = "add", at = @At(value = "INVOKE",
            target = "Ljava/lang/Math;min(FF)F"), index = 1)
    private float modifySaturationCap(float originalCap) {
        // Only apply new cap if server has the mod
        if (EatingPace.isServerSideActive()) {
            return NEW_SATURATION_CAP;
        }
        return VANILLA_SATURATION_CAP;
    }

    @Inject(method = "setFoodLevel", at = @At("TAIL"))
    private void onSetFoodLevel(int foodLevel, CallbackInfo ci) {
        // Only apply new cap if server has the mod
        if (EatingPace.isServerSideActive()) {
            if (this.saturationLevel > NEW_SATURATION_CAP) {
                this.saturationLevel = NEW_SATURATION_CAP;
            }
        } else {
            // Vanilla behavior - cap at food level
            if (this.saturationLevel > VANILLA_SATURATION_CAP) {
                this.saturationLevel = VANILLA_SATURATION_CAP;
            }
        }
    }

    @ModifyReturnValue(method = "getSaturationLevel", at = @At("RETURN"))
    private float ensureWithinCap(float original) {
        // Only apply new cap if server has the mod
        if (EatingPace.isServerSideActive()) {
            return Math.min(original, NEW_SATURATION_CAP);
        }
        return Math.min(original, VANILLA_SATURATION_CAP);
    }
}