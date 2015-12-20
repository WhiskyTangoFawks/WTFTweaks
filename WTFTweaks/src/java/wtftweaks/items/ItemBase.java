package wtftweaks.items;


import wtftweaks.WTFtweaks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemBase extends Item {

	public ItemBase()
	{
		//this.setCreativeTab(getCreativeTab().tabMaterials);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
	this.itemIcon = iconRegister.registerIcon(WTFtweaks.modid + ":" + this.getUnlocalizedName().substring(5));
	}
}