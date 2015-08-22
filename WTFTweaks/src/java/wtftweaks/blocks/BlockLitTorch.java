package wtftweaks.blocks;


import java.util.Random;

import wtftweaks.WTFTweaksConfig;
import wtftweaks.WTFtweaks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockLitTorch extends BlockTorch// implements ITileEntityProvider
{
/*
	@Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TorchTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata) {

        return true;
    }
*/

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)  {
		return Item.getItemFromBlock(Blocks.torch);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random){
    	if (WTFTweaksConfig.enableFiniteTorch > 0 && random.nextInt(WTFTweaksConfig.torchLifespan) == 0){
    		world.setBlock(x, y, z, WTFtweaks.finitetorch_unlit);
    	}
    }

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_)
	{
		return Blocks.torch.getIcon(p_149691_1_, p_149691_2_);
	}

}
