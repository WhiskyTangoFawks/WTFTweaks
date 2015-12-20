package wtftweaks.util;

import java.util.HashMap;
import java.util.Random;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import wtfcore.WTFCore;
import wtfcore.blocks.OreChildBlock;
import wtfcore.utilities.LoadBlockSets;
import wtfcore.utilities.UBCblocks;
import wtftweaks.WTFBlocks;
import wtftweaks.WTFtweaks;
import wtftweaks.configs.WTFTweaksConfig;
import wtftweaks.entities.WTFcreeper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class WTFEventMonitor {

	Random random = new Random();
	public static HashMap<Block, Float> speedMod = Maps.newHashMap();

	@SubscribeEvent
	public void SpawnReplacer (LivingSpawnEvent event)
	{
		if (WTFTweaksConfig.replaceCreepers && !event.world.isRemote && event.entityLiving instanceof EntityCreeper)
		{
			EntityCreeper vCreeper = (EntityCreeper) event.entityLiving;
			if (vCreeper.getCanSpawnHere())
			{
				WTFcreeper newCreeper = new WTFcreeper(event.world);
				newCreeper.setLocationAndAngles(vCreeper.posX, vCreeper.posY, vCreeper.posZ, vCreeper.rotationYaw, vCreeper.rotationPitch);
				event.world.spawnEntityInWorld(newCreeper);
				vCreeper.setDead();
			}
		}
	}


	//slows mining of stone
	@SubscribeEvent
	public void StoneBreakSpeed (BreakSpeed event)
	{
		if (!event.entityPlayer.capabilities.isCreativeMode){
			if (speedMod.containsKey(event.block)){
				event.newSpeed = speedMod.get(event.block) * event.originalSpeed;
			}	
		}
	}
		
	

	//Deals with whenever a player places a block
	@SubscribeEvent
	public void PlayerPlaceBlock (PlaceEvent event)
	{

		if(WTFTweaksConfig.enableNameGetter){
			event.player.addChatMessage(new ChatComponentText("The block name is : " + GameData.getBlockRegistry().getNameForObject(event.block)));
			event.player.addChatMessage(new ChatComponentText("The block metadata is : " + event.world.getBlockMetadata(event.x, event.y, event.z)));
		}


		Block block = event.block;
		if (LoadBlockSets.shouldFall(event.block)){

			WTFmethods.dropBlock(event.world, event.x, event.y, event.z);
		}
		if (!event.player.capabilities.isCreativeMode){
			if (WTFTweaksConfig.replaceExplosives && event.block == Blocks.tnt)
			{
				event.world.setBlock(event.x, event.y, event.z, WTFBlocks.blockWTFtnt);
			}
			if (WTFTweaksConfig.oreFractures && LoadBlockSets.isOre(block)){
				event.setCanceled (true);
			}
			if (WTFTweaksConfig.enableFiniteTorch > 0 && event.block == Blocks.torch){
				event.world.setBlock(event.x, event.y, event.z, WTFBlocks.finitetorch_unlit, event.world.getBlockMetadata(event.x, event.y, event.z), 3);;
			}
		}
	}

	@SubscribeEvent
	public void BlockBreakEvent(BreakEvent event)
	{
		int x = event.x;
		int y = event.y;
		int z = event.z;
		World world = event.world;
		Block block = event.block;

		if (!event.getPlayer().capabilities.isCreativeMode && LoadBlockSets.hasCobblestone(block) && WTFTweaksConfig.stoneFracturesBeforeBreaking)
		{
			if (FracMethods.fracStone(x, y, z, world)){
				event.setResult(Event.Result.DENY);
				if (event.getPlayer().getHeldItem() != null){
					event.getPlayer().getHeldItem().attemptDamageItem(1, random);
				}
			}
		}
		//This checks if the block is an ore block, then calls the fracture method associated with it in the ore blocks hashmap if it is

	
		if (LoadBlockSets.isOre(block) && WTFTweaksConfig.oreFractures)
		{
			if (event.block instanceof OreChildBlock){ 
				FracMethods.WTFOresFracture(world, x, y, z, event.block, event.blockMetadata);
			}
			else {
				FracMethods.fracture(world, x, y, z);
			}
			
		}
		//checks for fall of the block above the block thats been broken
		WTFmethods.disturbBlock(event.world, x, y+1, z);

	}

	@SubscribeEvent
	public void BlockHarvestEvent(HarvestDropsEvent event){
		if (event.block instanceof BlockLeavesBase){
			if (random.nextBoolean()){
				event.drops.add(new ItemStack(Items.stick, random.nextInt(1)+random.nextInt(2)));
			}
		}
		
	}



}
