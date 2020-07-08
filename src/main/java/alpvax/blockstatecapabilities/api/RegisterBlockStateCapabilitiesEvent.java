package alpvax.blockstatecapabilities.api;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.RegistryObject;

public class RegisterBlockStateCapabilitiesEvent extends Event {
  private final IBlockCapabilitiesManager manager;

  public RegisterBlockStateCapabilitiesEvent(IBlockCapabilitiesManager manager) {
    this.manager = manager;
  }

  public IBlockCapabilitiesManager getManager() {
    return manager;
  }

  public <T> void register(ResourceLocation id, Capability<T> capability, IBlockStateCapabilityProvider<T> handler) {
    getManager().registerHandler(id, capability, handler);
  }
  public <T> void register(RegistryObject<? super Block> block, Capability<T> capability, IBlockStateCapabilityProvider<T> handler) {
    getManager().registerHandler(block.getId(), capability, handler);
  }
}
