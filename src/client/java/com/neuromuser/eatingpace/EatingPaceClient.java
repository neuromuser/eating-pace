package com.neuromuser.eatingpace;

import com.neuromuser.eatingpace.client.EatingProgressHudConfigurable;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class EatingPaceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register the eating progress HUD
		HudRenderCallback.EVENT.register(new EatingProgressHudConfigurable());
	}
}