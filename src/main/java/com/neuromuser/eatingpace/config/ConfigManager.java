package com.neuromuser.eatingpace.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig clientConfig = new ModConfig();
    private static ModConfig serverConfig = null;
    private static boolean isServerModPresent = false;
    private static boolean isIntegratedServer = false;
    private static final ThreadLocal<Boolean> isProcessingFoodComponent = ThreadLocal.withInitial(() -> false);

    /**
     * Get the active configuration (server config takes priority when available)
     */
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

    /**
     * Get client config specifically (for GUI editing)
     */
    public static ModConfig getClientConfig() {
        return clientConfig;
    }

    /**
     * Load config from file
     */
    public static void load(Path path) {
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                ModConfig loaded = GSON.fromJson(json, ModConfig.class);
                if (loaded != null) {
                    clientConfig = loaded;
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

    /**
     * Save config to file
     */
    public static void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(clientConfig));
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    /**
     * Convert config to JSON string
     */
    public static String toJson() {
        return GSON.toJson(clientConfig);
    }

    /**
     * Receive server config via network
     */
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

    /**
     * Mark as integrated server
     */
    public static void setIntegratedServer(boolean integrated) {
        isIntegratedServer = integrated;
    }

    /**
     * Clear server config (on disconnect)
     */
    public static void clearServerConfig() {
        serverConfig = null;
        isServerModPresent = false;
        isIntegratedServer = false;
    }

    /**
     * Check if server has the mod installed
     */
    public static boolean hasServerMod() {
        return isServerModPresent;
    }

    /**
     * Check if running on physical server
     */
    public static boolean isPhysicalServer() {
        try {
            return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== FOOD PROPERTY GETTERS ====================

    /**
     * Get food entry for an item (vanilla or modded)
     */
    public static FoodProperties getFoodProperties(Item item) {
        ModConfig config = get();

        if (!config.general.enableMod) {
            return null;
        }

        String itemId = getItemId(item);

        // Check vanilla foods first
        if (config.vanillaFoods.containsKey(itemId)) {
            ModConfig.VanillaFoodEntry entry = config.vanillaFoods.get(itemId);
            if (entry.enabled) {
                return new FoodProperties(entry);
            }
        }

        // Check modded foods
        if (config.moddedFoods.containsKey(itemId)) {
            ModConfig.ModdedFoodEntry entry = config.moddedFoods.get(itemId);
            if (entry.enabled) {
                return new FoodProperties(entry);
            }
        }

        // Fallback logic for unregistered modded foods
        if (config.moddedFoodDefaults.enableFallbackLogic && item.isFood()) {
            return getFallbackProperties(item, config);
        }

        return null;
    }

    /**
     * Calculate fallback properties for modded foods
     */
    private static FoodProperties getFallbackProperties(Item item, ModConfig config) {
        // Check if we're already processing this item to prevent recursion
        if (isProcessingFoodComponent.get()) {
            return null;
        }

        isProcessingFoodComponent.set(true);
        try {
            var foodComponent = item.getFoodComponent();
            if (foodComponent == null) {
                return null;
            }

            float baseSaturation = foodComponent.getSaturationModifier();
            int baseHunger = foodComponent.getHunger();

            // Determine eating time based on saturation
            int eatingTime;
            if (baseSaturation >= config.moddedFoodDefaults.highSaturationThreshold) {
                eatingTime = config.moddedFoodDefaults.mealEatingTime;
            } else if (baseSaturation <= config.moddedFoodDefaults.lowSaturationThreshold) {
                eatingTime = config.moddedFoodDefaults.snackEatingTime;
            } else {
                eatingTime = config.moddedFoodDefaults.normalEatingTime;
            }

            // Apply hard caps
            eatingTime = Math.max(config.moddedFoodDefaults.minEatingTime,
                    Math.min(config.moddedFoodDefaults.maxEatingTime, eatingTime));

            // Apply saturation scaling
            float scaledSaturation = baseSaturation * config.moddedFoodDefaults.saturationScalingMultiplier;

            return new FoodProperties(
                    eatingTime,
                    (int)(baseHunger * config.general.globalHungerMultiplier),
                    scaledSaturation * config.general.globalSaturationMultiplier,
                    foodComponent.isMeat(),
                    foodComponent.isSnack(),
                    foodComponent.isAlwaysEdible()
            );
        } finally {
            isProcessingFoodComponent.set(false);
        }
    }

    /**
     * Get item ID as string
     */
    private static String getItemId(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        if (id.getNamespace().equals("minecraft")) {
            return id.getPath(); // Just the path for vanilla items
        }
        return id.toString(); // Full ID for modded items
    }

    /**
     * Get eating time for an item
     */
    public static int getEatingTime(ItemStack stack) {
        if (!stack.isFood()) {
            return 32; // Default vanilla time
        }

        FoodProperties props = getFoodProperties(stack.getItem());
        if (props != null) {
            return (int)(props.eatingTime * get().general.globalEatingSpeedMultiplier);
        }

        // Fallback to vanilla detection
        return getVanillaEatingTime(stack);
    }

    /**
     * Fallback vanilla eating time detection
     */
    private static int getVanillaEatingTime(ItemStack stack) {
        try {
            // Check if we're already processing to prevent recursion
            if (isProcessingFoodComponent.get()) {
                var foodComponent = stack.getItem().getFoodComponent();
                if (foodComponent != null) {
                    return foodComponent.isSnack() ? 16 : 32;
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return 32;
    }

    // ==================== FOOD PROPERTIES CLASS ====================

    /**
     * Wrapper class for food properties
     */
    public static class FoodProperties {
        public final int eatingTime;
        public final int hunger;
        public final float saturation;
        public final boolean isMeat;
        public final boolean isSnack;
        public final boolean alwaysEdible;
        public final java.util.Map<String, ModConfig.EffectEntry> effects;

        public FoodProperties(ModConfig.VanillaFoodEntry entry) {
            this.eatingTime = entry.eatingTime;
            this.hunger = entry.hunger;
            this.saturation = entry.saturation;
            this.isMeat = entry.isMeat;
            this.isSnack = entry.isSnack;
            this.alwaysEdible = entry.alwaysEdible;
            this.effects = entry.effects;
        }

        public FoodProperties(ModConfig.ModdedFoodEntry entry) {
            this.eatingTime = entry.eatingTime;
            this.hunger = entry.hunger;
            this.saturation = entry.saturation;
            this.isMeat = entry.isMeat;
            this.isSnack = entry.isSnack;
            this.alwaysEdible = entry.alwaysEdible;
            this.effects = entry.effects;
        }

        public FoodProperties(int eatingTime, int hunger, float saturation,
                              boolean isMeat, boolean isSnack, boolean alwaysEdible) {
            this.eatingTime = eatingTime;
            this.hunger = hunger;
            this.saturation = saturation;
            this.isMeat = isMeat;
            this.isSnack = isSnack;
            this.alwaysEdible = alwaysEdible;
            this.effects = new java.util.HashMap<>();
        }
    }
}