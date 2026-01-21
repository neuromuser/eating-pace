# Eating Pace

A Minecraft food rebalance mod that implements how I think the food system should work.

## Core Mechanics

### Eating Interruption
Taking damage (except poison, fire, drowning, starvation, withering) resets eating progress. This makes eating speed matter.

### Two Food Types

**Combat Foods:** Fast eating, low saturation, provide combat or utility buffs
- Example: Golden Carrot (0.7 sec) - gives short-term Speed II, Resistance

**Sustain Foods:** Slow eating, high saturation, minimal effects
- Example: Cooked Beef (5.5 sec) - 21.6 saturation, no effects

### Saturation System
Saturation cap increased from 20 to 30. Slow foods provide long-lasting sustain.

## Features

- Variable eating times (0.5 to 5.5 seconds)
- Combat interrupt mechanic (damage cancels eating)
- All vanilla foods rebalanced
- Situational food effects (water breathing for tropical fish, night vision for glow berries, etc.)
- Eating progress indicator

## Food Examples

**High Saturation (Sustain):**
- Rabbit Stew: 25.3 saturation, 4.5 sec
- Cooked Beef: 21.6 saturation, 5.5 sec
- Pumpkin Pie: 18.0 saturation, 4.25 sec

**Low Saturation (Combat):**
- Golden Carrot: 3.2 saturation, 0.7 sec - Speed II, Resistance
- Cookie: 0.4 saturation, 0.5 sec - Speed, Haste, Jump Boost (have chances)
- Honey Bottle: 1.2 saturation, 0.9 sec - Regeneration

**Utility:**
- Tropical Fish: Water Breathing, Dolphin's Grace
- Glow Berries: Night Vision
- Carrot: Speed, Jump Boost

## Recommended Mods

For the best experience, use alongside:

- **[AppleSkin](https://modrinth.com/mod/appleskin)** - Shows saturation and hunger values
- **[Centered Crosshair](https://modrinth.com/mod/centered-crosshair)** - Better eating indication visibility
- **[No Night Vision Flickering](https://modrinth.com/mod/no-night-vision-flickering)** - Removes annoying night vision flickering

## Installation

**Requirements:**
- Fabric Loader
- Fabric API

## Roadmap
- [x] Eating reset on hit
- [x] Vanilla food rebalance
- [x] Visual eating progress indicator
- [ ] Cloth config + Mod menu configuration
- [ ] Farmer's delight support
- [ ] Ports for 1.21, 1.19...


## License

MIT License

## Credits
Neuromuser, inspired by the game Phoenotopia Awakening
