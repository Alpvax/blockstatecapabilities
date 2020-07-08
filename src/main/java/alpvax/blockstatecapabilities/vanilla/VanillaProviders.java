package alpvax.blockstatecapabilities.vanilla;

import alpvax.blockstatecapabilities.api.RegisterBlockStateCapabilitiesEvent;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class VanillaProviders {
  //TODO: Add Config

  @SuppressWarnings("ConstantConditions")
  public static void register(RegisterBlockStateCapabilitiesEvent e) {
    e.register(Blocks.CAULDRON.getRegistryName(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
        (world, pos, side) -> LazyOptional.of(() -> new CauldronFluidHandler(world, pos))
    );
  }
}
