package io.thunderscore.charge_cancel;

import io.thunderscore.charge_cancel.network.ModNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.common.Tags;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = "charge_cancel", bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    private static final List<Field> USE_ITEM_FIELDS = new ArrayList<>();
    private static Field SWINGING_FIELD;
    private static boolean initialized = false;
    private static boolean suppressingUse = false;

    private static void init() {
        if (initialized) return;
        initialized = true;
        for (Field f : LivingEntity.class.getDeclaredFields()) {
            if (f.getType() == ItemStack.class && !java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                f.setAccessible(true);
                USE_ITEM_FIELDS.add(f);
            }
        }

        for (Class<?> c : new Class<?>[]{
                net.minecraft.client.player.LocalPlayer.class,
                net.minecraft.client.player.AbstractClientPlayer.class,
                net.minecraft.world.entity.player.Player.class,
                net.minecraft.world.entity.LivingEntity.class
        }) {
            for (Field f : c.getDeclaredFields()) {
                if (f.getType() == boolean.class && (f.getName().equals("swinging") || f.getName().contains("Swinging"))) {
                    f.setAccessible(true);
                    SWINGING_FIELD = f;
                    break;
                }
            }
            if (SWINGING_FIELD != null) break;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        init();

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        ItemStack usingItem = player.getUseItem();
        boolean isChargeableItem = usingItem.is(Tags.Items.TOOLS_BOWS)
                || usingItem.is(Tags.Items.TOOLS_CROSSBOWS)
                || usingItem.is(Tags.Items.TOOLS_TRIDENTS);

        boolean isCancelDown = isCancelKeyDown(mc);

        if (isCancelDown && player.isUsingItem() && isChargeableItem) {
            cancelCharge(player, mc);
            suppressingUse = true;
        }

        if (suppressingUse) {
            mc.options.keyUse.setDown(false);
            mc.options.keyAttack.setDown(false);
            if (!isCancelDown) {
                suppressingUse = false;
            }
        }
    }

    private static boolean isCancelKeyDown(Minecraft mc) {
        int cancelKeyValue = Keybinds.CANCEL_CHARGE.getKey().getValue();

        if (cancelKeyValue >= 0) {
            return Keybinds.CANCEL_CHARGE.isDown();
        }

        long window = mc.getWindow().getWindow();
        int value = mc.options.keyAttack.getKey().getValue();
        if (value < 0) {
            return GLFW.glfwGetKey(window, value) == GLFW.GLFW_PRESS;
        }
        return GLFW.glfwGetMouseButton(window, value) == GLFW.GLFW_PRESS;
    }

    private static void cancelCharge(LocalPlayer player, Minecraft mc) {
        clearUseItem(player);
        mc.options.keyUse.setDown(false);
        ModNetworking.INSTANCE.sendToServer(new ModNetworking.CancelChargeC2SPacket());
    }

    private static void clearUseItem(LocalPlayer player) {
        for (Field f : USE_ITEM_FIELDS) {
            try {
                f.set(player, ItemStack.EMPTY);
            } catch (Exception ignored) {}
        }
    }
}