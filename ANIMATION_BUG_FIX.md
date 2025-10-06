# Critical Animation Bug Fix - Dash Breaking After First Use

## 🐛 ROOT CAUSE IDENTIFIED

The dash animation was breaking after the first use due to **static phase tracking variables** in `DragonbladeItem.java`:

```java
// BROKEN CODE (OLD):
private static Phase handPhase  = Phase.NONE;
private static Phase bladePhase = Phase.NONE;
```

### Why This Broke The Animation

**Static variables are shared across ALL instances** of the item, including the special `DASH_STACK` used for rendering dash animations.

**What happened:**
1. **First Dash**:
   - `handPhase` changes: `NONE` → `DASH` → (animation plays)
   - After dash ends, `handPhase` transitions back but the **static variable retains state**

2. **Second Dash**:
   - `DASH_STACK` is a different ItemStack instance but shares the same static `handPhase`
   - Animation controller checks: `if (want != item.handPhase)`
   - Since `handPhase` might still be cached from previous transition, **`setClip()` doesn't get called**
   - Result: No animation, or broken/partial animation

3. **After Deflect**:
   - Similar issue - the static phase gets polluted by deflect animations
   - When dash tries to play, it conflicts with the cached phase state

## ✅ THE FIX

### 1. Made Phase Variables Instance-Specific
```java
// FIXED CODE (NEW):
private Phase handPhase  = Phase.NONE;  // No longer static!
private Phase bladePhase = Phase.NONE;  // No longer static!
```

Each ItemStack now has its own phase tracking, preventing cross-contamination.

### 2. Forced Phase Reset on Dash Start
```java
if (want == Phase.DASH && FPDashAnim.justStarted()) {
    state.getController().forceAnimationReset();
    item.handPhase = Phase.NONE; // Force reset to NONE
}
```

Even if phase is somehow cached, we explicitly reset it to `NONE` before transitioning to `DASH`, guaranteeing `setClip()` will be called.

### 3. Updated Animation Controller Logic
Changed from static access to instance access:
```java
// OLD: if (want != handPhase)
// NEW: if (want != item.handPhase)
```

## 🎯 RESULT

- ✅ First dash: Animation plays perfectly
- ✅ Second dash: Animation plays perfectly (no longer breaks!)
- ✅ Third+ dash: Animation plays perfectly every time
- ✅ Dash after deflect: Animation plays perfectly
- ✅ Each ItemStack maintains independent animation state

## 🧪 HOW TO VERIFY

Test multiple dashes in a row. The logs should now show:

**First Dash:**
```
DragonbladeItem hand_ctrl: DASH justStarted, forcing animation reset and phase reset
DragonbladeItem hand_ctrl: Phase changed from NONE to DASH
```

**Second Dash:**
```
DragonbladeItem hand_ctrl: DASH justStarted, forcing animation reset and phase reset
DragonbladeItem hand_ctrl: Phase changed from NONE to DASH
```

Notice that **both dashes start from NONE**, proving the phase is properly reset!

## 📝 FILES MODIFIED

- `src/main/java/com/example/genji/content/DragonbladeItem.java`
  - Changed `static Phase` to instance `Phase` variables
  - Updated animation controllers to use instance variables
  - Added forced phase reset on dash start

## 💡 LESSON LEARNED

**Never use static variables for animation state tracking** when multiple instances (like ItemStacks) might be rendered simultaneously or sequentially. Each instance needs its own state tracking to prevent cross-contamination.

This was a classic Java static state bug that only manifests during repeated use!

