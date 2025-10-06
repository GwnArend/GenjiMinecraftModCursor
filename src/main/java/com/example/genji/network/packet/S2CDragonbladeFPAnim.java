package com.example.genji.network.packet;

import com.example.genji.client.anim.DragonbladeFPAnim;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Tells the client which swing animation to play (LEFT/RIGHT), exactly when server starts it. */
public class S2CDragonbladeFPAnim {
    public enum Dir { LEFT, RIGHT }
    private final Dir dir;

    public S2CDragonbladeFPAnim(Dir dir) { this.dir = dir; }
    public S2CDragonbladeFPAnim(FriendlyByteBuf buf) { this.dir = buf.readEnum(Dir.class); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeEnum(dir); }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        var c = ctx.get();
        c.enqueueWork(() -> {
            // Must run on the client thread
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (dir == Dir.LEFT) DragonbladeFPAnim.startLeft();
            else DragonbladeFPAnim.startRight();
        });
        c.setPacketHandled(true);
        return true;
    }
}
