package org._9636dev.autolib.lib.container;

import net.minecraft.world.inventory.ContainerData;
import org._9636dev.autolib.lib.blockentity.AutoBlockEntity;

@SuppressWarnings("unused")
public class AutoContainerData implements ContainerData {

    private final int count;
    private final AutoBlockEntity be;

    public AutoContainerData(AutoBlockEntity be) {
        this.count = be.data.getCount();
        this.be = be;
    }

    @Override
    public int get(int pIndex) {
        return be.data.get(pIndex);
    }

    @Override
    public void set(int pIndex, int pValue) {
        be.data.set(pIndex, pValue);
    }

    @Override
    public int getCount() {
        return count;
    }
}
