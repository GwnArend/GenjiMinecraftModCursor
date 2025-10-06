package com.example.genji.client.bridge;

import com.example.genji.client.ClientGenjiData;

/**
 * Dunne adapter om bestaande (gesyncte) swing-velden te lezen en
 * te normaliseren voor GeckoLib. Vul de TODO's met jouw echte velden.
 */
public final class DragonbladeAnimBridge {

    private DragonbladeAnimBridge() {}

    /** Is er op dít moment een swing bezig (ongeacht richting)? */
    public static boolean isSwinging() {
        // TODO map naar jouw client-veld, bv: return ClientGenjiData.bladeSwingTicks > 0;
        return false;
    }

    /** True als de huidige swing Right -> Left is; false = Left -> Right. */
    public static boolean isRightToLeft() {
        // TODO map naar jouw client-veld, bv: return ClientGenjiData.bladeSwingRightToLeft;
        return true;
    }

    /** Huidige swing's totale duur in ticks (matcht server): RL=15, LR=14. */
    public static int currentSwingTotalTicks() {
        // Heb je per-richting timers? Prima. Zo niet, gebruik de vaste waardes:
        return isRightToLeft() ? 15 : 14;
    }

    /** Hoever de swing is (0..total), als je dat hebt. Anders 0 laten. */
    public static int currentSwingTick() {
        // Optioneel: map naar jouw actuele tick-voortgang, bv: return ClientGenjiData.bladeSwingTick;
        return 0;
    }
}
