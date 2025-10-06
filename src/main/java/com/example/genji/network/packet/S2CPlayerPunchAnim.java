package com.example.genji.network.packet;

import com.example.genji.client.anim.PlayerAnimationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Tells the client to trigger third-person player air-punch animations for shurikens. */
public class S2CPlayerPunchAnim {
    public enum Type { SINGLE_PUNCH, BURST_PUNCH }
    private final Type type;

    public S2CPlayerPunchAnim(Type type) { this.type = type; }
    public S2CPlayerPunchAnim(FriendlyByteBuf buf) { this.type = buf.readEnum(Type.class); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeEnum(type); }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        var c = ctx.get();
        c.enqueueWork(() -> {
            // Use the animation helper for consistent behavior
            PlayerAnimationHelper.triggerShurikenThrow();
        });
        c.setPacketHandled(true);
        return true;
    }
}
