package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.EatingPace;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEatingInterruptMixin {

    @Shadow
    public abstract ItemStack getActiveItem();

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract void stopUsingItem();

    @Inject(method = "damage", at = @At("HEAD"))
    private void interruptEatingOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!EatingPace.isServerSideActive()) {
            return;
        }

        // Check if eating interruption is enabled
        if (!EatingPace.CONFIG.enableEatingInterruption) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        if (!this.isUsingItem()) {
            return;
        }

        ItemStack activeItem = this.getActiveItem();
        if (activeItem.isEmpty() || !activeItem.isFood()) {
            return;
        }

        if (shouldInterruptEating(source)) {
            this.stopUsingItem();
        }
    }

    @Unique
    private boolean shouldInterruptEating(DamageSource source) {
        String sourceName = source.getName();

        if (source.isIn(DamageTypeTags.IS_FIRE) || sourceName.equals("inFire") || sourceName.equals("onFire") || sourceName.equals("lava")) {
            return EatingPace.CONFIG.fireInterrupts;
        }

        if (sourceName.contains("magic") || sourceName.contains("poison") || sourceName.contains("wither")) {
            return EatingPace.CONFIG.poisonInterrupts;
        }

        if (sourceName.equals("drown") || sourceName.contains("drowning")) {
            return EatingPace.CONFIG.drowningInterrupts;
        }

        if (sourceName.equals("inWall") || sourceName.contains("suffocate") || sourceName.contains("suffocation")) {
            return EatingPace.CONFIG.suffocationInterrupts;
        }

        if (sourceName.equals("starve") || sourceName.contains("starvation")) {
            return EatingPace.CONFIG.starvationInterrupts;
        }

        if (source.isIn(DamageTypeTags.IS_FALL)) {
            return EatingPace.CONFIG.fallInterrupts;
        }

        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            return EatingPace.CONFIG.projectileInterrupts;
        }

        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            return EatingPace.CONFIG.explosionInterrupts;
        }

        return EatingPace.CONFIG.meleeInterrupts;
    }
}