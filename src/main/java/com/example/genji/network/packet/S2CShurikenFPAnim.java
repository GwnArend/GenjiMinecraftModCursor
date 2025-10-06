package com.example.genji.network.packet;

import com.example.genji.content.ShurikenItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CShurikenFPAnim {
    public enum Type { M1_SHOT, M2_FAN }
    private final Type type;

    public S2CShurikenFPAnim(Type type) { this.type = type; }

    // --- codec ---
    public S2CShurikenFPAnim(FriendlyByteBuf buf) {
        this.type = Type.values()[buf.readVarInt()];
    }
    public void toBytes(FriendlyByteBuf buf) { buf.writeVarInt(this.type.ordinal()); }

    // --- handle (client) ---
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        var c = ctx.get();
        c.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (this.type == Type.M1_SHOT) ShurikenItem.playThrowSingle();
            else ShurikenItem.playThrowFan();
        });
        c.setPacketHandled(true);
        return true;
    }
}
