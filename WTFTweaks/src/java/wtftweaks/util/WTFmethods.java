package wtftweaks.util;

import java.util.HashSet;

import wtftweaks.configs.WTFTweaksConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import wtfcore.api.BlockSets;
import wtfcore.utilities.LoadBlockSets;

public class WTFmethods {

	//turns blocks at the given location from stone to cobblestone

	public static void dropBlock(World world, int x, int y, int z)
	{
		HashSet<Block> hashset = new HashSet<Block>();
		for (int xloop = -2; xloop < 3; xloop++){
			for (int yloop = -1; yloop < 1; yloop++){
				for (int zloop = -2; zloop < 3; zloop++){
					hashset.add(world.getBlock(x+xloop, y+yloop, z+zloop));
				}
			}
		}

		if (!hashset.contains(Blocks.fence)){

			if (LoadBlockSets.shouldFall(world.getBlock(x, y, z))){
			//	if (world.getBlock(x,  y,  z) == Blocks.grass){world.setBlock(x, y, z, Blocks.dirt);}
				EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, x + 0.5F, y + 0.5F, z + 0.5F, world.getBlock(x,  y,  z), world.getBlockMetadata(x, y, z));
				if (WTFTweaksConfig.fallingBlocksDamage){
					entityfallingblock.func_145806_a(true);
				}
				world.spawnEntityInWorld(entityfallingblock);
				disturbBlock(world, x, y+1, z);
			}
		}
	}

	public static void disturbBlock(World world, int x, int y, int z){

		Block block = world.getBlock(x, y, z);
		if (LoadBlockSets.shouldFall(block)){
			int stability = LoadBlockSets.getStabilty(block);
			boolean fall = false;
			for (int loop = 1; loop < stability +1; loop++){
				if (world.getBlock(x, y+loop, z) != block){fall = true;}
			}
			if (fall){
				dropBlock(world, x, y, z);
			}
		}
	}


}
