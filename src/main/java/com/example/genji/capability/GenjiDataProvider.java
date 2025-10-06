package com.example.genji.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenjiDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<GenjiData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("genji", "genji_data");

    private final GenjiData backend = new GenjiData();
    private final LazyOptional<GenjiData> optional = LazyOptional.of(() -> backend);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override public CompoundTag serializeNBT() {
        try {
            CompoundTag nbt = backend.save();
            if (nbt == null) nbt = new CompoundTag();
            nbt.putInt("_genji_cap_ver", 1);
            return nbt;
        } catch (Throwable t) {
            return new CompoundTag();
        }
    }

    @Override public void deserializeNBT(CompoundTag nbt) {
        try {
            if (nbt != null) backend.load(nbt);
        } catch (Throwable t) {
            // never throw during join
        }
    }

    // Existing throwing accessor (unchanged)
    public static GenjiData get(Player p) {
        return p.getCapability(CAPABILITY)
                .orElseThrow(() -> new IllegalStateException("GenjiData missing"));
    }

    // New safe helpers
    public static LazyOptional<GenjiData> maybe(Player p) { return p.getCapability(CAPABILITY); }
    @Nullable public static GenjiData getOrNull(Player p) { return maybe(p).orElse(null); }
}
