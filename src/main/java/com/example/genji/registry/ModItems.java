package com.example.genji.registry;

import com.example.genji.GenjiMod;
import com.example.genji.content.DragonbladeItem;
import com.example.genji.content.ShurikenItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Use ONE DeferredRegister for items
    public static final DeferredRegister<Item> REGISTER =
            DeferredRegister.create(ForgeRegistries.ITEMS, GenjiMod.MODID);

    // ✅ genji:shuriken (make sure this id matches everywhere you reference it)
    public static final RegistryObject<Item> SHURIKEN = REGISTER.register(
            "shuriken",
            () -> new ShurikenItem(new Item.Properties().stacksTo(1))
    );

    // ✅ genji:dragonblade
    public static final RegistryObject<Item> DRAGONBLADE = REGISTER.register(
            "dragonblade",
            () -> new DragonbladeItem(Tiers.IRON, 6, -2.2f, new Item.Properties().stacksTo(1))
    );
}
