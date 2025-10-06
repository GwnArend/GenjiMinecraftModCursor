package com.example.genji.network.packet;

import com.example.genji.capability.GenjiDataProvider;
import com.example.genji.content.DragonbladeItem;
import com.example.genji.events.ShurikenCombat;
import com.example.genji.network.ModNetwork;
import com.example.genji.registry.ModItems;
import com.example.genji.registry.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Client -> Server: request to activate Dragonblade. Carries selected hotbar slot (0..8 or -1). */
public class C2SActivateBlade {
    private final int selectedSlot; // -1 if unknown

    public C2SActivateBlade(int selectedSlot) { this.selectedSlot = selectedSlot; }
    public C2SActivateBlade(FriendlyByteBuf buf) { this.selectedSlot = buf.readVarInt(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeVarInt(selectedSlot); }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        var c = ctx.get();
        c.enqueueWork(() -> {
            ServerPlayer sp = c.getSender();
            if (sp == null) return;

            sp.getCapability(GenjiDataProvider.CAPABILITY).ifPresent(data -> {
                if (!data.canBlade()) return;

                // Remember client-selected slot if valid; else current
                int sel = (selectedSlot >= 0 && selectedSlot < 9) ? selectedSlot : sp.getInventory().selected;
                data.setBladeSlot(sel);

                // Swap shuriken item to dragonblade item in the selected slot
                ItemStack dragonbladeStack = new ItemStack(ModItems.DRAGONBLADE.get());
                
                // Apply nanoboost enchantments if active
                if (data.isNanoActive()) {
                    DragonbladeItem.applyNanoboostEnchantments(dragonbladeStack, true);
                }
                
                sp.getInventory().setItem(sel, dragonbladeStack);
                sp.inventoryMenu.broadcastChanges();

                // Start cast (this also cancels deflect pose without cooldown)
                data.beginBladeCast();

                // Play the ult VO / start sound
                sp.level().playSound(null, sp, ModSounds.DRAGONBLADE_START.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

                // Stop shuriken channels immediately
                ShurikenCombat.setPrimaryHeld(sp, false);
                ShurikenCombat.setSecondaryHeld(sp, false);

                // Immediate sync so client shows unsheath overlay this tick
                ModNetwork.CHANNEL.sendTo(
                        new S2CSyncGenjiData(
                                data.getUlt(),
                                data.getNano(),
                                data.getBladeTicks(),
                                data.getDeflectTicks(),
                                data.getDashCooldown(),
                                data.getDeflectCooldown(),
                                data.getBladeCastTicks(),
                                data.getBladeSheatheTicks()
                        ),
                        sp.connection.connection,
                        NetworkDirection.PLAY_TO_CLIENT
                );
                data.markSynced();
            });
        });
        c.setPacketHandled(true);
        return true;
    }
}
