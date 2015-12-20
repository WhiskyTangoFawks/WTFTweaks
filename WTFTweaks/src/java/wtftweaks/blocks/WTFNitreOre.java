package wtftweaks.blocks;

import java.util.Random;

import wtftweaks.WTFItems;
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
			this.blockIcon = iconRegister.registerIcon(WTFtweaks.modid + ":Nitre");
		}
	    @Override
	    public Item getItemDropped(int metadata, Random random, int fortune) {
	        return WTFItems.nitre;
	    }
	   @Override
	    public int quantityDropped(Random par1Random)
	    {
	        return par1Random.nextInt(4) + 1;
	    }

	}