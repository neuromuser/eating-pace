package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemEatFoodMixin {

    @ModifyArg(
            method = "finishUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/FoodComponent;)Lnet/minecraft/item/ItemStack;"
            ),
            index = 2
    )
    private FoodComponent modifyFoodComponent(FoodComponent original) {
        if (!ConfigManager.get().general.enableMod) {
            return original;
        }

        Item thisItem = (Item) (Object) this;
        ConfigManager.FoodProperties props = ConfigManager.getFoodProperties(thisItem);

        if (props != null) {
            FoodComponent.Builder builder = new FoodComponent.Builder()
                    .nutrition(props.hunger)
                    .saturationModifier(props.saturation);

            if (props.alwaysEdible) builder.alwaysEdible();
            if (props.isSnack) builder.snack();

            if (props.effects != null && !props.effects.isEmpty()) {
                for (var entry : props.effects.entrySet()) {
                    Identifier id = Identifier.tryParse(entry.getKey());
                    if (id != null) {
                        var effectRegistry = Registries.STATUS_EFFECT;
                        Optional<RegistryEntry.Reference<net.minecraft.entity.effect.StatusEffect>> effectEntry =
                                effectRegistry.getReadOnlyWrapper().getOptional(RegistryKey.of(effectRegistry.getKey(), id));

                        if (effectEntry.isPresent()) {
                            StatusEffectInstance instance = new StatusEffectInstance(
                                    effectEntry.get(),
                                    entry.getValue().duration,
                                    entry.getValue().amplifier,
                                    false,
                                    false
                            );
                            builder.statusEffect(instance, entry.getValue().chance);
                        }
                    }
                }
            }

            return builder.build();
        }

        return original;
    }
}