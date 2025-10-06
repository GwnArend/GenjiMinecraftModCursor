# Dash Mechanic Improvements

## Changes Made

### 1. **Ultra-Smooth Client-Side Interpolation**
- Created `DashInterpolation.java` - Handles smooth camera movement with ease-out cubic interpolation
- **Uses real-time (milliseconds) instead of ticks** for ultra-smooth frame-independent movement
- Renders at full framerate (60+ FPS) for buttery-smooth movement
- Client smoothly interpolates between start and end positions with partial tick rendering

### 2. **Camera Locking**
- Created `DashCameraControl.java` - Event handler for camera and input control during dash
- **Camera rotation is completely locked** during dash - no mouse movement allowed
- **All player inputs are disabled** during dash (WASD, jump, crouch, etc.)
- The camera smoothly glides to the end position without any jittering

### 3. **Dynamic Duration Scaling**
- **Dash duration now scales with actual distance traveled**
- If dash is interrupted by a wall, the lockout time is reduced proportionally
- Example: 50% distance = 50% duration
- Minimum duration: 2 ticks to prevent instant dashes

### 4. **Network Communication**
- Created `S2CStartDash.java` packet - Sends dash start/end positions from server to client
- Server tells client exactly where the dash starts and ends with calculated duration
- Client uses this info for smooth interpolation while server handles collision/damage

### 5. **Anti-Stutter System**
- Client overrides server position updates during dash to prevent stuttering
- Updates position on both `ClientTickEvent` and `RenderTickEvent` for maximum smoothness
- Sets both current and old positions to prevent Minecraft's built-in interpolation from interfering

### 6. **Updated Server Logic**
- Modified `DashAbility.java` to calculate actual dash distance and scale duration
- Sends scaled duration to client for synchronized lockout
- Server still handles all collision detection and damage dealing (authoritative)

### 7. **Registered New Packet**
- Updated `ModNetwork.java` to register the `S2CStartDash` packet

## How It Works

1. **Player presses Dash key**
2. **Server receives dash request** and calculates start/end positions with collision detection
3. **Server calculates actual distance** and scales duration (e.g., wall at 7.5 blocks = 4 ticks instead of 8)
4. **Server sends S2CStartDash packet** to client with positions and scaled duration
5. **Client locks camera and input** for the scaled duration
6. **Client smoothly interpolates** at full framerate using real-time milliseconds
7. **Client overrides server teleports** to maintain smooth movement
8. **At end of dash**, camera lock releases and player regains control instantly

## Key Features

✅ **Ultra-smooth movement** - Real-time interpolation at full framerate (60+ FPS)
✅ **No stuttering** - Client overrides server position updates during dash
✅ **Dynamic duration** - Shorter dashes = shorter lockout time
✅ **Locked camera** - Cannot move or look around during dash
✅ **Locked input** - Cannot use WASD or other controls during dash
✅ **Smooth easing** - Uses ease-out cubic for professional feel
✅ **Server authoritative** - All collision/damage handled server-side for anti-cheat

## Technical Details

- **Max Duration**: 8 ticks (0.4 seconds) for full 15-block dash
- **Min Duration**: 2 ticks (0.1 seconds) for very short dashes
- **Range**: 15 blocks maximum
- **Interpolation**: Ease-out cubic (`1 - (1-t)³`) using real-time milliseconds
- **Update Rate**: Every render frame (60+ FPS) for ultra-smooth movement
- **Events Used**:
  - `MovementInputUpdateEvent` - Locks player input
  - `ViewportEvent.ComputeCameraAngles` - Locks camera rotation
  - `ClientTickEvent` - Overrides server position updates
  - `RenderTickEvent` - Updates position at full framerate with partial tick

## Example Scenarios

- **Full 15-block dash**: 8 ticks (0.4s) duration
- **Wall at 7.5 blocks**: 4 ticks (0.2s) duration
- **Wall at 3.75 blocks**: 2 ticks (0.1s) duration - minimum
- **Wall at 1 block**: 2 ticks (0.1s) duration - minimum enforced

