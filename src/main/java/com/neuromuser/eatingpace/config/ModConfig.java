package com.neuromuser.eatingpace.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "eatingpace")
public class ModConfig implements ConfigData {

    // General settings
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.PrefixText
    public float saturationCap = 30.0f;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    public boolean enableCustomFoodValues = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    public float eatingSpeedMultiplier = 1.0f;

    // Interruption settings
    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.PrefixText
    public boolean enableEatingInterruption = true;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean fireInterrupts = false;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean poisonInterrupts = false;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean fallInterrupts = true;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean drowningInterrupts = false;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean suffocationInterrupts = true;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean starvationInterrupts = false;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean meleeInterrupts = true;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean projectileInterrupts = true;

    @ConfigEntry.Category("interruption")
    @ConfigEntry.Gui.Tooltip
    public boolean explosionInterrupts = true;
}