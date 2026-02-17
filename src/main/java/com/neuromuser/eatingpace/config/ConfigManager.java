package com.neuromuser.eatingpace.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig clientConfig = new ModConfig();
    private static ModConfig serverConfig = null;
    private static boolean isServerModPresent = false;
    private static boolean isIntegratedServer = false;
    private static final ThreadLocal<Boolean> isProcessingFoodComponent = ThreadLocal.withInitial(() -> false);

    public static ModConfig get() {
        if (isPhysicalServer()) {
            return clientConfig;
        }

        if (isIntegratedServer) {
            return clientConfig;
        }

        if (isServerModPresent && serverConfig != null) {
            return serverConfig;
        }

        return clientConfig;
    }

    public static ModConfig getClientConfig() {
        return clientConfig;
    }

    public static void load(Path path) {
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                ModConfig loaded = GSON.fromJson(json, ModConfig.class);
                if (loaded != null) {
                    if (loaded.configVersion < ModConfig.CURRENT_VERSION) {
                        System.out.println("[EatingPace] Config version outdated (v" + loaded.configVersion + " -> v" + ModConfig.CURRENT_VERSION + "), resetting to defaults.");
                        clientConfig = new ModConfig();
                        save(path);
                    } else {
                        clientConfig = loaded;
                    }
                } else {
                    System.err.println("Failed to parse config, using defaults");
                    clientConfig = new ModConfig();
                    save(path);
                }
            } else {
                clientConfig = new ModConfig();
                save(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            clientConfig = new ModConfig();
        }
    }

    public static void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(clientConfig));
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public static String toJson() {
        return GSON.toJson(clientConfig);
    }

    public static void receiveServerConfig(String json) {
        try {
            ModConfig received = GSON.fromJson(json, ModConfig.class);
            if (received != null) {
                serverConfig = received;
                isServerModPresent = true;
            }
        } catch (Exception e) {
            System.err.println("Failed to parse server config: " + e.getMessage());
        }
    }

    public static void setIntegratedServer(boolean integrated) {
        isIntegratedServer = integrated;
    }

    public static void clearServerConfig() {
        serverConfig = null;
        isServerModPresent = false;
        isIntegratedServer = false;
    }

    public static boolean hasServerMod() {
        return isServerModPresent;
    }

    public static boolean isPhysicalServer() {
        try {
            return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
        } catch (Exception e) {
            return false;
        }
    }

    public static FoodProperties getFoodProperties(Item item) {
        ModConfig config = get();

        if (!config.general.enableMod) {
            return null;
        }

        String itemId = getItemId(item);

        if (config.vanillaFoods.containsKey(itemId)) {
            ModConfig.VanillaFoodEntry entry = config.vanillaFoods.get(itemId);
            if (entry.enabled) {
                return new FoodProperties(entry, config);
            }
        }

        if (config.moddedFoods.containsKey(itemId)) {
            ModConfig.ModdedFoodEntry entry = config.moddedFoods.get(itemId);
            if (entry.enabled) {
                return new FoodProperties(entry, config);
            }
        }

        if (config.moddedFoodDefaults.enableFallbackLogic) {
            return getFallbackProperties(item, config);
        }

        return null;
    }

    private static FoodProperties getFallbackProperties(Item item, ModConfig config) {
        if (isProcessingFoodComponent.get()) {
            return null;
        }

        isProcessingFoodComponent.set(true);
        try {
            FoodComponent foodComponent = item.getComponents().get(DataComponentTypes.FOOD);
            if (foodComponent == null) {
                return null;
            }

            float baseSaturation = foodComponent.saturation();
            int baseHunger = foodComponent.nutrition();

            int eatingTime = (int) (baseHunger * 6 + baseSaturation * 60);
            eatingTime = Math.max(config.moddedFoodDefaults.minEatingTime,
                    Math.min(config.moddedFoodDefaults.maxEatingTime, eatingTime));

            float scaledSaturation = baseSaturation * config.moddedFoodDefaults.saturationScalingMultiplier
                    * config.general.globalSaturationMultiplier;

            return new FoodProperties(
                    eatingTime,
                    (int) (baseHunger * config.general.globalHungerMultiplier),
                    scaledSaturation,
                    false, 
                    foodComponent.eatSeconds() < 1.0f, 
                    foodComponent.canAlwaysEat(),
                    new java.util.HashMap<>()
            );
        } finally {
            isProcessingFoodComponent.set(false);
        }
    }

    private static String getItemId(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        if (id.getNamespace().equals("minecraft")) {
            return id.getPath();
        }
        return id.toString();
    }

    public static int getEatingTime(ItemStack stack) {
        if (!stack.contains(DataComponentTypes.FOOD)) {
            return 32;
        }

        FoodProperties props = getFoodProperties(stack.getItem());
        if (props != null) {
            return (int) (props.eatingTime * get().general.globalEatingSpeedMultiplier);
        }

        FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
        if (foodComponent != null) {
            return (int) (foodComponent.eatSeconds() * 20);
        }
        return 32;
    }

    public static class FoodProperties {
        public final int eatingTime;
        public final int hunger;
        public final float saturation;
        public final boolean isMeat;
        public final boolean isSnack;
        public final boolean alwaysEdible;
        public final java.util.Map<String, ModConfig.EffectEntry> effects;

        public FoodProperties(ModConfig.VanillaFoodEntry entry, ModConfig config) {
            this.eatingTime = entry.eatingTime;
            this.hunger = (int) (entry.hunger * config.general.globalHungerMultiplier);
            this.saturation = entry.saturation * config.general.globalSaturationMultiplier;
            this.isMeat = entry.isMeat;
            this.isSnack = entry.isSnack;
            this.alwaysEdible = entry.alwaysEdible;
            this.effects = entry.effects;
        }

        public FoodProperties(ModConfig.ModdedFoodEntry entry, ModConfig config) {
            this.eatingTime = entry.eatingTime;
            this.hunger = (int) (entry.hunger * config.general.globalHungerMultiplier);
            this.saturation = entry.saturation * config.general.globalSaturationMultiplier;
            this.isMeat = entry.isMeat;
            this.isSnack = entry.isSnack;
            this.alwaysEdible = entry.alwaysEdible;
            this.effects = entry.effects;
        }

        public FoodProperties(int eatingTime, int hunger, float saturation,
                              boolean isMeat, boolean isSnack, boolean alwaysEdible,
                              java.util.Map<String, ModConfig.EffectEntry> effects) {
            this.eatingTime = eatingTime;
            this.hunger = hunger;
            this.saturation = saturation;
            this.isMeat = isMeat;
            this.isSnack = isSnack;
            this.alwaysEdible = alwaysEdible;
            this.effects = effects != null ? effects : new java.util.HashMap<>();
        }
    }
}