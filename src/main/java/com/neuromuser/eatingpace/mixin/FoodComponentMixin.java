package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.neuromuser.eatingpace.config.ConfigManager;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Item.class)
public abstract class FoodComponentMixin {

    @ModifyReturnValue(method = "getComponents", at = @At("RETURN"))
    private ComponentMap modifyFoodComponent(ComponentMap original) {
        Item thisItem = (Item) (Object) this;

        // Only modify if the item is actually food
        if (!original.contains(DataComponentTypes.FOOD)) {
            return original;
        }

        ConfigManager.FoodProperties props = ConfigManager.getFoodProperties(thisItem);

        if (props != null) {
            // Use the Builder from the source code you provided
            FoodComponent.Builder builder = new FoodComponent.Builder()
                    .nutrition(props.hunger)
                    // Note: Your source shows the parameter name is saturationModifier
                    .saturationModifier(props.saturation);

            if (props.alwaysEdible) {
                builder.alwaysEdible();
            }

            if (props.isSnack) {
                builder.snack();
            }

            // Meat: The 1.21.1 FoodComponent no longer has a .meat() method.
            // If you need it to behave like meat (for wolves), that is now handled
            // by adding the item to the 'minecraft:meat' item tag via JSON.

            if (props.effects != null && !props.effects.isEmpty()) {
                for (var entry : props.effects.entrySet()) {
                    String effectId = entry.getKey();
                    var data = entry.getValue(); // Correctly accessing the value

                    Identifier identifier = Identifier.tryParse(effectId);
                    if (identifier != null) {
                        // Get the RegistryEntry required for StatusEffectInstance
                        var effectRegistry = Registries.STATUS_EFFECT;
                        Optional<RegistryEntry.Reference<net.minecraft.entity.effect.StatusEffect>> effectEntry =
                                effectRegistry.getReadOnlyWrapper().getOptional(RegistryKey.of(effectRegistry.getKey(), identifier));

                        if (effectEntry.isPresent()) {
                            StatusEffectInstance instance = new StatusEffectInstance(
                                    effectEntry.get(),
                                    data.duration,
                                    data.amplifier,
                                    false,
                                    false
                            );
                            // Matches the builder.statusEffect(StatusEffectInstance, float) in your source
                            builder.statusEffect(instance, data.chance);
                        }
                    }
                }
            }

            // Return a new map with our modified FoodComponent
            return ComponentMap.builder()
                    .addAll(original)
                    .add(DataComponentTypes.FOOD, builder.build())
                    .build();
        }

        return original;
    }
}