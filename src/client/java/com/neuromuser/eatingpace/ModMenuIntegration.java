package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.Config;
import com.neuromuser.eatingpace.config.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createConfigScreen;
    }

    private Screen createConfigScreen(Screen parent) {
        Config config = ConfigManager.getClientConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Eating Pace Config"));

        if (ConfigManager.hasServerMod()) {
            builder.setTitle(Text.literal("Eating Pace Config (Server Active - Changes won't apply)"));
        }

        builder.setSavingRunnable(() -> {
            if (!ConfigManager.hasServerMod()) {
                ConfigManager.save(FabricLoader.getInstance().getConfigDir().resolve("eating-pace.json"));
            }
        });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        var category = builder.getOrCreateCategory(Text.literal("Settings"));

        category.addEntry(entryBuilder.startFloatField(Text.literal("Saturation Cap"), config.saturationCap)
                .setDefaultValue(30.0f)
                .setSaveConsumer(newValue -> config.saturationCap = newValue)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Custom Food Values"), config.enableCustomFoodValues)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableCustomFoodValues = newValue)
                .build());

        category.addEntry(entryBuilder.startFloatField(Text.literal("Eating Speed Multiplier"), config.eatingSpeedMultiplier)
                .setDefaultValue(1.0f)
                .setSaveConsumer(newValue -> config.eatingSpeedMultiplier = newValue)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Eating Interruption"), config.enableEatingInterruption)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableEatingInterruption = newValue)
                .build());

        var interruptions = builder.getOrCreateCategory(Text.literal("Interruption Sources"));

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Fire Interrupts"), config.fireInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.fireInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Poison Interrupts"), config.poisonInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.poisonInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Fall Interrupts"), config.fallInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.fallInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Drowning Interrupts"), config.drowningInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.drowningInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Suffocation Interrupts"), config.suffocationInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.suffocationInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Starvation Interrupts"), config.starvationInterrupts)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.starvationInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Melee Interrupts"), config.meleeInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.meleeInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Projectile Interrupts"), config.projectileInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.projectileInterrupts = newValue)
                .build());

        interruptions.addEntry(entryBuilder.startBooleanToggle(Text.literal("Explosion Interrupts"), config.explosionInterrupts)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.explosionInterrupts = newValue)
                .build());

        return builder.build();
    }
}