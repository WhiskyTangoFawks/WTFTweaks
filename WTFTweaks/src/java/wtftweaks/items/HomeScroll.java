package wtftweaks.items;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import wtfcore.WTFCore;


public class HomeScroll extends Item{
	
	public HomeScroll(){
		//this.setCreativeTab(getCreativeTab().tabTransport);
	}
	Random random = new Random();
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz){
		ChunkCoordinates home = player.getBedLocation(world.provider.dimensionId);
		if (home != null){
			--itemstack.stackSize;


			if (!world.isRemote)
			{

				EntityPlayerMP entityplayermp = (EntityPlayerMP)player;

				if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == world)
				{
					EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, home.posX, home.posY, home.posZ, 5.0F);
					if (!MinecraftForge.EVENT_BUS.post(event))
					{ // Don't indent to lower patch size
						if (player.isRiding())
						{
							player.mountEntity((Entity)null);
						}

					       for (int i = 0; i < 32; ++i)
					        {
					            world.spawnParticle("portal", home.posX, home.posY + random.nextDouble() * 2.0D, home.posZ, random.nextGaussian(), 0.0D, random.nextGaussian());
					        }
						player.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
					
					}
				}


			}
		}
		return false;

	}

	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        this.itemIcon = iconregister.registerIcon(WTFCore.WTFTweaks+":home_scroll");
    }

}
