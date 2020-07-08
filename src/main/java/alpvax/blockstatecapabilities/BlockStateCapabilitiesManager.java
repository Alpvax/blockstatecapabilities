package alpvax.blockstatecapabilities;

import alpvax.blockstatecapabilities.api.IBlockCapabilitiesManager;
import alpvax.blockstatecapabilities.api.IBlockStateCapabilityProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class BlockStateCapabilitiesManager implements IBlockCapabilitiesManager {
  public static BlockStateCapabilitiesManager INSTANCE = new BlockStateCapabilitiesManager();

  private final Table<ResourceLocation, Capability<?>, IBlockStateCapabilityProvider<?>> providers = HashBasedTable.create();

  /**
   * Register a new handler for non-TileEntity blocks. DO NOT USE FOR TILE ENTITIES!!
   * If added to a block with a TileEntity, the handler will be ignored.
   * Will override any existing handler added for that block and capability combination!
   * @param name the id of the block to register the handler to.
   * @param capability the capability to handle.
   * @param handler the handler to register.
   * @param <T> the type of the capability instance.
   */
  public <T> void registerHandler(ResourceLocation name, Capability<T> capability, IBlockStateCapabilityProvider<T> handler) {
    providers.put(name, capability, handler);
  }

  /**
   * Equivalent of {@link ICapabilityProvider#getCapability(Capability, Direction)}
   * @param world the world to get the capability from
   * @param pos the position of the block in the world
   * @param capability the capability instance to request
   * @param side the side to request the capability from
   * @param <T> the type of the capability instance
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> LazyOptional<T> getCapability(IWorld world, BlockPos pos, Capability<T> capability, @Nullable Direction side) {
    TileEntity tile = world.getTileEntity(pos);
    if (tile != null) {
     return tile.getCapability(capability, side);
    }
    BlockState state = world.getBlockState(pos);
    IBlockStateCapabilityProvider<T> provider = (IBlockStateCapabilityProvider<T>)providers.get(state.getBlock().getRegistryName(), capability);
    return provider != null ? provider.getCapability(world, pos, side) : LazyOptional.empty();
  }
}
