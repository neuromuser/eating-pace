package com.neuromuser.eatingpace.config;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ConfigNetworking {
    private static final Identifier SYNC_ID = new Identifier("eating-pace", "config");

    public static void initServer() {
    }

    public static void initIntegrated() {
    }

    public static void sendToClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(ConfigManager.toJson());
        ServerPlayNetworking.send(player, SYNC_ID, buf);
    }

}