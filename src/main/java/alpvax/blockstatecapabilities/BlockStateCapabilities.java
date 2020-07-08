package alpvax.blockstatecapabilities;

import alpvax.blockstatecapabilities.api.RegisterBlockStateCapabilitiesEvent;
import alpvax.blockstatecapabilities.vanilla.CauldronFluidHandler;
import alpvax.blockstatecapabilities.vanilla.VanillaProviders;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BlockStateCapabilities.MODID)
public class BlockStateCapabilities
{
    public static final String MODID = "blockstatecapabilities";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public BlockStateCapabilities() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        //modBus.addListener(VanillaProviders::register);
        modBus.addListener(
            (FMLCommonSetupEvent e) ->
                //modBus.post(new RegisterBlockStateCapabilitiesEvent(new BlockStateCapabilitiesManager()))
                modBus.post(new RegisterBlockStateCapabilitiesEvent(BlockStateCapabilitiesManager.INSTANCE))
        );
        /*TODO: Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigOptions.SPEC);
        modBus.addListener(ConfigOptions::onModConfigEvent);
        */
    }

    @CapabilityInject(IFluidHandler.class)
    private static void registerFluidCap(Capability<IFluidHandler> cap) {
        BlockStateCapabilitiesManager.INSTANCE.registerHandler(Blocks.CAULDRON, cap, (world, pos, side) -> LazyOptional.of(() -> new CauldronFluidHandler(world, pos)));
    }
}
