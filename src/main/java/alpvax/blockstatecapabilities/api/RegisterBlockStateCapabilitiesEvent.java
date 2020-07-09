package alpvax.blockstatecapabilities.api;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.RegistryObject;

public class RegisterBlockStateCapabilitiesEvent<T> extends GenericEvent<T> {
  private final IBlockCapabilitiesManager manager;
  private final Capability<T> capability;

  public RegisterBlockStateCapabilitiesEvent(Class<T> type, IBlockCapabilitiesManager manager, Capability<T> capability) {
    super(type);
    this.manager = manager;
    this.capability = capability;
  }

  public IBlockCapabilitiesManager getManager() {
    return manager;
  }
  public Capability<T> getCapability() {
    return capability;
  }

  public void register(ResourceLocation id, IBlockStateCapabilityProvider<T> handler) {
    getManager().registerHandler(id, capability, handler);
  }
  public void register(RegistryObject<? super Block> block, IBlockStateCapabilityProvider<T> handler) {
    getManager().registerHandler(block.getId(), capability, handler);
  }
}
