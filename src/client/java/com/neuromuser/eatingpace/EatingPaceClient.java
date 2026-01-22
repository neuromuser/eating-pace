package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.client.EatingProgressHudConfigurable;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class EatingPaceClient implements ClientModInitializer {
	private static boolean serverHasMod = false;

	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			serverHasMod = false;
			EatingPace.setServerSideActive(false);
			if (ClientPlayNetworking.canSend(EatingPace.SERVER_HANDSHAKE_PACKET)) {
				sender.sendPacket(EatingPace.SERVER_HANDSHAKE_PACKET, PacketByteBufs.create());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(EatingPace.SERVER_HANDSHAKE_PACKET, (client, handler, buf, responseSender) -> {
			// Server responded! It has the mod
			client.execute(() -> {
				serverHasMod = true;
				EatingPace.setServerSideActive(true);
			});
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			serverHasMod = false;
			EatingPace.setServerSideActive(false);
		});

		HudRenderCallback.EVENT.register(new EatingProgressHudConfigurable());
	}
		public static boolean isServerHasMod() {
			return serverHasMod;
		}
}