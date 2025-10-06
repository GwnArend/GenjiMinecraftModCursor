package com.example.genji.network.packet;

import com.example.genji.capability.GenjiDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SBladeSwingHold {
    public C2SBladeSwingHold() {}
    public C2SBladeSwingHold(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sp = ctx.get().getSender();
            if (sp == null) return;

            // No "hold" timer exists in GenjiData; keep as a placeholder for future input.
            // var data = GenjiDataProvider.get(sp);
            // if (data.isBladeActive()) { /* optionally: DragonbladeCombat.tryStartSwing(sp); */ }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }
}
