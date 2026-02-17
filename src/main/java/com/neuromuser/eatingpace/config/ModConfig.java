package com.neuromuser.eatingpace.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    public static final int CURRENT_VERSION = 1;

    @SerializedName("configVersion")
    public int configVersion = CURRENT_VERSION;

    @SerializedName("general")
    public GeneralConfig general = new GeneralConfig();

    @SerializedName("vanillaFoods")
    public Map<String, VanillaFoodEntry> vanillaFoods = new HashMap<>();

    @SerializedName("moddedFoods")
    public Map<String, ModdedFoodEntry> moddedFoods = new HashMap<>();

    @SerializedName("moddedFoodDefaults")
    public ModdedFoodDefaults moddedFoodDefaults = new ModdedFoodDefaults();

    @SerializedName("interruptions")
    public InterruptionConfig interruptions = new InterruptionConfig();

    public ModConfig() {
        initializeVanillaFoods();
        initializeModdedFoods();
    }


    public static class GeneralConfig {
        @SerializedName("enableMod")
        public boolean enableMod = true;

        @SerializedName("saturationCap")
        public float saturationCap = 30.0f;

        @SerializedName("globalHungerMultiplier")
        public float globalHungerMultiplier = 1.0f;

        @SerializedName("globalSaturationMultiplier")
        public float globalSaturationMultiplier = 1.0f;

        @SerializedName("globalEatingSpeedMultiplier")
        public float globalEatingSpeedMultiplier = 1.0f;
    }

    public static class VanillaFoodEntry {
        @SerializedName("enabled")
        public boolean enabled = true;

        @SerializedName("eatingTime")
        public int eatingTime = 32;

        @SerializedName("hunger")
        public int hunger = 4;

        @SerializedName("saturation")
        public float saturation = 0.5f;

        @SerializedName("isMeat")
        public boolean isMeat = false;

        @SerializedName("isSnack")
        public boolean isSnack = false;

        @SerializedName("alwaysEdible")
        public boolean alwaysEdible = false;

        @SerializedName("effects")
        public Map<String, EffectEntry> effects = new HashMap<>();

        public VanillaFoodEntry() {}

        public VanillaFoodEntry(int eatingTime, int hunger, float saturation) {
            this.eatingTime = eatingTime;
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public VanillaFoodEntry meat() {
            this.isMeat = true;
            return this;
        }

        public VanillaFoodEntry snack() {
            this.isSnack = true;
            this.eatingTime = 16;
            return this;
        }

        public VanillaFoodEntry alwaysEdible() {
            this.alwaysEdible = true;
            return this;
        }

        public VanillaFoodEntry addEffect(String effectId, float chance, int duration, int amplifier) {
            this.effects.put(effectId, new EffectEntry(chance, duration, amplifier));
            return this;
        }
    }

    public static class ModdedFoodEntry {
        @SerializedName("enabled")
        public boolean enabled = true;

        @SerializedName("eatingTime")
        public int eatingTime = 32;

        @SerializedName("hunger")
        public int hunger = 4;

        @SerializedName("saturation")
        public float saturation = 0.5f;

        @SerializedName("isMeat")
        public boolean isMeat = false;

        @SerializedName("isSnack")
        public boolean isSnack = false;

        @SerializedName("alwaysEdible")
        public boolean alwaysEdible = false;

        @SerializedName("effects")
        public Map<String, EffectEntry> effects = new HashMap<>();

        public ModdedFoodEntry() {}

        public ModdedFoodEntry(int eatingTime, int hunger, float saturation) {
            this.eatingTime = eatingTime;
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public ModdedFoodEntry meat() {
            this.isMeat = true;
            return this;
        }
    }

    public static class EffectEntry {
        @SerializedName("chance")
        public float chance = 1.0f;

        @SerializedName("duration")
        public int duration = 100;

        @SerializedName("amplifier")
        public int amplifier = 0;

        public EffectEntry() {}

        public EffectEntry(float chance, int duration, int amplifier) {
            this.chance = chance;
            this.duration = duration;
            this.amplifier = amplifier;
        }
    }

    public static class ModdedFoodDefaults {
        @SerializedName("enableFallbackLogic")
        public boolean enableFallbackLogic = true;

        @SerializedName("saturationScalingMultiplier")
        public float saturationScalingMultiplier = 2.0f;

        @SerializedName("minEatingTime")
        public int minEatingTime = 8;

        @SerializedName("maxEatingTime")
        public int maxEatingTime = 100;
    }

    public static class InterruptionConfig {
        @SerializedName("enableEatingInterruption")
        public boolean enableEatingInterruption = true;

        @SerializedName("fireInterrupts")
        public boolean fireInterrupts = false;

        @SerializedName("poisonInterrupts")
        public boolean poisonInterrupts = false;

        @SerializedName("fallInterrupts")
        public boolean fallInterrupts = true;

        @SerializedName("drowningInterrupts")
        public boolean drowningInterrupts = false;

        @SerializedName("suffocationInterrupts")
        public boolean suffocationInterrupts = true;

        @SerializedName("starvationInterrupts")
        public boolean starvationInterrupts = false;

        @SerializedName("meleeInterrupts")
        public boolean meleeInterrupts = true;

        @SerializedName("projectileInterrupts")
        public boolean projectileInterrupts = true;

        @SerializedName("explosionInterrupts")
        public boolean explosionInterrupts = true;
    }


    private void initializeModdedFoods() {
        moddedFoods.put("artifacts:eternal_steak", new ModdedFoodEntry(110, 8, 0.8f).meat());
    }

    private void initializeVanillaFoods() {
        addVanillaFood("apple", new VanillaFoodEntry(26, 4, 0.5f));

        addVanillaFood("baked_potato", new VanillaFoodEntry(90, 6, 0.9f)
                .addEffect("minecraft:resistance", 0.4f, 300, 0));

        addVanillaFood("beef", new VanillaFoodEntry(24, 2, 0.2f)
                .meat()
                .addEffect("minecraft:hunger", 0.5f, 500, 0));

        addVanillaFood("beetroot", new VanillaFoodEntry(18, 2, 0.6f)
                .addEffect("minecraft:regeneration", 0.6f, 80, 0));

        addVanillaFood("beetroot_soup", new VanillaFoodEntry(80, 8, 0.85f));

        addVanillaFood("bread", new VanillaFoodEntry(100, 6, 0.7f));

        addVanillaFood("carrot", new VanillaFoodEntry(16, 4, 0.5f)
                .addEffect("minecraft:speed", 0.8f, 300, 0)
                .addEffect("minecraft:jump_boost", 0.4f, 200, 0));

        addVanillaFood("chicken", new VanillaFoodEntry(24, 2, 0.2f)
                .meat()
                .addEffect("minecraft:hunger", 0.5f, 700, 0));

        addVanillaFood("chorus_fruit", new VanillaFoodEntry(20, 3, 0.3f)
                .alwaysEdible());

        addVanillaFood("cod", new VanillaFoodEntry(20, 2, 0.1f));

        addVanillaFood("cooked_beef", new VanillaFoodEntry(110, 9, 1.2f)
                .meat());

        addVanillaFood("cooked_chicken", new VanillaFoodEntry(60, 7, 0.8f)
                .meat()
                .addEffect("minecraft:jump_boost", 0.6f, 400, 0)
                .addEffect("minecraft:slow_falling", 0.3f, 200, 0));

        addVanillaFood("cooked_cod", new VanillaFoodEntry(50, 5, 0.6f));

        addVanillaFood("cooked_mutton", new VanillaFoodEntry(95, 7, 1.0f)
                .meat());

        addVanillaFood("cooked_porkchop", new VanillaFoodEntry(105, 8, 1.1f)
                .meat());

        addVanillaFood("cooked_rabbit", new VanillaFoodEntry(55, 6, 0.75f)
                .meat()
                .addEffect("minecraft:speed", 0.5f, 300, 0)
                .addEffect("minecraft:luck", 0.4f, 600, 0));

        addVanillaFood("cooked_salmon", new VanillaFoodEntry(65, 7, 0.9f)
                .addEffect("minecraft:dolphins_grace", 0.4f, 400, 0));

        addVanillaFood("cookie", new VanillaFoodEntry(8, 2, 0.1f)
                .snack()
                .addEffect("minecraft:speed", 0.2f, 140, 0)
                .addEffect("minecraft:haste", 0.2f, 140, 0)
                .addEffect("minecraft:jump_boost", 0.2f, 100, 0));

        addVanillaFood("dried_kelp", new VanillaFoodEntry(8, 4, 0.2f)
                .snack());

        addVanillaFood("enchanted_golden_apple", new VanillaFoodEntry(48, 4, 1.2f)
                .alwaysEdible()
                .addEffect("minecraft:regeneration", 1.0f, 400, 1)
                .addEffect("minecraft:resistance", 1.0f, 6000, 0)
                .addEffect("minecraft:fire_resistance", 1.0f, 6000, 0)
                .addEffect("minecraft:absorption", 1.0f, 2400, 3));

        addVanillaFood("golden_apple", new VanillaFoodEntry(40, 4, 1.2f)
                .alwaysEdible()
                .addEffect("minecraft:regeneration", 1.0f, 100, 1)
                .addEffect("minecraft:absorption", 1.0f, 2400, 0));

        addVanillaFood("golden_carrot", new VanillaFoodEntry(14, 4, 0.4f)
                .addEffect("minecraft:speed", 1.0f, 200, 1)
                .addEffect("minecraft:absorption", 1.0f, 800, 0));

        addVanillaFood("glow_berries", new VanillaFoodEntry(8, 1, 0.2f)
                .snack()
                .addEffect("minecraft:night_vision", 1.0f, 800, 0)
                .addEffect("minecraft:glowing", 0.3f, 400, 0));

        addVanillaFood("honey_bottle", new VanillaFoodEntry(18, 3, 0.2f)
                .addEffect("minecraft:regeneration", 1.0f, 120, 0));

        addVanillaFood("melon_slice", new VanillaFoodEntry(8, 2, 0.25f)
                .snack()
                .addEffect("minecraft:fire_resistance", 0.3f, 200, 0));

        addVanillaFood("mushroom_stew", new VanillaFoodEntry(75, 7, 0.8f));

        addVanillaFood("mutton", new VanillaFoodEntry(24, 2, 0.2f)
                .meat()
                .addEffect("minecraft:hunger", 0.5f, 500, 0));

        addVanillaFood("poisonous_potato", new VanillaFoodEntry(20, 2, 0.2f)
                .addEffect("minecraft:poison", 0.7f, 100, 0));

        addVanillaFood("porkchop", new VanillaFoodEntry(24, 3, 0.2f)
                .meat()
                .addEffect("minecraft:hunger", 0.5f, 500, 0));

        addVanillaFood("potato", new VanillaFoodEntry(18, 3, 0.6f)
                .addEffect("minecraft:resistance", 0.6f, 160, 0));

        addVanillaFood("pufferfish", new VanillaFoodEntry(16, 1, 0.1f)
                .addEffect("minecraft:poison", 1.0f, 1200, 1)
                .addEffect("minecraft:hunger", 1.0f, 300, 2)
                .addEffect("minecraft:nausea", 1.0f, 300, 0));

        addVanillaFood("pumpkin_pie", new VanillaFoodEntry(85, 9, 1.0f));

        addVanillaFood("rabbit", new VanillaFoodEntry(24, 3, 0.2f)
                .meat()
                .addEffect("minecraft:hunger", 0.5f, 500, 0));

        addVanillaFood("rabbit_stew", new VanillaFoodEntry(90, 11, 1.15f)
                .addEffect("minecraft:regeneration", 1.0f, 240, 0));

        addVanillaFood("rotten_flesh", new VanillaFoodEntry(32, 3, 0.05f)
                .meat()
                .addEffect("minecraft:hunger", 0.85f, 600, 0));

        addVanillaFood("salmon", new VanillaFoodEntry(20, 2, 0.1f));

        addVanillaFood("spider_eye", new VanillaFoodEntry(16, 2, 0.4f)
                .addEffect("minecraft:poison", 1.0f, 100, 0));

        addVanillaFood("suspicious_stew", new VanillaFoodEntry(24, 6, 0.7f)
                .alwaysEdible());

        addVanillaFood("sweet_berries", new VanillaFoodEntry(8, 1, 0.2f)
                .snack()
                .addEffect("minecraft:resistance", 0.6f, 160, 0)
                .addEffect("minecraft:regeneration", 0.3f, 60, 0));

        addVanillaFood("tropical_fish", new VanillaFoodEntry(14, 1, 0.1f)
                .addEffect("minecraft:water_breathing", 0.9f, 600, 0)
                .addEffect("minecraft:dolphins_grace", 0.5f, 300, 0));
    }

    private void addVanillaFood(String itemId, VanillaFoodEntry entry) {
        vanillaFoods.put(itemId, entry);
    }
}