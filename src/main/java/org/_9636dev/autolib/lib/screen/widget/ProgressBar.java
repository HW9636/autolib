package org._9636dev.autolib.lib.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ProgressBar extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    public enum ProgressDirection {
        LEFT_TO_RIGHT((x, mapped, w) -> x, (y, mapped, h) -> y, (w, mapped) -> mapped, (h, mapped) -> h, false),
        RIGHT_TO_LEFT((x, mapped, w) -> x + w - mapped, (y, mapped, h) -> y, (w, mapped) -> mapped, (h, mapped) -> h, false),
        TOP_TO_BOTTOM((x, mapped, w) -> x, (y, mapped, h) -> y, (w, mapped) -> w, (h, mapped) -> mapped, true),
        BOTTOM_TO_TOP((x, mapped, w) -> x, (y, mapped, h) -> y + h - mapped, (w, mapped) -> w, (h, mapped) -> mapped, true);

        private final TriFunction<Integer, Integer, Integer, Integer> xMapFunction, yMapFunction;
        private final BiFunction<Integer, Integer, Integer> widthMapFunction, heightMapFunction;

        private final boolean isVertical;
        ProgressDirection(TriFunction<Integer, Integer, Integer, Integer> pXMapFunction, TriFunction<Integer, Integer, Integer, Integer> pYMapFunction,
                          BiFunction<Integer, Integer, Integer> pWidthMapFunction, BiFunction<Integer, Integer, Integer> pHeightMapFunction, boolean isVertical) {
            this.xMapFunction = pXMapFunction;
            this.yMapFunction = pYMapFunction;
            this.widthMapFunction = pWidthMapFunction;
            this.heightMapFunction = pHeightMapFunction;
            this.isVertical = isVertical;
        }
    }

    private final int xPos, yPos, minWidth, minHeight, maxWidth, maxHeight, minXProgress, maxXProgress, minYProgress, maxYProgress;
    private final ProgressDirection direction;
    private final Supplier<Integer> progress;
    private final TexturedButton.Texture texture;

    public ProgressBar(int pXPos, int pYPos, int pMinWidth, int pMinHeight, int pMaxWidth, int pMaxHeight,
                          int pMinXProgress, int pMaxXProgress, int pMinYProgress, int pMaxYProgress,
                          ProgressDirection pDirection, TexturedButton.Texture texture, Supplier<Integer> progress) {
        this.xPos = pXPos;
        this.yPos = pYPos;
        this.minWidth = pMinWidth;
        this.minHeight = pMinHeight;
        this.maxWidth = pMaxWidth;
        this.maxHeight = pMaxHeight;
        this.direction = pDirection;
        this.minXProgress = pMinXProgress;
        this.maxXProgress = pMaxXProgress;
        this.minYProgress = pMinYProgress;
        this.maxYProgress = pMaxYProgress;
        this.texture = texture;
        this.progress = progress;
    }

    private int mapNum(int min, int max, int minMapped, int maxMapped, int num) {
        if (minMapped == maxMapped) return minMapped;
        if (num < min || num > max) {
            LogUtils.getLogger().warn("{} is too big or too small to map for {}, {}", num, min, max);
            return minMapped;
        }
        return minMapped + (int)((num - min) / (double)(max - min) * (maxMapped - minMapped));
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.texture.location() != null) {
            RenderSystem.setShaderTexture(0, this.texture.location());
        }

        int progress = this.progress.get();
        int xMapped = this.direction.isVertical ? this.xPos : mapNum(this.minXProgress, this.maxXProgress, this.minWidth, this.maxWidth, progress);
        int yMapped = !this.direction.isVertical ? this.yPos : mapNum(this.minYProgress, this.maxYProgress, this.minHeight, this.maxHeight, progress);

        this.blit(pPoseStack,
                direction.xMapFunction.apply(this.xPos, xMapped, this.maxWidth),
                direction.yMapFunction.apply(this.yPos, yMapped, this.maxHeight),
                this.texture.x(),
                this.texture.y(),
                direction.widthMapFunction.apply(this.maxWidth, xMapped),
                direction.heightMapFunction.apply(this.maxHeight, yMapped)
        );
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {}
}
