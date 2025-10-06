package com.example.genji.network;

import com.example.genji.GenjiMod;
import com.example.genji.network.packet.C2SActivateBlade;
import com.example.genji.network.packet.C2SActivateDash;
import com.example.genji.network.packet.C2SActivateDeflect;
import com.example.genji.network.packet.C2SActivateNanoBoost; // <-- NEW
import com.example.genji.network.packet.C2SBladeSwingHold;
import com.example.genji.network.packet.C2SDoubleJump;
import com.example.genji.network.packet.C2SSetPrimaryHeld;
import com.example.genji.network.packet.C2SSetSecondaryHeld;
import com.example.genji.network.packet.S2CSyncGenjiData;
import com.example.genji.network.packet.S2CShurikenFPAnim;
import com.example.genji.network.packet.S2CPlayHitSound;
import com.example.genji.network.packet.S2CPlayerPunchAnim;
import com.example.genji.network.packet.S2CStartDash;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    public static final String PROTO = "1";
    public static SimpleChannel CHANNEL;
    private static int id = 0;

    public static void init() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(GenjiMod.MODID, "main"))
                .networkProtocolVersion(() -> PROTO)
                .clientAcceptedVersions(PROTO::equals)
                .serverAcceptedVersions(PROTO::equals)
                .simpleChannel();

        // ---------------- C2S ----------------
        CHANNEL.messageBuilder(C2SActivateDeflect.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SActivateDeflect::new).encoder(C2SActivateDeflect::toBytes)
                .consumerMainThread(C2SActivateDeflect::handle).add();

        CHANNEL.messageBuilder(C2SActivateDash.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SActivateDash::new).encoder(C2SActivateDash::toBytes)
                .consumerMainThread(C2SActivateDash::handle).add();

        CHANNEL.messageBuilder(C2SActivateBlade.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SActivateBlade::new).encoder(C2SActivateBlade::toBytes)
                .consumerMainThread(C2SActivateBlade::handle).add();

        // NEW: Nano-Boost activation
        CHANNEL.messageBuilder(C2SActivateNanoBoost.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SActivateNanoBoost::new).encoder(C2SActivateNanoBoost::toBytes)
                .consumerMainThread(C2SActivateNanoBoost::handle).add();

        // NEW: hold signal while Blade is active (M1/M2 pressed)
        CHANNEL.messageBuilder(C2SBladeSwingHold.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SBladeSwingHold::new).encoder(C2SBladeSwingHold::toBytes)
                .consumerMainThread(C2SBladeSwingHold::handle).add();

        // NEW: double jump activation
        CHANNEL.messageBuilder(C2SDoubleJump.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SDoubleJump::new).encoder(C2SDoubleJump::toBytes)
                .consumerMainThread(C2SDoubleJump::handle).add();

        CHANNEL.messageBuilder(C2SSetPrimaryHeld.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSetPrimaryHeld::new).encoder(C2SSetPrimaryHeld::toBytes)
                .consumerMainThread(C2SSetPrimaryHeld::handle).add();

        CHANNEL.messageBuilder(C2SSetSecondaryHeld.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSetSecondaryHeld::new).encoder(C2SSetSecondaryHeld::toBytes)
                .consumerMainThread(C2SSetSecondaryHeld::handle).add();

        // ---------------- S2C ----------------
        CHANNEL.messageBuilder(S2CSyncGenjiData.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CSyncGenjiData::new).encoder(S2CSyncGenjiData::toBytes)
                .consumerMainThread(S2CSyncGenjiData::handle).add();

        // NEW: first-person shuriken hand animation trigger (client-only)
        CHANNEL.messageBuilder(S2CShurikenFPAnim.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CShurikenFPAnim::new).encoder(S2CShurikenFPAnim::toBytes)
                .consumerMainThread(S2CShurikenFPAnim::handle).add();

        CHANNEL.messageBuilder(com.example.genji.network.packet.S2CDragonbladeFPAnim.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(com.example.genji.network.packet.S2CDragonbladeFPAnim::new)
                .encoder(com.example.genji.network.packet.S2CDragonbladeFPAnim::toBytes)
                .consumerMainThread(com.example.genji.network.packet.S2CDragonbladeFPAnim::handle).add();

        CHANNEL.messageBuilder(com.example.genji.network.packet.S2CFPDashAnim.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(com.example.genji.network.packet.S2CFPDashAnim::new)
                .encoder(com.example.genji.network.packet.S2CFPDashAnim::toBytes)
                .consumerMainThread(com.example.genji.network.packet.S2CFPDashAnim::handle)
                .add();

        CHANNEL.messageBuilder(com.example.genji.network.packet.S2CDeflectHit.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(com.example.genji.network.packet.S2CDeflectHit::new)
                .encoder(com.example.genji.network.packet.S2CDeflectHit::toBytes)
                .consumerMainThread(com.example.genji.network.packet.S2CDeflectHit::handle)
                .add();

        CHANNEL.messageBuilder(S2CPlayHitSound.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CPlayHitSound::new)
                .encoder(S2CPlayHitSound::toBytes)
                .consumerMainThread(S2CPlayHitSound::handle)
                .add();

        // NEW: third-person player air-punch animations for shurikens
        CHANNEL.messageBuilder(S2CPlayerPunchAnim.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CPlayerPunchAnim::new)
                .encoder(S2CPlayerPunchAnim::toBytes)
                .consumerMainThread(S2CPlayerPunchAnim::handle)
                .add();

        // Smooth dash interpolation
        CHANNEL.messageBuilder(S2CStartDash.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CStartDash::new)
                .encoder(S2CStartDash::toBytes)
                .consumerMainThread(S2CStartDash::handle)
                .add();

    }
}
