package org._9636dev.autolib.lib.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class AutoBlockEntity extends BlockEntity {

    public final ContainerData data;
    protected int sidesConfig;

    public AutoBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);

        data = getContainerData();
        sidesConfig = getDefaultSides();
    }

    public abstract void changeSides(int oldSize, int newSides);

    protected abstract int getDefaultSides();

    protected abstract ContainerData getContainerData();

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
    }

    public static int getSide(int sidesConfig, Direction side) {
        return switch (side) {
            case UP -> (sidesConfig >> 20) & 15;
            case DOWN ->  (sidesConfig >> 16) & 15;
            case NORTH ->  (sidesConfig >> 12) & 15;
            case EAST -> (sidesConfig >> 8) & 15;
            case SOUTH -> (sidesConfig >> 4) & 15;
            case WEST -> sidesConfig & 15;
        };
    }

    public static int setSide(Direction dir, int value, int sidesConfig) {
        return switch (dir) {
            case UP -> (sidesConfig & 0x0FFFFF) | (value << 20); // 2 ^ 10
            case DOWN -> (sidesConfig & 0xF0FFFF) | (value << 16); // 2 ^ 8
            case NORTH -> (sidesConfig & 0xFF0FFF) | (value << 12); // 2 ^ 6
            case EAST -> (sidesConfig & 0xFFF0FF) | (value << 8); // 2 ^ 4
            case SOUTH -> (sidesConfig & 0xFFFF0F) | (value << 4);
            case WEST -> (sidesConfig & 0xFFFFF0) | value;
        };
    }

    public abstract void dropItems(double pX, double pY, double pZ);

    public final void update() {
        requestModelDataUpdate();
        setChanged();

        if (this.level != null)
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
    }
}
