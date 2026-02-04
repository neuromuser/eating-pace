package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.ConfigManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

public class ConfigNetworkingClient {
    private static final Identifier SYNC_ID = Identifier.of("eating-pace", "config");

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(com.neuromuser.eatingpace.config.ConfigSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ConfigManager.receiveServerConfig(payload.json());
                EatingPace.LOGGER.info("Received server config");
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ConfigManager.clearServerConfig();
            EatingPace.LOGGER.info("Disconnected from server, cleared server config");
        });

        HudRenderCallback.EVENT.register(new EatingProgressHud());
    }
}