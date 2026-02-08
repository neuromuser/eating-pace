package com.neuromuser.eatingpace.config;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ConfigNetworking {
    private static final Identifier SYNC_ID = Identifier.of("eating-pace", "config");

    public static void init() {
        PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
    }

    public static void sendToClient(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new ConfigSyncPayload(ConfigManager.toJson()));
    }

}