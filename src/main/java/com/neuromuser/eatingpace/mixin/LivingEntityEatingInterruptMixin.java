package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        if (!ConfigManager.get().enableEatingInterruption) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object) this;

        // In 1.19.2, player-only logic is still fine here
        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        if (!this.isUsingItem()) {
            return;
        }

        ItemStack activeItem = this.getActiveItem();
        // activeItem.isFood() works in 1.19.2
        if (activeItem.isEmpty() || !activeItem.isFood()) {
            return;
        }

        if (shouldInterruptEating(source)) {
            this.stopUsingItem();
        }
    }

    @Unique
    private boolean shouldInterruptEating(DamageSource source) {
        // 1.19.2 uses source.getName() and specific boolean checks
        String sourceName = source.getName();

        if (source.isFire() || sourceName.equals("lava")) {
            return ConfigManager.get().fireInterrupts;
        }

        if (source.isMagic() || sourceName.contains("poison") || sourceName.contains("wither")) {
            return ConfigManager.get().poisonInterrupts;
        }

        switch (sourceName) {
            case "drown" -> {
                return ConfigManager.get().drowningInterrupts;
            }
            case "inWall" -> {
                return ConfigManager.get().suffocationInterrupts;
            }
            case "starve" -> {
                return ConfigManager.get().starvationInterrupts;
            }
        }

        if (source.isFromFalling()) {
            return ConfigManager.get().fallInterrupts;
        }

        if (source.isProjectile()) {
            return ConfigManager.get().projectileInterrupts;
        }

        if (source.isExplosive()) {
            return ConfigManager.get().explosionInterrupts;
        }

        // Default to melee/other
        return ConfigManager.get().meleeInterrupts;
    }
}