package com.example.genji.network.packet;

import com.example.genji.events.DoubleJumpAbility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client-to-server packet for double jump activation
 */
public class C2SDoubleJump {
    
    public C2SDoubleJump() {
        // Empty constructor for packet creation
    }
    
    public C2SDoubleJump(FriendlyByteBuf buf) {
        // Empty constructor for packet decoding
    }
    
    public void toBytes(FriendlyByteBuf buf) {
        // No data to encode
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sp = ctx.getSender();
            if (sp != null) {
                System.out.println("C2SDoubleJump: Received double jump request from " + sp.getName());
                System.out.println("C2SDoubleJump: Player onGround=" + sp.onGround() + ", inWater=" + sp.isInWater() + ", passenger=" + sp.isPassenger());
                DoubleJumpAbility.executeDoubleJump(sp);
            } else {
                System.out.println("C2SDoubleJump: No sender found!");
            }
        });
        return true;
    }
}
