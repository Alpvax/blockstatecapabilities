package alpvax.blockstatecapabilities.vanilla;

import alpvax.blockstatecapabilities.api.RegisterBlockStateCapabilitiesEvent;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class VanillaProviders {
  //TODO: Add Config

  public static void register() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    //noinspection ConstantConditions
    modBus.addGenericListener(IFluidHandler.class, (RegisterBlockStateCapabilitiesEvent<IFluidHandler> e) ->
        e.register(Blocks.CAULDRON.getRegistryName(),
            (world, pos, side) -> LazyOptional.of(() -> new CauldronFluidHandler(world, pos))
        )
    );
  }
}
