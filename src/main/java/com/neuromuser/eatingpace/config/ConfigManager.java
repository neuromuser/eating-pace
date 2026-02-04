package com.neuromuser.eatingpace.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Config clientConfig = new Config();
    private static Config serverConfig = null;
    private static boolean isServerModPresent = false;
    private static boolean isIntegratedServer = false;

    public static Config get() {
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

    public static Config getClientConfig() {
        return clientConfig;
    }

    public static void load(Path path) {
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                clientConfig = GSON.fromJson(json, Config.class);
            } else {
                save(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
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
        serverConfig = GSON.fromJson(json, Config.class);
        isServerModPresent = true;
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
}