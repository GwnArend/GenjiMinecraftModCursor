package com.example.genji.network.packet;

import com.example.genji.capability.GenjiDataProvider;
import com.example.genji.events.DragonbladeCombat;
import com.example.genji.events.ShurikenCombat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Mouse 1 (primary) held/released. Route to blade during CAST/ACTIVE; shuriken otherwise. HARD-LOCK during SHEATHE. */
public class C2SSetPrimaryHeld {
    private final boolean down;
    public C2SSetPrimaryHeld(boolean down) { this.down = down; }
    public C2SSetPrimaryHeld(FriendlyByteBuf buf) { this.down = buf.readBoolean(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeBoolean(down); }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sp = ctx.get().getSender();
            if (sp == null) return;

            var data = GenjiDataProvider.get(sp);

            // === HARD LOCK while SHEATHING: no blade swings, no shurikens ===
            if (data.isSheathing()) {
                DragonbladeCombat.setPrimaryHeld(sp, false);
                ShurikenCombat.setPrimaryHeld(sp, false);
                return;
            }

            boolean bladePhase = data.isCastingBlade() || data.isBladeActive();
            if (bladePhase) {
                // Drive blade; stop shurikens
                DragonbladeCombat.setPrimaryHeld(sp, down);
                ShurikenCombat.setPrimaryHeld(sp, false);
            } else {
                // Drive shurikens; stop blade input
                ShurikenCombat.setPrimaryHeld(sp, down);
                DragonbladeCombat.setPrimaryHeld(sp, false);
            }
        });
        return true;
    }
}
