package alpvax.blockstatecapabilities;

import alpvax.blockstatecapabilities.api.IBlockCapabilitiesManager;
import alpvax.blockstatecapabilities.api.RegisterBlockStateCapabilitiesEvent;
import alpvax.blockstatecapabilities.vanilla.VanillaProviders;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BlockStateCapabilities.MODID)
public class BlockStateCapabilities
{
    public static final String MODID = "blockstatecapabilities";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public BlockStateCapabilities() {
        VanillaProviders.register();
        /*TODO: Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigOptions.SPEC);
        modBus.addListener(ConfigOptions::onModConfigEvent);
        */
    }

    @SuppressWarnings("unchecked")
    @CapabilityInject(IItemHandler.class)
    private static void fireCapabilityEvents(Capability<IItemHandler> itemHandlerCapability) {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IBlockCapabilitiesManager manager = BlockStateCapabilitiesManager.INSTANCE;
        try {
            Field f = CapabilityManager.class.getDeclaredField("providers");
            f.setAccessible(true);
            IdentityHashMap<String, Capability<?>> providers = (IdentityHashMap<String, Capability<?>>) f.get(CapabilityManager.INSTANCE);
            providers.forEach((name, capability) -> postCapabilityEvent(modBus, manager, name, capability));
        } catch (NoSuchFieldException e) {
            LOGGER.error("Unable to hook into CapabilityManager callbacks (NoSuchMethod)", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Unable to hook into CapabilityManager callbacks (IllegalAccess)", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void postCapabilityEvent(IEventBus modBus, IBlockCapabilitiesManager manager, String name, Capability<T> capability) {
        try {
            Class<T> type = (Class<T>) Class.forName(name.substring(1));
            modBus.post(new RegisterBlockStateCapabilitiesEvent<T>(type, manager, capability));
        } catch (ClassNotFoundException e) {
            LOGGER.error("Error finding Capability class: {} (ClassNotFound)", name.substring(1), e);
        }
    }
}
