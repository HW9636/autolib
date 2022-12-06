package org._9636dev.autolib.lib.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org._9636dev.autolib.lib.container.AutoContainer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AutoScreen<CONTAINER extends AutoContainer> extends AbstractContainerScreen<CONTAINER> {

    public AutoScreen(CONTAINER pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    protected static boolean isIn(int x, int y, int minX, int minY, int maxX, int maxY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    protected static int mapNum(int pMinMapped, int pMaxMapped, int pStart, int pMax, int pCurrent) {
        if (pMinMapped == pMaxMapped) return pMinMapped;
        if (pCurrent < pStart || pCurrent > pMax) {
            LogUtils.getLogger().warn("MapNum received unexpected value: {} (Range: {} - {})", pCurrent, pStart, pMax);
            return -1; // Error
        }
        return pMinMapped + (int)((pCurrent - pStart) / (double)(pMax - pStart) * (pMaxMapped - pMinMapped));
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pPoseStack);
    }
}
