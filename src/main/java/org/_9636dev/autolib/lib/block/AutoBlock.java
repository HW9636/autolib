package org._9636dev.autolib.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org._9636dev.autolib.lib.blockentity.AutoBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"unused", "deprecation"})
public abstract class AutoBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected AutoBlock(Properties pProperties) {
        super(pProperties);
    }

    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide()) return clientUse(pState, pLevel, pPos, pPlayer, pHand, pHit);
        return serverUse(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    protected InteractionResult clientUse(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        return InteractionResult.PASS;
    }

    protected InteractionResult serverUse(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable BlockGetter pLevel, @NotNull List<Component> pTooltip,
                                @NotNull TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                         @NotNull BlockState pNewState, boolean pIsMoving) {
        if (pState.hasBlockEntity() && pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof AutoBlockEntity be) {
                be.dropItems(pPos.getX(), pPos.getY(), pPos.getZ());
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);

    }

    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState);

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos,
                                        @NotNull CollisionContext pContext) {
        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel,
                                                 @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return super.getCollisionShape(pState, pLevel, pPos, pContext);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return super.getOcclusionShape(pState, pLevel, pPos);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel,
                                                   @NotNull BlockPos pPos) {
        return super.getInteractionShape(pState, pLevel, pPos);
    }
}
