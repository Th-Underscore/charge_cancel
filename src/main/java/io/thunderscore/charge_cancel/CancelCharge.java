package io.thunderscore.charge_cancel;

import io.thunderscore.charge_cancel.network.ModNetworking;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod("charge_cancel")
public class CancelCharge {
    public CancelCharge() {
        ModNetworking.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(Keybinds::registerKeyMappings));
    }
}
