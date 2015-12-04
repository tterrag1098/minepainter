package hx.utils;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IBlockAccessWrapper implements IBlockAccess {

  protected IBlockAccess wrapped;
  private Block fakeBlock;
  private int fakeMeta;
  private int x, y, z;

  public IBlockAccessWrapper(IBlockAccess ba) {
    wrapped = ba;
  }
  
  public void setFakeBlock(Block block, int x, int y, int z, int meta) {
      this.fakeBlock = block;
      this.x = x;
      this.y = y;
      this.z = z;
      this.fakeMeta = meta;
  }

  @Override
  public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
    return isFakePoint(x, y, z) ? fakeBlock.isSideSolid(this, x, y, z, side) : wrapped.isSideSolid(x, y, z, side, _default);
  }

  @Override
  public int isBlockProvidingPowerTo(int x, int y, int z, int side) {
    return isFakePoint(x, y, z) ? fakeBlock.isProvidingStrongPower(this, x, y, z, side) : wrapped.isBlockProvidingPowerTo(x, x, y, z);
  }

  @Override
  public boolean isAirBlock(int x, int y, int z) {
    return isFakePoint(x, y, z) ? fakeBlock.isAir(this, x, y, x) : wrapped.isAirBlock(x, y, z);
  }

  @Override
  public TileEntity getTileEntity(int var1, int var2, int var3) {
    if (var2 >= 0 && var2 < 256) {
      return wrapped.getTileEntity(var1, var2, var3);
    } else {
      return null;
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
    return 15 << 20 | 15 << 4;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getHeight() {
    return wrapped.getHeight();
  }

  @Override
  public int getBlockMetadata(int x, int y, int z) {
    return isFakePoint(x, y, z) ? fakeMeta : wrapped.getBlockMetadata(x, y, z);
  }

  @Override
  public Block getBlock(int x, int y, int z) {
    return isFakePoint(x, y, z) ? fakeBlock : wrapped.getBlock(x, y, z);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
    return wrapped.getBiomeGenForCoords(var1, var2);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean extendedLevelsInChunkCache() {
    return wrapped.extendedLevelsInChunkCache();
  }
  
  private boolean isFakePoint(int x, int y, int z) {
      return this.x == x && this.y == y && this.z == z;
  }

}
