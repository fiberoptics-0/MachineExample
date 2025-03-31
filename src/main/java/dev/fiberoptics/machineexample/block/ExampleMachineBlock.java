package dev.fiberoptics.machineexample.block;

import dev.fiberoptics.machineexample.block.entity.ExampleMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
public class ExampleMachineBlock extends BaseEntityBlock {

    public ExampleMachineBlock() {
        super(Properties.of());
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