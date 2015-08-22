package wtftweaks.blocks;

import java.util.Random;

import wtftweaks.WTFtweaks;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WTFNitreOre extends BlockOre {
		public WTFNitreOre()
		{
			super ();
			this.setHardness(3.0F);
			this.setResistance(5.0F);
			//this.setCreativeTab(getCreativeTabToDisplayOn().tabBlock);
		}

		@SideOnly(Side.CLIENT)
		public void registerBlockIcons(IIconRegister iconRegister)
		{
			this.blockIcon = iconRegister.registerIcon(WTFtweaks.modid + ":" + this.getUnlocalizedName().substring(5));
		}
	    @Override
	    public Item getItemDropped(int metadata, Random random, int fortune) {
	        return WTFtweaks.itemUnrefinedNitre;
	    }
	   @Override
	    public int quantityDropped(Random par1Random)
	    {
	        return par1Random.nextInt(4) + 1;
	    }

	}