package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Item.class)
public abstract class FoodComponentMixin {

    @ModifyReturnValue(method = "getFoodComponent", at = @At("RETURN"))
    private FoodComponent modifyFoodComponent(FoodComponent original) {
        Item thisItem = (Item)(Object)this;

        ConfigManager.FoodProperties props = ConfigManager.getFoodProperties(thisItem);

        if (props != null && original != null) {
            FoodComponent.Builder builder = new FoodComponent.Builder()
                    .hunger(props.hunger)
                    .saturationModifier(props.saturation);

            if (props.isMeat) {
                builder.meat();
            }
            if (props.isSnack) {
                builder.snack();
            }
            if (props.alwaysEdible) {
                builder.alwaysEdible();
            }

            if (props.effects != null && !props.effects.isEmpty()) {
                for (var effectEntry : props.effects.entrySet()) {
                    try {
                        String effectId = effectEntry.getKey();
                        var effectData = effectEntry.getValue();

                        Identifier effectIdentifier = Identifier.tryParse(effectId);
                        if (effectIdentifier != null) {
                            StatusEffect statusEffect = Registries.STATUS_EFFECT.get(effectIdentifier);
                            if (statusEffect != null) {
                                StatusEffectInstance effect = new StatusEffectInstance(
                                        statusEffect,
                                        effectData.duration,
                                        effectData.amplifier,
                                        false,
                                        false
                                );
                                builder.statusEffect(effect, effectData.chance);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }

            return builder.build();
        }

        return original;
    }
}