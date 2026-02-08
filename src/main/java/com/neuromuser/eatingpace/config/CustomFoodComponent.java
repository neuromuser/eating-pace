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
    private boolean snack = false;
    private int eatTicks = 32;
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
        this.snack = true;
        this.eatTicks = 16;
        return this;
    }

    public CustomFoodComponent eatTicks(int ticks) {
        this.eatTicks = ticks;
        return this;
    }

    public CustomFoodComponent statusEffect(StatusEffectInstance effect, float chance) {
        this.effects.add(new StatusEffectEntry(effect, chance));
        return this;
    }

    public int getHunger() { return hunger; }
    public float getSaturation() { return saturation; }
    public boolean isMeat() { return meat; }
    public boolean isAlwaysEdible() { return alwaysEdible; }
    public boolean isSnack() { return snack; }
    public int getEatTicks() { return eatTicks; }
    public List<StatusEffectEntry> getEffects() { return effects; }

    public FoodComponent toVanillaComponent() {
        FoodComponent.Builder builder = new FoodComponent.Builder()
                .nutrition(this.hunger)
                .saturationModifier(saturation);

        if (alwaysEdible) builder.alwaysEdible();
        if (snack) builder.snack();

        for (StatusEffectEntry entry : effects) {
            builder.statusEffect(entry.effect, entry.chance);
        }

        return builder.build();
    }
}