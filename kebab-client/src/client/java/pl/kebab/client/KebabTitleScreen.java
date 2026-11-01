package pl.kebab.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class KebabTitleScreen extends Screen {
    private float time = 0f;

    public KebabTitleScreen() {
        super(Text.literal(KebabClient.NAME));
    }

    @Override
    protected void init() {
        int bw = 200, bh = 22;
        int cx = this.width / 2 - bw / 2;
        int y = this.height / 2 - 10;

        addDrawableChild(ButtonWidget.builder(Text.literal("Jeden gracz"),
            b -> client.setScreen(new SelectWorldScreen(this)))
            .dimensions(cx, y, bw, bh).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Multiplayer"),
            b -> client.setScreen(new MultiplayerScreen(this)))
            .dimensions(cx, y + 26, bw, bh).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Ustawienia"),
            b -> client.setScreen(new OptionsScreen(this, client.options)))
            .dimensions(cx, y + 52, bw / 2 - 2, bh).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Wyjdz"),
            b -> client.scheduleStop())
            .dimensions(cx + bw / 2 + 2, y + 52, bw / 2 - 2, bh).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        time += delta;

        // Animowany gradient tla (ciemny fiolet -> granat, falujacy)
        float wave = (float) (Math.sin(time * 0.02) * 0.5 + 0.5);
        int top = lerp(0xFF1A1033, 0xFF2B1055, wave);
        int bottom = lerp(0xFF0B0B1E, 0xFF3A1C71, 1f - wave);
        ctx.fillGradient(0, 0, width, height, top, bottom);

        // Plywajace "swiatla" w tle
        for (int i = 0; i < 6; i++) {
            float px = (float) ((Math.sin(time * 0.01 + i * 1.7) * 0.5 + 0.5) * width);
            float py = (float) ((Math.cos(time * 0.008 + i * 2.3) * 0.5 + 0.5) * height);
            int s = 24 + i * 6;
            ctx.fill((int) px - s, (int) py - s, (int) px + s, (int) py + s, 0x10FF9F1C);
        }

        // Panel za przyciskami
        int pw = 236, ph = 130;
        int px = width / 2 - pw / 2, py = height / 2 - 48;
        ctx.fill(px, py, px + pw, py + ph, 0xA0101018);
        ctx.fill(px, py, px + pw, py + 2, 0xFFFF9F1C);

        // Duzy tytul (skalowany x3)
        ctx.getMatrices().pushMatrix();
        ctx.getMatrices().translate(width / 2f, height / 2f - 90f);
        ctx.getMatrices().scale(3f, 3f);
        ctx.drawCenteredTextWithShadow(textRenderer, "KEBAB", 0, 0, 0xFFFF9F1C);
        ctx.getMatrices().popMatrix();

        ctx.drawCenteredTextWithShadow(textRenderer, "C L I E N T", width / 2, height / 2 - 60, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, "Kebab Client 1.0 | Minecraft 1.21.11", 6, height - 12, 0xFF8888AA);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private static int lerp(int a, int b, float t) {
        int ar = (a >> 16) & 255, ag = (a >> 8) & 255, ab = a & 255;
        int br = (b >> 16) & 255, bg = (b >> 8) & 255, bb = b & 255;
        int r = (int) (ar + (br - ar) * t), g = (int) (ag + (bg - ag) * t), bl = (int) (ab + (bb - ab) * t);
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }
}
