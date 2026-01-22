package com.neuromuser.eatingpace;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.util.Identifier;

public class EatingPace implements ModInitializer {
	public static final String MOD_ID = "eatingpace";

	public static final Identifier SERVER_HANDSHAKE_PACKET = new Identifier(MOD_ID, "server_handshake");
	private static boolean isServerSideActive = false;

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(SERVER_HANDSHAKE_PACKET,
				(server, player, handler, buf, responseSender) -> {
					responseSender.sendPacket(SERVER_HANDSHAKE_PACKET, PacketByteBufs.create());
				});

		isServerSideActive = true;
	}

	public static boolean isServerSideActive() {
		return isServerSideActive;
	}

	public static void setServerSideActive(boolean active) {
		isServerSideActive = active;
	}
}