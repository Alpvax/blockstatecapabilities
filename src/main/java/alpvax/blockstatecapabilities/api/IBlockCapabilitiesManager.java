package alpvax.blockstatecapabilities.api;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public interface IBlockCapabilitiesManager {

  /**
   * Convenience method to create a standard {@link ICapabilityProvider} for the specified world and position.
   * Can be safely cached.
   */
  default ICapabilityProvider getProvider(IWorld world, BlockPos pos) {
    return new ICapabilityProvider() {
      @Nonnull
      @Override
      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return IBlockCapabilitiesManager.this.getCapability(world, pos, cap, side);
      }
    };
  }
  /**
   * Convenience method to create a standard {@link ICapabilityProvider} from an {@link IWorldPosCallable}.
   * Can be safely cached.
   * Equivalent to calling {@linkplain #getProvider(IWorld, BlockPos)}
   */
  default ICapabilityProvider getProvider(IWorldPosCallable callable) {
    //noinspection OptionalGetWithoutIsPresent
    return callable.apply(this::getProvider).get();
  }

  /**
   * Register a new handler for non-TileEntity blocks. DO NOT USE FOR TILE ENTITIES!!
   * If added to a block with a TileEntity, the handler will be ignored.
   * Will override any existing handler added for that block and capability combination!
   * @param block the block to register the handler to.
   * @param capability the capability to handle.
   * @param handler the handler to register.
   * @param <T> the type of the capability instance.
   */
  default  <T> void registerHandler(Block block, Capability<T> capability, IBlockStateCapabilityProvider<T> handler) {
    registerHandler(Objects.requireNonNull(block.getRegistryName()), capability, handler);
  }
  /**
   * Register a new handler for non-TileEntity blocks. DO NOT USE FOR TILE ENTITIES!!
   * If added to a block with a TileEntity, the handler will be ignored.
   * Will override any existing handler added for that block and capability combination!
   * @param block the block to register the handler to.
   * @param capability the capability to handle.
   * @param handler the handler to register.
   * @param <T> the type of the capability instance.
   */
  default  <T> void registerHandler(RegistryObject<? extends Block> block, Capability<T> capability, IBlockStateCapabilityProvider<T> handler) {
    registerHandler(Objects.requireNonNull(block.getId()), capability, handler);
  }
  /**
   * Register a new handler for non-TileEntity blocks. DO NOT USE FOR TILE ENTITIES!!
   * If added to a block with a TileEntity, the handler will be ignored.
   * Will override any existing handler added for that block and capability combination!
   * @param name the id of the block to register the handler to.
   * @param capability the capability to handle.
   * @param handler the handler to register.
   * @param <T> the type of the capability instance.
   */
  <T> void registerHandler(ResourceLocation name, Capability<T> capability, IBlockStateCapabilityProvider<T> handler);

  /**
   * Equivalent of {@link ICapabilityProvider#getCapability(Capability)}
   * @param world the world to get the capability from
   * @param pos the position of the block in the world
   * @param capability the capability instance to request
   * @param <T> the type of the capability instance
   */
  default  <T> LazyOptional<T> getCapability(IWorld world, BlockPos pos, Capability<T> capability) {
    return getCapability(world, pos, capability, null);
  }

  /**
   * Equivalent of {@link ICapabilityProvider#getCapability(Capability, Direction)}
   * @param world the world to get the capability from
   * @param pos the position of the block in the world
   * @param capability the capability instance to request
   * @param side the side to request the capability from
   * @param <T> the type of the capability instance
   */
  <T> LazyOptional<T> getCapability(IWorld world, BlockPos pos, Capability<T> capability, @Nullable Direction side);
}
