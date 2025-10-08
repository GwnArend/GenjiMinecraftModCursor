# Deflect Third Person Model Setup

## Overview
Added third-person deflect animation and model rendering with debug keybinds for positioning.

## Files Created

### 1. `DeflectTPSModel.java`
- Location: `src/main/java/com/example/genji/client/model/`
- GeoModel that references the deflect third-person model (`deflect.tps.model.geo.json`)
- Uses the dragonblade texture
- References the `deflect.tps.animation.deflect.json` animation file

### 2. `DeflectTPSAnimatable.java`
- Location: `src/main/java/com/example/genji/client/render/`
- Singleton GeoItem that holds the animation state for the deflect effect
- Contains animation controller that plays the `tpdeflect` animation when deflect is active
- Checks `FPDeflectAnim.isVisible()` to determine if deflect is active

### 3. `DeflectTPSLayer.java`
- Location: `src/main/java/com/example/genji/client/render/`
- RenderLayer that attaches to the player renderer
- Renders the deflect third-person model when `FPDeflectAnim.isVisible()` returns true
- Implements custom GeckoLib bone rendering with proper animation state updates
- Contains adjustable position/rotation/scale parameters (controllable with keybinds)

### 4. `DeflectModelAdjustmentHandler.java`
- Location: `src/main/java/com/example/genji/client/events/`
- Client-side event handler for keybind-based model adjustment
- Displays real-time feedback to the player via action bar messages
- Allows fine-tuning of the deflect model position/rotation/scale

## Keybinds Added

All keybinds are registered in `Keybinds.java` and use the numpad for easy access:

### Position Controls
- **Numpad 8**: Move model up (Y+)
- **Numpad 2**: Move model down (Y-)
- **Numpad 4**: Move model left (X-)
- **Numpad 6**: Move model right (X+)
- **Numpad 9**: Move model forward (Z-)
- **Numpad 3**: Move model backward (Z+)

### Rotation Controls
- **Numpad +**: Rotate X-axis up
- **Numpad -**: Rotate X-axis down
- **Numpad 7**: Rotate Y-axis left
- **Numpad 1**: Rotate Y-axis right
- **[**: Rotate Z-axis left
- **]**: Rotate Z-axis right

### Scale Controls
- **Numpad ***: Scale up
- **Numpad /**: Scale down

## Default Position
- Offset X: 0.0
- Offset Y: 1.2 (above player center)
- Offset Z: 0.3 (slightly forward)
- Rotation X/Y/Z: 0.0°
- Scale: 1.0

## How It Works

1. When the player activates deflect (via the Q keybind), `FPDeflectAnim.start()` is called
2. The `DeflectTPSLayer` checks `FPDeflectAnim.isVisible()` every frame during player rendering
3. If deflect is active, the layer renders the deflect model at the configured position/rotation
4. The `DeflectTPSAnimatable` animation controller plays the `tpdeflect` animation continuously while active
5. The animation is synchronized with the deflect state using GeckoLib's animation system

## Animation Details

- Animation name: `tpdeflect`
- Location: `assets/genji/animations/deflect.tps.animation.deflect.json`
- Duration: 0.9167 seconds (22 ticks at 24fps)
- Loop: Yes
- Bones animated: Arm1, Arm2, Arm3 (with scaling and rotation keyframes)

## Integration

The deflect layer is automatically added to all player renderers (default and slim skins) via `ClientModEvents.onAddLayers()`.

## Testing

To test the deflect third-person model:

1. Run the game
2. Get the dragonblade item
3. Press **Q** to activate deflect
4. Switch to third-person view (F5)
5. Use numpad keys to adjust the model position/rotation if needed
6. The wakizashi arms should appear and animate in front of the player

## Troubleshooting

- **Model not appearing**: Check that `FPDeflectAnim.isVisible()` returns true during deflect
- **Animation not playing**: Verify the animation file path matches in `DeflectTPSModel.getAnimationResource()`
- **Model in wrong position**: Use the numpad keybinds to adjust position/rotation in real-time
- **Texture missing**: Ensure `dragonblade.png` exists in `assets/genji/textures/item/`

