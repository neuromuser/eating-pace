package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.ConfigManager;
import com.neuromuser.eatingpace.config.ModConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createConfigScreen;
    }

    private Screen createConfigScreen(Screen parent) {
        ModConfig config = ConfigManager.getClientConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Eating Pace Configuration"));

        if (ConfigManager.hasServerMod()) {
            builder.setTitle(Text.literal("Eating Pace Config (Server Active - Changes won't apply)"));
        }

        builder.setSavingRunnable(() -> {
            if (!ConfigManager.hasServerMod()) {
                ConfigManager.save(FabricLoader.getInstance().getConfigDir().resolve("eating-pace.json"));
            }
        });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        createGeneralCategory(builder, entryBuilder, config);

        createInterruptionCategory(builder, entryBuilder, config);

        createVanillaFoodCategory(builder, entryBuilder, config);

        createModdedFoodCategory(builder, entryBuilder, config);

        return builder.build();
    }

    private void createGeneralCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig config) {
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("⚙ General"));

        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Enable Mod"),
                        config.general.enableMod)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Master switch for the entire mod"))
                .setSaveConsumer(newValue -> config.general.enableMod = newValue)
                .build());

        general.addEntry(entryBuilder.startFloatField(
                        Text.literal("Saturation Cap"),
                        config.general.saturationCap)
                .setDefaultValue(30.0f)
                .setMin(0.0f)
                .setMax(100.0f)
                .setTooltip(Text.literal("Maximum saturation level (vanilla: 20)"))
                .setSaveConsumer(newValue -> config.general.saturationCap = newValue)
                .build());

        general.addEntry(entryBuilder.startFloatField(
                        Text.literal("Global Hunger Multiplier"),
                        config.general.globalHungerMultiplier)
                .setDefaultValue(1.0f)
                .setMin(0.1f)
                .setMax(5.0f)
                .setTooltip(Text.literal("Multiplier for all food hunger values"))
                .setSaveConsumer(newValue -> config.general.globalHungerMultiplier = newValue)
                .build());

        general.addEntry(entryBuilder.startFloatField(
                        Text.literal("Global Saturation Multiplier"),
                        config.general.globalSaturationMultiplier)
                .setDefaultValue(1.0f)
                .setMin(0.1f)
                .setMax(5.0f)
                .setTooltip(Text.literal("Multiplier for all food saturation values"))
                .setSaveConsumer(newValue -> config.general.globalSaturationMultiplier = newValue)
                .build());

        general.addEntry(entryBuilder.startFloatField(
                        Text.literal("Global Eating Speed Multiplier"),
                        config.general.globalEatingSpeedMultiplier)
                .setDefaultValue(1.0f)
                .setMin(0.1f)
                .setMax(10.0f)
                .setTooltip(Text.literal("Multiplier for eating speed (lower = faster)"))
                .setSaveConsumer(newValue -> config.general.globalEatingSpeedMultiplier = newValue)
                .build());
    }

    private void createInterruptionCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig config) {
        ConfigCategory interruptions = builder.getOrCreateCategory(Text.literal("⚔ Interruptions"));

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Enable Eating Interruption"),
                        config.interruptions.enableEatingInterruption)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Allow damage to interrupt eating"))
                .setSaveConsumer(newValue -> config.interruptions.enableEatingInterruption = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Fire Interrupts"),
                        config.interruptions.fireInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.interruptions.fireInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Poison Interrupts"),
                        config.interruptions.poisonInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.interruptions.poisonInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Fall Interrupts"),
                        config.interruptions.fallInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.interruptions.fallInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Drowning Interrupts"),
                        config.interruptions.drowningInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.interruptions.drowningInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Suffocation Interrupts"),
                        config.interruptions.suffocationInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.interruptions.suffocationInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Starvation Interrupts"),
                        config.interruptions.starvationInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.interruptions.starvationInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Melee Interrupts"),
                        config.interruptions.meleeInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.interruptions.meleeInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Projectile Interrupts"),
                        config.interruptions.projectileInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.interruptions.projectileInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Explosion Interrupts"),
                        config.interruptions.explosionInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.interruptions.explosionInterrupts = newValue)
                .build());
    }

    private void createVanillaFoodCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig config) {
        ConfigCategory vanillaFood = builder.getOrCreateCategory(Text.literal("🍎 Vanilla Foods"));

        vanillaFood.addEntry(entryBuilder.startTextDescription(
                Text.literal("Configure vanilla Minecraft food items.")
        ).build());

        config.vanillaFoods.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> addVanillaFoodEntry(vanillaFood, entryBuilder, entry.getKey(), entry.getValue()));
    }

    private void addVanillaFoodEntry(ConfigCategory category, ConfigEntryBuilder entryBuilder,
                                     String itemId, ModConfig.VanillaFoodEntry entry) {
        String displayName = formatItemName(itemId);


        category.addEntry(entryBuilder.startTextDescription(
                Text.literal("§6" + displayName)
        ).build());

        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("  Enable"),
                        entry.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> entry.enabled = newValue)
                .build());

        category.addEntry(entryBuilder.startIntField(
                        Text.literal("  Eating Time (ticks)"),
                        entry.eatingTime)
                .setDefaultValue(32)
                .setMin(1)
                .setMax(200)
                .setTooltip(Text.literal("Time to eat (20 ticks = 1 second)"))
                .setSaveConsumer(newValue -> entry.eatingTime = newValue)
                .build());

        category.addEntry(entryBuilder.startIntField(
                        Text.literal("  Hunger"),
                        entry.hunger)
                .setDefaultValue(4)
                .setMin(0)
                .setMax(20)
                .setSaveConsumer(newValue -> entry.hunger = newValue)
                .build());

        category.addEntry(entryBuilder.startFloatField(
                        Text.literal("  Saturation"),
                        entry.saturation)
                .setDefaultValue(0.5f)
                .setMin(0.0f)
                .setMax(2.0f)
                .setSaveConsumer(newValue -> entry.saturation = newValue)
                .build());
    }

    private void createModdedFoodCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, ModConfig config) {
        ConfigCategory moddedFood = builder.getOrCreateCategory(Text.literal("🔧 Modded Foods"));

        moddedFood.addEntry(entryBuilder.startTextDescription(
                Text.literal("Configure how modded foods are handled. Fallback logic automatically categorizes foods based on saturation.")
        ).build());

        moddedFood.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("Enable Fallback Logic"),
                        config.moddedFoodDefaults.enableFallbackLogic)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Auto-configure unregistered modded foods"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.enableFallbackLogic = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startFloatField(
                        Text.literal("Saturation Scaling Multiplier"),
                        config.moddedFoodDefaults.saturationScalingMultiplier)
                .setDefaultValue(1.5f)
                .setMin(0.1f)
                .setMax(5.0f)
                .setTooltip(Text.literal("High saturation foods give proportionally more saturation"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.saturationScalingMultiplier = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startTextDescription(
                Text.literal("§6Categorization Thresholds")
        ).build());

        moddedFood.addEntry(entryBuilder.startFloatField(
                        Text.literal("High Saturation Threshold"),
                        config.moddedFoodDefaults.highSaturationThreshold)
                .setDefaultValue(0.8f)
                .setMin(0.0f)
                .setMax(2.0f)
                .setTooltip(Text.literal("Foods above this are 'Meals' (slower eating)"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.highSaturationThreshold = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startFloatField(
                        Text.literal("Low Saturation Threshold"),
                        config.moddedFoodDefaults.lowSaturationThreshold)
                .setDefaultValue(0.4f)
                .setMin(0.0f)
                .setMax(2.0f)
                .setTooltip(Text.literal("Foods below this are 'Snacks' (faster eating)"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.lowSaturationThreshold = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startTextDescription(
                Text.literal("§6Default Eating Times")
        ).build());

        moddedFood.addEntry(entryBuilder.startIntField(
                        Text.literal("Meal Eating Time (ticks)"),
                        config.moddedFoodDefaults.mealEatingTime)
                .setDefaultValue(80)
                .setMin(10)
                .setMax(100)
                .setTooltip(Text.literal("High saturation foods (e.g., steak)"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.mealEatingTime = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startIntField(
                        Text.literal("Normal Eating Time (ticks)"),
                        config.moddedFoodDefaults.normalEatingTime)
                .setDefaultValue(32)
                .setMin(10)
                .setMax(100)
                .setTooltip(Text.literal("Medium saturation foods"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.normalEatingTime = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startIntField(
                        Text.literal("Snack Eating Time (ticks)"),
                        config.moddedFoodDefaults.snackEatingTime)
                .setDefaultValue(16)
                .setMin(10)
                .setMax(100)
                .setTooltip(Text.literal("Low saturation foods (e.g., cookies)"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.snackEatingTime = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startTextDescription(
                Text.literal("§6Hard Limits")
        ).build());

        moddedFood.addEntry(entryBuilder.startIntField(
                        Text.literal("Minimum Eating Time (ticks)"),
                        config.moddedFoodDefaults.minEatingTime)
                .setDefaultValue(10)
                .setMin(1)
                .setMax(50)
                .setTooltip(Text.literal("No food can be eaten faster than this (0.5s)"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.minEatingTime = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startIntField(
                        Text.literal("Maximum Eating Time (ticks)"),
                        config.moddedFoodDefaults.maxEatingTime)
                .setDefaultValue(100)
                .setMin(50)
                .setMax(200)
                .setTooltip(Text.literal("No food can take longer than this to eat (5.0s)"))
                .setSaveConsumer(newValue -> config.moddedFoodDefaults.maxEatingTime = newValue)
                .build());

        moddedFood.addEntry(entryBuilder.startTextDescription(
                Text.literal("§6§lRegistered Modded Foods")
        ).build());

        if (config.moddedFoods.isEmpty()) {
            moddedFood.addEntry(entryBuilder.startTextDescription(
                    Text.literal("§7No modded foods registered yet. Add them manually to the config file.")
            ).build());
        } else {
            for (Map.Entry<String, ModConfig.ModdedFoodEntry> foodEntry : config.moddedFoods.entrySet()) {
                addModdedFoodEntry(moddedFood, entryBuilder, foodEntry.getKey(), foodEntry.getValue());
            }
        }
    }

    private void addModdedFoodEntry(ConfigCategory category, ConfigEntryBuilder entryBuilder,
                                    String itemId, ModConfig.ModdedFoodEntry entry) {
        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.literal("  Enable " + itemId),
                        entry.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> entry.enabled = newValue)
                .build());

        category.addEntry(entryBuilder.startIntField(
                        Text.literal("    ├ Eating Time (ticks)"),
                        entry.eatingTime)
                .setDefaultValue(32)
                .setMin(1)
                .setMax(200)
                .setSaveConsumer(newValue -> entry.eatingTime = newValue)
                .build());

        category.addEntry(entryBuilder.startIntField(
                        Text.literal("    ├ Hunger"),
                        entry.hunger)
                .setDefaultValue(4)
                .setMin(0)
                .setMax(20)
                .setSaveConsumer(newValue -> entry.hunger = newValue)
                .build());

        category.addEntry(entryBuilder.startFloatField(
                        Text.literal("    └ Saturation"),
                        entry.saturation)
                .setDefaultValue(0.5f)
                .setMin(0.0f)
                .setMax(2.0f)
                .setSaveConsumer(newValue -> entry.saturation = newValue)
                .build());
    }

    private String formatItemName(String itemId) {
        return itemId.replace("_", " ")
                .substring(0, 1).toUpperCase() +
                itemId.replace("_", " ").substring(1);
    }
}