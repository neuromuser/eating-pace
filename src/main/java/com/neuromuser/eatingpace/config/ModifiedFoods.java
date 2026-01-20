package com.neuromuser.eatingpace.config;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModifiedFoods {
    private static final Map<Item, CustomFoodComponent> CUSTOM_FOODS = new HashMap<>();

    static {
        register(Items.APPLE, new CustomFoodComponent(4, 0.5f).eatTicks(26));

        register(Items.BAKED_POTATO, new CustomFoodComponent(6, 0.9f).eatTicks(90).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 0), 0.4f));

        register(Items.BEEF, new CustomFoodComponent(2, 0.2f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 500, 0), 0.5f).eatTicks(24));

        register(Items.BEETROOT, new CustomFoodComponent(2, 0.6f).eatTicks(18).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80, 0), 0.6f));

        register(Items.BEETROOT_SOUP, new CustomFoodComponent(8, 0.85f).eatTicks(80));

        register(Items.BREAD, new CustomFoodComponent(6, 0.7f).eatTicks(100));

        register(Items.CARROT, new CustomFoodComponent(4, 0.5f).eatTicks(16).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 0), 0.8f).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 0), 0.4f));

        register(Items.CHICKEN, new CustomFoodComponent(2, 0.2f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 700, 0), 0.5f).eatTicks(24));

        register(Items.CHORUS_FRUIT, new CustomFoodComponent(3, 0.3f).alwaysEdible().eatTicks(20));

        register(Items.COD, new CustomFoodComponent(2, 0.1f).eatTicks(20));

        register(Items.COOKED_BEEF, new CustomFoodComponent(9, 1.2f).meat().eatTicks(110));

        register(Items.COOKED_CHICKEN, new CustomFoodComponent(7, 0.8f).meat().eatTicks(60).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 400, 0), 0.6f).statusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0), 0.3f));

        register(Items.COOKED_COD, new CustomFoodComponent(5, 0.6f).eatTicks(50));

        register(Items.COOKED_MUTTON, new CustomFoodComponent(7, 1.0f).meat().eatTicks(95));

        register(Items.COOKED_PORKCHOP, new CustomFoodComponent(8, 1.1f).meat().eatTicks(105));

        register(Items.COOKED_RABBIT, new CustomFoodComponent(6, 0.75f).meat().eatTicks(55).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 0), 0.5f).statusEffect(new StatusEffectInstance(StatusEffects.LUCK, 600, 0), 0.4f));

        register(Items.COOKED_SALMON, new CustomFoodComponent(7, 0.9f).eatTicks(65).statusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 400, 0), 0.4f));

        register(Items.COOKIE, new CustomFoodComponent(2, 0.1f).snack().statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 140, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 140, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 100, 0), 0.5f));

        register(Items.DRIED_KELP, new CustomFoodComponent(1, 0.3f).snack().statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 200, 0), 0.3f));

        register(Items.ENCHANTED_GOLDEN_APPLE, new CustomFoodComponent(4, 1.2f)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3), 1.0f)
                .alwaysEdible()
                .eatTicks(48));

        register(Items.GOLDEN_APPLE, new CustomFoodComponent(4, 1.2f)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0), 1.0f)
                .alwaysEdible()
                .eatTicks(40));

        register(Items.GOLDEN_CARROT, new CustomFoodComponent(4, 0.4f).eatTicks(14).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 800, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 0), 0.5f));

        register(Items.HONEY_BOTTLE, new CustomFoodComponent(3, 0.2f).eatTicks(18).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 0), 1.0f));

        register(Items.MELON_SLICE, new CustomFoodComponent(2, 0.25f).snack().statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0), 0.3f));

        register(Items.MUSHROOM_STEW, new CustomFoodComponent(7, 0.8f).eatTicks(75));

        register(Items.MUTTON, new CustomFoodComponent(2, 0.2f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 500, 0), 0.5f).eatTicks(24));

        register(Items.POISONOUS_POTATO, new CustomFoodComponent(2, 0.2f).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0), 0.7f).eatTicks(20));

        register(Items.PORKCHOP, new CustomFoodComponent(3, 0.2f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 500, 0), 0.5f).eatTicks(24));

        register(Items.POTATO, new CustomFoodComponent(3, 0.6f).eatTicks(18).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 160, 0), 0.6f));

        register(Items.PUFFERFISH, new CustomFoodComponent(1, 0.1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 1200, 1), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 2), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1.0f)
                .eatTicks(16));

        register(Items.PUMPKIN_PIE, new CustomFoodComponent(9, 1.0f).eatTicks(85));

        register(Items.RABBIT, new CustomFoodComponent(3, 0.2f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 500, 0), 0.5f).eatTicks(24));

        register(Items.RABBIT_STEW, new CustomFoodComponent(11, 1.15f).eatTicks(90).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 240, 0), 1.0f));

        register(Items.ROTTEN_FLESH, new CustomFoodComponent(3, 0.05f).meat().statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.85f).eatTicks(32));

        register(Items.SALMON, new CustomFoodComponent(2, 0.1f).eatTicks(20));

        register(Items.SPIDER_EYE, new CustomFoodComponent(2, 0.4f).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0), 1.0f).eatTicks(16));

        register(Items.SUSPICIOUS_STEW, new CustomFoodComponent(6, 0.7f).alwaysEdible().eatTicks(24));

        register(Items.SWEET_BERRIES, new CustomFoodComponent(1, 0.2f).snack().statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 160, 0), 0.6f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0), 0.3f));

        register(Items.GLOW_BERRIES, new CustomFoodComponent(1, 0.2f).snack().statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 800, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 400, 0), 0.3f));

        register(Items.TROPICAL_FISH, new CustomFoodComponent(1, 0.1f).eatTicks(14).statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0), 0.9f).statusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 300, 0), 0.5f));
    }

    private static void register(Item item, CustomFoodComponent component) {
        CUSTOM_FOODS.put(item, component);
    }

    public static CustomFoodComponent getCustomFood(Item item) {
        return CUSTOM_FOODS.get(item);
    }

    public static boolean hasCustomFood(Item item) {
        return CUSTOM_FOODS.containsKey(item);
    }

    public static Map<Item, CustomFoodComponent> getAllCustomFoods() {
        return CUSTOM_FOODS;
    }
}