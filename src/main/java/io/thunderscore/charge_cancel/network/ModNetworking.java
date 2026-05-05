package io.thunderscore.charge_cancel.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("charge_cancel", "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static volatile boolean ignoreNextClientAttack = false;

    public static void register() {
        INSTANCE.registerMessage(0, CancelChargeC2SPacket.class,
            CancelChargeC2SPacket::encode,
            CancelChargeC2SPacket::decode,
            CancelChargeC2SPacket::handle
        );
    }

    public static boolean shouldIgnoreNextClientAttack() {
        if (ignoreNextClientAttack) {
            ignoreNextClientAttack = false;
            return true;
        }
        return false;
    }

    public static void setIgnoreNextClientAttack() {
        ignoreNextClientAttack = true;
    }

    public static class CancelChargeC2SPacket {
        public CancelChargeC2SPacket() {}

        public static void encode(CancelChargeC2SPacket pkt, FriendlyByteBuf buf) {}

        public static CancelChargeC2SPacket decode(FriendlyByteBuf buf) {
            return new CancelChargeC2SPacket();
        }

        public static void handle(CancelChargeC2SPacket pkt, Supplier<NetworkEvent.Context> ctx) {
            if (!ctx.get().getDirection().getReceptionSide().isServer()) return;
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    player.stopUsingItem();
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
