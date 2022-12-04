package org._9636dev.autolib.lib.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TexturedButton extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {

    public record Texture(ResourceLocation location, int x, int y) {}

    public interface OnClick {
        void onClick(TexturedButton button, int mouseButton);
    }

    private final int posX, posY, width, height;
    private Texture texture;
    private final OnClick onClick;
    private boolean visible;

    public TexturedButton(int posX, int posY, int width, int height, Texture texture, OnClick onClick) {
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.onClick = onClick;

        this.visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (!visible) return;

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.texture.location != null) {
            RenderSystem.setShaderTexture(0, this.texture.location);
        }
        this.blit(pPoseStack, this.posX, this.posY, this.texture.x, this.texture.y, this.width, this.height);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!visible) return false;

        if (pMouseX >= this.posX && pMouseX <= this.posX + this.width && pMouseY > this.posY && pMouseY > this.posY + this.height) {
            this.onClick.onClick(this, pButton);
            return true;
        }

        return false;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {}

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
