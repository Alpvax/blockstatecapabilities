package alpvax.test.blockstatecapabilities;

import alpvax.blockstatecapabilities.BlockStateCapabilities;
import alpvax.blockstatecapabilities.BlockStateCapabilitiesManager;
import alpvax.blockstatecapabilities.api.IBlockCapabilitiesManager;
import alpvax.blockstatecapabilities.api.RegisterBlockStateCapabilitiesEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlockStateCapabilities.MODID + "_test")
public class BlockStateCapsTest {
  private IBlockCapabilitiesManager manager = BlockStateCapabilitiesManager.INSTANCE;
  public BlockStateCapsTest() {
    //FMLJavaModLoadingContext.get().getModEventBus().addGenericListener();
    MinecraftForge.EVENT_BUS.addListener(this::interact);
    FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterBlockStateCapabilitiesEvent e) -> manager = e.getManager());
  }

  private void interact(PlayerInteractEvent.RightClickBlock e) {
    World w = e.getWorld();
    if (!w.isRemote) {
      BlockPos p = e.getPos();
      if (w.getBlockState(p).getBlock() == Blocks.CAULDRON && manager != null) {
        manager.getCapability(w, p, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(handler -> {
          FluidStack f = handler.getFluidInTank(0);
          e.getPlayer().sendMessage(new StringTextComponent(
              String.format("Cauldron contains: %dmb of %s", f.getAmount(), f.getDisplayName().getString())
          ));
        });
      }
    }
  }
}
