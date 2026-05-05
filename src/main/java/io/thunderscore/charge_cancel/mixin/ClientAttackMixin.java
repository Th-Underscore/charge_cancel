package io.thunderscore.charge_cancel.mixin;

import io.thunderscore.charge_cancel.network.ModNetworking;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class ClientAttackMixin {

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void onCancelAttackSwing(CallbackInfoReturnable<Boolean> cir) {
        if (ModNetworking.shouldIgnoreNextClientAttack()) {
            cir.setReturnValue(false);
        }
    }
}
