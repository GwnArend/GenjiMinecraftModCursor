package com.example.genji.network.packet;

import com.example.genji.capability.GenjiDataProvider;
import com.example.genji.content.DragonbladeItem;
import com.example.genji.registry.ModItems;
import com.example.genji.registry.ModSounds;
import com.example.genji.util.AdvancementHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SActivateNanoBoost {

    public C2SActivateNanoBoost() {}
    public C2SActivateNanoBoost(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        var c = ctx.get();
        c.enqueueWork(() -> {
            ServerPlayer sp = c.getSender();
            if (sp == null) return;

            sp.getCapability(GenjiDataProvider.CAPABILITY).ifPresent(data -> {
                if (data.isNanoActive()) return;  // already running
                if (data.getNano() < 100) return; // not enough charge

                data.beginNanoBoost();             // consume charge & start timer
                
                // Grant first nanoboost advancement
                AdvancementHelper.grantAdvancement(sp, ResourceLocation.fromNamespaceAndPath("genji", "first_nanoboost"));
                
                // Update dragonblade enchantments if blade is active
                if (data.isBladeActive()) {
                    int bladeSlot = data.getBladeSlot();
                    if (bladeSlot >= 0 && bladeSlot < 9) {
                        ItemStack bladeStack = sp.getInventory().getItem(bladeSlot);
                        if (bladeStack.is(ModItems.DRAGONBLADE.get())) {
                            DragonbladeItem.applyNanoboostEnchantments(bladeStack, true);
                            sp.inventoryMenu.broadcastChanges();
                        }
                    }
                    
                    // Grant combo achievement if both nano and blade are active
                    AdvancementHelper.grantAdvancement(sp, ResourceLocation.fromNamespaceAndPath("genji", "nano_blade_combo"));
                }
                
                // Play nanoboost casting sound
                sp.level().playSound(null, sp, ModSounds.NANOBOOST_CAST.get(), SoundSource.PLAYERS, 2.0f, 1.0f);
                
                // No explicit sync call needed if you already sync in your player tick;
                // otherwise your existing S2C sync path will propagate nano meter=0 and runtime.
            });
        });
        c.setPacketHandled(true);
        return true;
    }
}
