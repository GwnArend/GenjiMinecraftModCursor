# Better Combat Integration

This mod integrates with [Better Combat](https://github.com/ZsoltMolnarrr/BetterCombat) to provide authentic katana animations and combat behavior for the dragonblade.

## How It Works

### Weapon Attributes
The dragonblade uses Better Combat's `katana` preset with custom modifications:

**File:** `src/main/resources/data/genji/weapon_attributes/dragonblade.json`
```json
{
  "parent": "bettercombat:katana",
  "attributes": {
    "attack_range": 4.0,
    "attacks": [
      {
        "damageMultiplier": 1.2,
        "angle": 120,
        "upswing": 0.6
      },
      {
        "damageMultiplier": 1.0,
        "angle": 100,
        "upswing": 0.5
      },
      {
        "damageMultiplier": 1.4,
        "angle": 140,
        "upswing": 0.7
      }
    ]
  }
}
```

### Custom Attributes Explained
- **`attack_range: 4.0`** - Extended reach for the dragonblade
- **3-attack combo** with varying damage multipliers and angles
- **`upswing` values** - Control the visual timing of each attack animation
- **`angle` values** - Define the attack arc for each combo move

### Animation System
When Better Combat is installed:
- **Automatic katana animations** are applied to the dragonblade
- **Combo system** provides fluid attack sequences
- **Third-person animations** are handled by Better Combat
- **First-person animations** remain custom (GeckoLib-based)

### Fallback Support
- **Without Better Combat:** Falls back to vanilla sword animations
- **Simply Swords compatibility:** Uses enhanced katana-style animations
- **Mod detection:** Automatically detects installed combat mods

## Installation
1. Install [Better Combat](https://www.curseforge.com/minecraft/mc-mods/better-combat-by-daedelus)
2. Install this mod
3. The dragonblade will automatically use katana animations and combat behavior

## Customization
You can modify the weapon attributes by editing:
`data/genji/weapon_attributes/dragonblade.json`

For more information, see the [Better Combat integration guide](https://github.com/ZsoltMolnarrr/BetterCombat?tab=readme-ov-file#-integrate-your-mod).







