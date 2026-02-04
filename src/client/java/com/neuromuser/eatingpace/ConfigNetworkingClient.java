package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.ConfigManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

public class ConfigNetworkingClient {
    private static final Identifier SYNC_ID = new Identifier("eating-pace", "config");

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_ID, (client, handler, buf, responseSender) -> {
            String json = buf.readString();
            client.execute(() -> {
                ConfigManager.receiveServerConfig(json);
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