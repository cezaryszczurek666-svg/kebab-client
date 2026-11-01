package pl.kebab.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class KebabHud implements HudElement {
    private static final int BG = 0x90101018;      // polprzezroczyste tlo
    private static final int ACCENT = 0xFFFF9F1C;  // kebabowy pomaranczowy
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GRAY = 0xFFB8B8C8;

    private static final EquipmentSlot[] ARMOR = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    @Override
    public void render(DrawContext ctx, RenderTickCounter tick) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden || mc.getDebugHud().shouldShowDebugHud()) return;
        TextRenderer tr = mc.textRenderer;

        drawInfoPanel(ctx, mc, tr);
        drawArmorPanel(ctx, mc, tr);
    }

    /** Lewy gorny rog: FPS, ping, koordynaty */
    private void drawInfoPanel(DrawContext ctx, MinecraftClient mc, TextRenderer tr) {
        int x = 6, y = 6, w = 104, h = 52;
        panel(ctx, x, y, w, h);

        int fps = mc.getCurrentFps();
        int fpsColor = fps >= 120 ? 0xFF55FF55 : fps >= 60 ? 0xFFFFFF55 : 0xFFFF5555;
        ctx.drawTextWithShadow(tr, "FPS", x + 7, y + 6, GRAY);
        ctx.drawTextWithShadow(tr, String.valueOf(fps), x + 36, y + 6, fpsColor);

        ctx.drawTextWithShadow(tr, "PING", x + 7, y + 18, GRAY);
        ctx.drawTextWithShadow(tr, getPing(mc) + " ms", x + 36, y + 18, WHITE);

        String xyz = (int) mc.player.getX() + " " + (int) mc.player.getY() + " " + (int) mc.player.getZ();
        ctx.drawTextWithShadow(tr, "XYZ", x + 7, y + 30, GRAY);
        ctx.drawTextWithShadow(tr, xyz, x + 7, y + 40, WHITE);
    }

    /** Prawa strona ekranu: czesci zbroi + wytrzymalosc */
    private void drawArmorPanel(DrawContext ctx, MinecraftClient mc, TextRenderer tr) {
        int w = 64, rowH = 20;
        int h = ARMOR.length * rowH + 8;
        int x = ctx.getScaledWindowWidth() - w - 6;
        int y = ctx.getScaledWindowHeight() / 2 - h / 2;
        panel(ctx, x, y, w, h);

        int cy = y + 4;
        for (EquipmentSlot slot : ARMOR) {
            ItemStack stack = mc.player.getEquippedStack(slot);
            if (!stack.isEmpty()) {
                ctx.drawItem(stack, x + 5, cy + 1);
                if (stack.isDamageable()) {
                    int left = stack.getMaxDamage() - stack.getDamage();
                    float pct = (float) left / stack.getMaxDamage();
                    int col = pct > 0.5f ? 0xFF55FF55 : pct > 0.2f ? 0xFFFFFF55 : 0xFFFF5555;
                    ctx.drawTextWithShadow(tr, String.valueOf(left), x + 26, cy + 5, col);
                } else {
                    ctx.drawTextWithShadow(tr, "-", x + 26, cy + 5, GRAY);
                }
            } else {
                ctx.drawTextWithShadow(tr, "--", x + 26, cy + 5, 0xFF666677);
            }
            cy += rowH;
        }
    }

    private static void panel(DrawContext ctx, int x, int y, int w, int h) {
        ctx.fill(x, y, x + w, y + h, BG);
        ctx.fill(x, y, x + 2, y + h, ACCENT); // akcent z lewej
    }

    private static int getPing(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null) return 0;
        PlayerListEntry e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        return e == null ? 0 : e.getLatency();
    }
}
