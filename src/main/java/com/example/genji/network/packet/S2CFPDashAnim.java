package com.example.genji.network.packet;

import com.example.genji.client.anim.FPDashAnim;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Triggers the 0.25s wakizashi "dash" FP animation on the client. */
public class S2CFPDashAnim {
    public S2CFPDashAnim() {}
    public S2CFPDashAnim(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        var c = ctx.get();
        c.enqueueWork(() -> {
            if (Minecraft.getInstance().player == null) return;
            FPDashAnim.start();
        });
        c.setPacketHandled(true);
        return true;
    }
}
