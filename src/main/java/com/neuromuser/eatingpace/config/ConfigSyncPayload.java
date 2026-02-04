package com.neuromuser.eatingpace.config;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncPayload(String json) implements CustomPayload {
    public static final CustomPayload.Id<ConfigSyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of("eating-pace", "config"));

    public static final PacketCodec<RegistryByteBuf, ConfigSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ConfigSyncPayload::json,
            ConfigSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}