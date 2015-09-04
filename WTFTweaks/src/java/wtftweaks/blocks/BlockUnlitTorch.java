package wtftweaks.blocks;

import java.util.Random;

import wtftweaks.WTFtweaks;
import wtftweaks.configs.WTFTweaksConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockUnlitTorch extends BlockTorch{

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(WTFtweaks.modid + ":" + this.getUnlocalizedName().substring(5));
	}
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)  {
		return Item.getItemFromBlock(Blocks.torch);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if(WTFTweaksConfig.enableFiniteTorch ==1) {
			world.setBlock(x,y,z, WTFtweaks.finitetorch_lit, world.getBlockMetadata(x, y, z), 3);
			return true;
		}
		else if (WTFTweaksConfig.enableFiniteTorch ==2 && player.getCurrentEquippedItem().getItem() == Items.flint_and_steel){
			world.setBlock(x,y,z, WTFtweaks.finitetorch_lit, world.getBlockMetadata(x, y, z), 3);
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
	{
		//disable random display tick
	}
}
