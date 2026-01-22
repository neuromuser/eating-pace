package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EatingPace implements ModInitializer {
	public static final String MOD_ID = "eatingpace";
	public static ModConfig CONFIG;
	public static final Identifier SERVER_HANDSHAKE_PACKET = new Identifier(MOD_ID, "server_handshake");
	public static final Identifier CONFIG_SYNC_PACKET = new Identifier(MOD_ID, "config_sync");  // ADD THIS
	private static boolean isServerSideActive = false;

	@Override
	public void onInitialize() {
		ConfigHolder<ModConfig> holder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		CONFIG = holder.getConfig();

		ServerPlayNetworking.registerGlobalReceiver(SERVER_HANDSHAKE_PACKET,
				(server, player, handler, buf, responseSender) -> {
					responseSender.sendPacket(SERVER_HANDSHAKE_PACKET, PacketByteBufs.create());

					// Send config to client
					sendConfigToClient(player);
				}
		);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			if (ServerPlayNetworking.canSend(handler.player, SERVER_HANDSHAKE_PACKET)) {
				sendConfigToClient(handler.player);
			}
		});

		isServerSideActive = true;
	}

	private void sendConfigToClient(ServerPlayerEntity player) {
		var buf = PacketByteBufs.create();

		buf.writeFloat(CONFIG.saturationCap);
		buf.writeBoolean(CONFIG.enableCustomFoodValues);
		buf.writeFloat(CONFIG.eatingSpeedMultiplier);

		buf.writeBoolean(CONFIG.enableEatingInterruption);
		buf.writeBoolean(CONFIG.fireInterrupts);
		buf.writeBoolean(CONFIG.poisonInterrupts);
		buf.writeBoolean(CONFIG.fallInterrupts);
		buf.writeBoolean(CONFIG.drowningInterrupts);
		buf.writeBoolean(CONFIG.suffocationInterrupts);
		buf.writeBoolean(CONFIG.starvationInterrupts);
		buf.writeBoolean(CONFIG.meleeInterrupts);
		buf.writeBoolean(CONFIG.projectileInterrupts);
		buf.writeBoolean(CONFIG.explosionInterrupts);

		ServerPlayNetworking.send(player, CONFIG_SYNC_PACKET, buf);
	}

	public static boolean isServerSideActive() {
		return isServerSideActive;
	}

	public static void setServerSideActive(boolean active) {
		isServerSideActive = active;
	}
}