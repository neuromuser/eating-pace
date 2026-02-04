package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.ConfigManager;
import com.neuromuser.eatingpace.config.ConfigNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EatingPace implements ModInitializer {
        public static final String MOD_ID = "eating-pace";
        public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

        @Override
        public void onInitialize() {
                ConfigManager.load(FabricLoader.getInstance().getConfigDir().resolve("eating-pace.json"));


                ConfigNetworking.init();

                ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                        if (server.isDedicated()) {
                                ConfigNetworking.sendToClient(handler.player);
                                LOGGER.info("Synced config to joining player: {}", handler.player.getName().getString());
                        } else {
                                ConfigManager.setIntegratedServer(true);
                        }
                });

                LOGGER.info("Eating Pace initialized!");
        }
}