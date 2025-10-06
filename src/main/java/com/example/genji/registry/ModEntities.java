package com.example.genji.registry;

import com.example.genji.GenjiMod;
import com.example.genji.content.ShurikenEntity; // <-- fixed package
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GenjiMod.MODID);

    public static final RegistryObject<EntityType<ShurikenEntity>> SHURIKEN =
            ENTITY_TYPES.register("shuriken",
                    () -> EntityType.Builder.<ShurikenEntity>of(ShurikenEntity::new, MobCategory.MISC)
                            .sized(0.35f, 0.35f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build(GenjiMod.MODID + ":shuriken"));

    public static void register(net.minecraftforge.eventbus.api.IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }
}
