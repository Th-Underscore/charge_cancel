package io.thunderscore.charge_cancel;

import io.thunderscore.charge_cancel.network.ModNetworking;
import net.minecraftforge.fml.common.Mod;

@Mod("charge_cancel")
public class CancelCharge {
    public CancelCharge() {
        ModNetworking.register();
    }
}
