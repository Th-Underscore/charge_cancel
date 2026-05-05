package io.thunderscore.charge_cancel;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybinds {

    public static final String KEY_CATEGORY_CANCELCHARGE = "key.categories.charge_cancel";
    public static final String KEY_CANCEL_CHARGE = "key.charge_cancel.cancel_charge";

    public static KeyMapping CANCEL_CHARGE;

    public static void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        CANCEL_CHARGE = new KeyMapping(
            KEY_CANCEL_CHARGE,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            KEY_CATEGORY_CANCELCHARGE
        );
        event.register(CANCEL_CHARGE);
    }
}