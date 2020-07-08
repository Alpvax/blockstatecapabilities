package alpvax.blockstatecapabilities;

import alpvax.blockstatecapabilities.api.RegisterBlockStateCapabilitiesEvent;
import alpvax.blockstatecapabilities.vanilla.VanillaProviders;
import net.minecraftforge.eventbus.api.IEventBus;
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
        modBus.addListener(VanillaProviders::register);
        modBus.addListener(
            (FMLCommonSetupEvent e) ->
                modBus.post(new RegisterBlockStateCapabilitiesEvent(new BlockStateCapabilitiesManager()))
        );
        /*TODO: Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigOptions.SPEC);
        modBus.addListener(ConfigOptions::onModConfigEvent);
        */
    }
}
