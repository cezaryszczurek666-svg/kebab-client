package pl.kebab.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;

public class KebabClient implements ClientModInitializer {
    public static final String MOD_ID = "kebabclient";
    public static final String NAME = "Kebab Client";

    @Override
    public void onInitializeClient() {
        // HUD (zbroja, FPS, ping, XYZ)
        HudElementRegistry.addLast(Identifier.of(MOD_ID, "hud"), new KebabHud());

        // Podmiana menu glownego na nasze
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen instanceof TitleScreen) {
                client.setScreen(new KebabTitleScreen());
            }
        });
    }
}
