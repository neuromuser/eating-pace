package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class EatingPaceClient implements ClientModInitializer {
        @Override
        public void onInitializeClient() {
                ConfigManager.load(FabricLoader.getInstance().getConfigDir().resolve("eating-pace.json"));
                ConfigNetworkingClient.init();
                EatingPace.LOGGER.info("Client initialization for {}!", EatingPace.MOD_ID);
        }
}