package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private float saturationLevel;

    @ModifyArg(method = "add", at = @At(value = "INVOKE",
            target = "Ljava/lang/Math;min(FF)F"), index = 1)
    private float modifySaturationCap(float originalCap) {
        return ConfigManager.get().general.saturationCap;
    }

    @Inject(method = "setFoodLevel", at = @At("TAIL"))
    private void onSetFoodLevel(int foodLevel, CallbackInfo ci) {
        float cap = ConfigManager.get().general.saturationCap;
        if (this.saturationLevel > cap) {
            this.saturationLevel = cap;
        }
    }

    @ModifyReturnValue(method = "getSaturationLevel", at = @At("RETURN"))
    private float ensureWithinCap(float original) {
        return Math.min(original, ConfigManager.get().general.saturationCap);
    }
}