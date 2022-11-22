package org._9636dev.autolib.blockentity;

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
    public AutoBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

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
    protected abstract void saveAdditional(@NotNull CompoundTag tag);

    @Override
    public abstract void load(@NotNull CompoundTag tag);

    public static int setSide(Direction dir, int value, int sidesConfig) {
        return switch (dir) {
            case UP -> (sidesConfig & 0x3FF) | (value << 10); // 2 ^ 10
            case DOWN -> (sidesConfig & 0xCFF) | (value << 8); // 2 ^ 8
            case NORTH -> (sidesConfig & 0xF3F) | (value << 6); // 2 ^ 6
            case EAST -> (sidesConfig & 0xFCF) | (value << 4); // 2 ^ 4
            case SOUTH -> (sidesConfig & 0xFF3) | (value << 2);
            case WEST -> (sidesConfig & 0xFFC) | value;
        };
    }

    public abstract void dropItems(double pX, double pY, double pZ);

    public final void update() {
        requestModelDataUpdate();
        setChanged();

        if (this.level != null)
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
    }

    protected abstract ContainerData getData();
}
