package org._9636dev.autolib.lib.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class AutoContainer extends AbstractContainerMenu {

    public static final int INVENTORY_SLOTS_START = 0;
    public static final int INVENTORY_SLOTS_END = 26;

    public static final int HOTBAR_SLOTS_START = 27;
    public static final int HOTBAR_SLOTS_END = 35;

    public final ContainerLevelAccess containerAccess;
    public final ContainerData data;
    public AutoContainer(@NotNull MenuType<?> pMenuType, int pContainerId, Inventory pPlayerInv, ContainerData containerData) {
        this(pMenuType, pContainerId, pPlayerInv, BlockPos.ZERO, containerData);
    }

    public AutoContainer(@NotNull MenuType<?> pMenuType, int pContainerId, Inventory pPlayerInv, BlockPos pBlocKPos,
                         ContainerData pContainerData) {
        super(pMenuType, pContainerId);

        this.containerAccess = ContainerLevelAccess.create(pPlayerInv.player.level, pBlocKPos);
        this.data = pContainerData;

        addDataSlots(data);
    }

    protected void addInventorySlots(Inventory pPlayerInv) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public abstract boolean stillValid(@NotNull Player pPlayer);

    private void moveItemToContainer(ItemStack item) {

    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack returnStack = ItemStack.EMPTY;

        final Slot slot = getSlot(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            returnStack = item.copy();
            if (index <= 35) {
                // Check for slots in Container first
                moveItemToContainer(item);

                // Inventory slots
                if (index <= INVENTORY_SLOTS_END) {
                    if (!moveItemStackTo(item, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END + 1, true))
                        return ItemStack.EMPTY;
                }
                else {
                    if (!moveItemStackTo(item, INVENTORY_SLOTS_START, INVENTORY_SLOTS_END + 1, false))
                        return ItemStack.EMPTY;
                }
            }
            else { // From Container
                if (!moveItemStackTo(item, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END + 1, true))
                    return ItemStack.EMPTY;
            }

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return returnStack;
    }
}
