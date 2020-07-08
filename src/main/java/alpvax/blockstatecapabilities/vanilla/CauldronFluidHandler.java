package alpvax.blockstatecapabilities.vanilla;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class CauldronFluidHandler implements IFluidHandler {
  private final IWorld world;
  private final BlockPos pos;
  private BlockState state;

  public CauldronFluidHandler(IWorld world, BlockPos pos) {
    this.world = world;
    this.pos = pos;
  }
  protected boolean validate() {
    state = world.getBlockState(pos);
    return state.getBlock() == Blocks.CAULDRON;
  }
  private int getFluidLevel() {
    return state.get(CauldronBlock.LEVEL);
  }
  private int nextFluidRequired() {
    switch (getFluidLevel()) {
      case 0:
      case 1:
        return 333;
      case 2:
        return 334;
      default: return 0;
    }
  }
  private int getFluidMB() {
    switch (getFluidLevel()) {
      case 1: return 333;
      case 2: return 666;
      case 3: return 1000;
      default: return 0;
    }
  }

  @Override
  public int getTanks() {
    return validate() ? 1 : 0;
  }

  @Nonnull
  @Override
  public FluidStack getFluidInTank(int tank) {
    if (!validate() || tank != 0) {
      return FluidStack.EMPTY;
    }
    int amount = 333 * getFluidLevel();
    if (amount >= 999) {
      amount = 1000;
    }
    return amount > 0 ? new FluidStack(Fluids.WATER, amount) : FluidStack.EMPTY;
  }

  @Override
  public int getTankCapacity(int tank) {
    return validate() && tank == 0 ? 1000 : 0;
  }

  @Override
  public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
    return validate() && tank == 0 && Fluids.WATER.isEquivalentTo(stack.getFluid());
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (!isFluidValid(0, resource)) {
      return 0;
    }
    int remaining = resource.getAmount();
    int amount = 0;
    int num = getFluidLevel();
    int next = nextFluidRequired();
    while (next > 0 && remaining >= next) {
      amount += next;
      remaining -= next;
      num += 1;
    }
    if (amount > 0 && action.execute()) {
      world.setBlockState(pos, state.with(CauldronBlock.LEVEL, num), Constants.BlockFlags.DEFAULT/*_AND_RERENDER*/);
    }
    return amount;
  }

  @Nonnull
  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    return isFluidValid(0, resource) ? drain(resource.getAmount(), action) : FluidStack.EMPTY;
  }

  @Nonnull
  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    if (!validate()) {
      return FluidStack.EMPTY;
    }
    int available = getFluidMB();
    int amount = 0;
    int num = getFluidLevel();
    if (maxDrain >= available) {
      amount = available;
      num = 0;
    } else {
      int remaining = maxDrain;
      while (num > 0 && remaining >= 333) {
        int change = num > 2 ? 334 : 333;
        amount += change;
        remaining -= change;
        num -= 1;
      }
    }
    if (amount > 0) {
      if (action.execute()) {
        world.setBlockState(pos, state.with(CauldronBlock.LEVEL, num), Constants.BlockFlags.DEFAULT/*_AND_RERENDER*/);
      }
      return new FluidStack(Fluids.WATER, amount);
    }
    return FluidStack.EMPTY;
  }
}
