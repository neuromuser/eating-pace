package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private float saturationLevel;
    @Shadow private int foodLevel;

    protected HungerManagerMixin(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    @ModifyArgs(method = "addInternal", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"))
    private void modifySaturationClamp(Args args) {

        float customCap = ConfigManager.get().saturationCap;
        args.set(2, customCap);
    }

    @Inject(method = "setFoodLevel", at = @At("TAIL"))
    private void onSetFoodLevel(int foodLevel, CallbackInfo ci) {
        float cap = ConfigManager.get().saturationCap;
        if (this.saturationLevel > cap) {
            this.saturationLevel = cap;
        }
    }

    @ModifyReturnValue(method = "getSaturationLevel", at = @At("RETURN"))
    private float ensureWithinCap(float original) {
        return Math.min(original, ConfigManager.get().saturationCap);
    }
}