package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.client.EatingProgressHudConfigurable;
import com.neuromuser.eatingpace.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class EatingPaceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			EatingPace.setServerSideActive(false);
			if (ClientPlayNetworking.canSend(EatingPace.SERVER_HANDSHAKE_PACKET)) {
				sender.sendPacket(EatingPace.SERVER_HANDSHAKE_PACKET, PacketByteBufs.create());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(EatingPace.SERVER_HANDSHAKE_PACKET, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				EatingPace.setServerSideActive(true);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(EatingPace.CONFIG_SYNC_PACKET, (client, handler, buf, responseSender) -> {
			// Read ALL config values
			float serverSaturationCap = buf.readFloat();
			boolean serverEnableCustomFoodValues = buf.readBoolean();
			float serverEatingSpeedMultiplier = buf.readFloat();

			boolean serverEnableEatingInterruption = buf.readBoolean();
			boolean serverFireInterrupts = buf.readBoolean();
			boolean serverPoisonInterrupts = buf.readBoolean();
			boolean serverFallInterrupts = buf.readBoolean();
			boolean serverDrowningInterrupts = buf.readBoolean();
			boolean serverSuffocationInterrupts = buf.readBoolean();
			boolean serverStarvationInterrupts = buf.readBoolean();
			boolean serverMeleeInterrupts = buf.readBoolean();
			boolean serverProjectileInterrupts = buf.readBoolean();
			boolean serverExplosionInterrupts = buf.readBoolean();

			client.execute(() -> {
				EatingPace.CONFIG.saturationCap = serverSaturationCap;
				EatingPace.CONFIG.enableCustomFoodValues = serverEnableCustomFoodValues;
				EatingPace.CONFIG.eatingSpeedMultiplier = serverEatingSpeedMultiplier;
				EatingPace.CONFIG.enableEatingInterruption = serverEnableEatingInterruption;
				EatingPace.CONFIG.fireInterrupts = serverFireInterrupts;
				EatingPace.CONFIG.poisonInterrupts = serverPoisonInterrupts;
				EatingPace.CONFIG.fallInterrupts = serverFallInterrupts;
				EatingPace.CONFIG.drowningInterrupts = serverDrowningInterrupts;
				EatingPace.CONFIG.suffocationInterrupts = serverSuffocationInterrupts;
				EatingPace.CONFIG.starvationInterrupts = serverStarvationInterrupts;
				EatingPace.CONFIG.meleeInterrupts = serverMeleeInterrupts;
				EatingPace.CONFIG.projectileInterrupts = serverProjectileInterrupts;
				EatingPace.CONFIG.explosionInterrupts = serverExplosionInterrupts;
			});
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			EatingPace.setServerSideActive(false);
			var holder = AutoConfig.getConfigHolder(ModConfig.class);
			EatingPace.CONFIG = holder.getConfig();
		});

		HudRenderCallback.EVENT.register(new EatingProgressHudConfigurable());
	}
}