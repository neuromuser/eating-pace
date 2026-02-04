package com.neuromuser.eatingpace.config;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomFoodComponent {
    private final int hunger;
    private final float saturation;
    private boolean meat = false; 
    private boolean alwaysEdible = false;
    private float eatSeconds = 1.6f; 
    private final List<StatusEffectEntry> effects = new ArrayList<>();

    public static class StatusEffectEntry {
        public StatusEffectInstance effect;
        public float chance;

        public StatusEffectEntry(StatusEffectInstance effect, float chance) {
            this.effect = effect;
            this.chance = chance;
        }
    }

    public CustomFoodComponent(int hunger, float saturation) {
        this.hunger = hunger;
        this.saturation = saturation;
    }

    public CustomFoodComponent meat() {
        this.meat = true;
        return this;
    }

    public CustomFoodComponent alwaysEdible() {
        this.alwaysEdible = true;
        return this;
    }

    public CustomFoodComponent snack() {
        this.eatSeconds = 0.8f; 
        return this;
    }

    public CustomFoodComponent eatTicks(int ticks) {
        this.eatSeconds = ticks / 20.0f;
        return this;
    }

    public CustomFoodComponent statusEffect(StatusEffectInstance effect, float chance) {
        this.effects.add(new StatusEffectEntry(effect, chance));
        return this;
    }

    public int getEatTicks() { return (int) (eatSeconds * 20); }

    public FoodComponent toVanillaComponent() {
        float calculatedSaturation = HungerConstants.calculateSaturation(this.hunger, this.saturation);

        List<FoodComponent.StatusEffectEntry> vanillaEffects = effects.stream()
                .map(e -> new FoodComponent.StatusEffectEntry(e.effect, e.chance))
                .toList();

        return new FoodComponent(
                this.hunger,
                calculatedSaturation,
                this.alwaysEdible,
                this.eatSeconds,
                Optional.empty(),
                vanillaEffects
        );
    }
}