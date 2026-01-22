package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.EatingPace;
import com.neuromuser.eatingpace.config.EatingInterruptConfig;
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
            return EatingInterruptConfig.FIRE_INTERRUPTS;
        }

        if (sourceName.contains("magic") || sourceName.contains("poison") || sourceName.contains("wither")) {
            return EatingInterruptConfig.POISON_INTERRUPTS;
        }

        if (source.isIn(DamageTypeTags.IS_FALL)) {
            return EatingInterruptConfig.FALL_INTERRUPTS;
        }

        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            return EatingInterruptConfig.PROJECTILE_INTERRUPTS;
        }

        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            return EatingInterruptConfig.EXPLOSION_INTERRUPTS;
        }

        return EatingInterruptConfig.MELEE_INTERRUPTS;
    }
}