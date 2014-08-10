package hx.minepainter.sculpture;

import hx.utils.Debug;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SculptureEntity extends TileEntity{

	Sculpture sculpture = new Sculpture();
	
	@SideOnly(Side.CLIENT)
	SculptureRenderCompiler render = new SculptureRenderCompiler();
	
	public Sculpture sculpture(){
		return sculpture;
	}
	
	@Override @SideOnly(Side.CLIENT)
	public void updateEntity(){
		if(this.worldObj.isRemote){
			BlockSlice slice = BlockSlice.at(worldObj, xCoord, yCoord, zCoord);
			if(render.update(slice))
				worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
			slice.clear();
		}
	}
	
    @Override
	public void invalidate()
	{
        super.invalidate();
        if(this.worldObj.isRemote)
			render.clear();
	}
	
        @Override
	public void onChunkUnload()
	{
        super.onChunkUnload();
		if(this.worldObj.isRemote)
			render.clear();
	}

	//TileEntity util

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		sculpture.write(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		sculpture.read(nbt);
	}
	
	public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 17, nbttagcompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }
}
