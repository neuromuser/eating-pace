package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private float saturationLevel;

    @Inject(method = "add", at = @At("TAIL"))
    private void applyCustomSaturationCap(int food, float saturationModifier, CallbackInfo ci) {
        float cap = ConfigManager.get().general.saturationCap;
        if (this.saturationLevel > cap) {
            this.saturationLevel = cap;
        }
    }

    @Inject(method = "setFoodLevel", at = @At("TAIL"))
    private void onSetFoodLevel(int foodLevel, CallbackInfo ci) {
        float cap = ConfigManager.get().general.saturationCap;
        if (this.saturationLevel > cap) {
            this.saturationLevel = cap;
        }
    }

    @Inject(method = "setSaturationLevel", at = @At("TAIL"))
    private void onSetSaturationLevel(float saturationLevel, CallbackInfo ci) {
        float cap = ConfigManager.get().general.saturationCap;
        if (this.saturationLevel > cap) {
            this.saturationLevel = cap;
        }
    }
}