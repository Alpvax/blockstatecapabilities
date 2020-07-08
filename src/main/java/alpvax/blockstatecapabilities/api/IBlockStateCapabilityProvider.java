package alpvax.blockstatecapabilities.api;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface IBlockStateCapabilityProvider<T> {
  @Nonnull default LazyOptional<T> getCapability(@Nonnull IWorld world, @Nonnull BlockPos pos) {
    return getCapability(world, pos, null);
  }
  @Nonnull LazyOptional<T> getCapability(@Nonnull IWorld world, @Nonnull BlockPos pos, @Nullable Direction side);
}
