package wtftweaks.blocks;

import java.util.Random;

import wtftweaks.entities.WTFprimedTNT;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class WTFtnt extends BlockTNT{

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;
	 @Override
	@SideOnly(Side.CLIENT)
	 public void registerBlockIcons(IIconRegister iconRegister)
		{
			this.blockIcon = iconRegister.registerIcon("minecraft:tnt_side");
			this.iconTop = iconRegister.registerIcon("minecraft:tnt_top");
			this.iconBottom = iconRegister.registerIcon("minecraft:tnt_bottom");
		}
	    @Override
		@SideOnly(Side.CLIENT)
	    public IIcon getIcon(int side, int p_149691_2_)
	    {
	        return side == 0 ? this.iconBottom : (side == 1 ? this.iconTop : this.blockIcon);
	    }

	@Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
    {
        if (!world.isRemote)
        {
            WTFprimedTNT entitytntprimed = new WTFprimedTNT(world, x + 0.5F, y + 0.5F, z + 0.5F, explosion.getExplosivePlacedBy(), world.getBlockMetadata(x, y, z));
            entitytntprimed.fuse = world.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
            world.spawnEntityInWorld(entitytntprimed);
        }
    }

	@Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return Blocks.tnt.getItemDropped(0, random, fortune);
    }

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.flint_and_steel)
		{
			this.func_150114_a(world, x, y, z, 1, player);
			world.setBlockToAir(x, y, z);
			player.getCurrentEquippedItem().damageItem(1, player);
			return true;
		}
		else if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.redstone){
			int metadata = world.getBlockMetadata(x,y,z);
			if (metadata < 15){
				world.setBlockMetadataWithNotify(x, y, z, metadata+1, 3);
				if (!player.capabilities.isCreativeMode){
					player.inventory.consumeInventoryItem(Items.redstone);
				}

				world.spawnParticle("reddust", x+0.5, y+1, z+0.5, 0.0D, 0.0D, 0.0D);
				// add a for loop to add a bunch more particles here, all around the edges of the block
				return true;
			}
			return false;
		}


		else
		{
			return super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
		}
	}


	@Override
	public void func_150114_a(World world, int x, int y, int z, int p_150114_5_, EntityLivingBase entity)
	    {
	        if (!world.isRemote)
	        {
	            if ((p_150114_5_ & 1) == 1)
	            {
	            	WTFprimedTNT entitytntprimed = new WTFprimedTNT(world, x + 0.5F, y + 0.5F, z + 0.5F, entity, world.getBlockMetadata(x, y, z));
	                world.spawnEntityInWorld(entitytntprimed);
	                world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
	            }
	        }
	    }
}
