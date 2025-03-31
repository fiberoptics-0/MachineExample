package dev.fiberoptics.machineexample.block;

import dev.fiberoptics.machineexample.block.entity.ExampleMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
public class ExampleMachineBlock extends BaseEntityBlock {

    protected ExampleMachineBlock() {
        super(Properties.of());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ExampleMachineBlockEntity(blockPos, blockState);
    }
}