package dev.fiberoptics.machineexample.block;

import dev.fiberoptics.machineexample.block.entity.ExampleMachineBlockEntity;
import dev.fiberoptics.machineexample.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
public class ExampleMachineBlock extends BaseEntityBlock {

    public ExampleMachineBlock() {
        super(Properties.of());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
                                                                            BlockEntityType<T> entityType) {
        return createTickerHelper(entityType,ModBlockEntities.MACHINE_BE.get(),ExampleMachineBlockEntity::ticker);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult result) {

        if(level.isClientSide()) return InteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof ExampleMachineBlockEntity blockEntity) {
            NetworkHooks.openScreen((ServerPlayer) player,blockEntity,pos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(newState.getBlock() != blockState.getBlock()) {
            if(level.getBlockEntity(pos)instanceof ExampleMachineBlockEntity blockEntity) {
                blockEntity.drop();
            }
        }
        super.onRemove(blockState, level, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ExampleMachineBlockEntity(blockPos, blockState);
    }
}