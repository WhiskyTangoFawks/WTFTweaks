package wtftweaks.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import wtfcore.api.BlockInfo;
import wtfcore.api.BlockSets;


public class FracMethods {

	static Random random = new Random();


	public static void fracture(World world, int x, int y, int z) {
		HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();
		hashset.add(new ChunkPosition(x+1, y, z));
		hashset.add(new ChunkPosition(x-1, y, z));
		hashset.add(new ChunkPosition(x, y+1, z));
		hashset.add(new ChunkPosition(x, y-1, z));
		hashset.add(new ChunkPosition(x, y, z+1));
		hashset.add(new ChunkPosition(x, y, z-1));
		FracIterator(world, hashset);
	}

	public static void WTFOresFracture(World world, int x, int y, int z, Block block, int metadata) {
		HashSet<ChunkPosition> hashset= null;

		int harvestLevel =block.getHarvestLevel(metadata); 
		if (harvestLevel <= Blocks.coal_ore.getHarvestLevel(0)){
			for (int loop = 0; loop < 4; loop++){
				hashset = fracLite(world, x, y, z);
			}
		}
		else if (harvestLevel < Blocks.iron_ore.getHarvestLevel(0)){
			for (int loop = 0; loop < 4; loop++){
				hashset = fracStandard(world, x, y, z);
			}
		}
		else{
			for (int loop = 0; loop < 4; loop++){
				hashset = fracCrack(world, x, y, z);
			}
		}

		FracIterator(world, hashset);
	}

	public static HashSet<ChunkPosition> fracStandard(World world, int x, int y, int z){
		HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();
		Block blockToFracture = Blocks.cobblestone;
		ChunkPosition chunkposition = new ChunkPosition(x,y,z);
		while (BlockSets.stoneAndCobble.containsValue(blockToFracture)){
			chunkposition = getRandom(chunkposition, random.nextInt(6));
			blockToFracture = world.getBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ);
		}

		if  (BlockSets.stoneAndCobble.containsKey(blockToFracture)){
			hashset.add(chunkposition);
		}
		return hashset;
	}

	public static HashSet<ChunkPosition> fracLite(World world, int x, int y, int z){
		HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();
		Block blockToFracture = Blocks.cobblestone;
		ChunkPosition chunkposition = new ChunkPosition(x,y,z);
		chunkposition = getRandom(chunkposition, random.nextInt(6));
		blockToFracture = world.getBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ);

		if  (BlockSets.stoneAndCobble.containsKey(blockToFracture)){
			hashset.add(chunkposition);
		}
		return hashset;
	}
	public static HashSet<ChunkPosition> fracCrack(World world, int x, int y, int z){
		HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();
		ChunkPosition chunkposition = new ChunkPosition(x,y,z);
		Block blockToFracture = Blocks.cobblestone;
		int frac = random.nextInt(6);
		chunkposition = getRandom(chunkposition, frac);

		blockToFracture = world.getBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ);
		while (BlockSets.stoneAndCobble.containsValue(blockToFracture)){
			chunkposition = getRandom(chunkposition, frac);
			blockToFracture = (world.getBlock(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ));
		}

		if  (BlockSets.stoneAndCobble.containsKey(blockToFracture)){
			hashset.add(chunkposition);
			if (random.nextBoolean()){
				chunkposition = getRandom(chunkposition, frac);
				hashset.add(chunkposition);
			}
		}
		return hashset;
	}

	public static ChunkPosition getRandom(ChunkPosition chunkposition, int frac){
		switch (frac){
		case 0 :
			return new ChunkPosition(chunkposition.chunkPosX-1, chunkposition.chunkPosY, chunkposition.chunkPosZ);
		case 1 :
			return new ChunkPosition(chunkposition.chunkPosX+1, chunkposition.chunkPosY, chunkposition.chunkPosZ);
		case 2 :
			return new ChunkPosition(chunkposition.chunkPosX, chunkposition.chunkPosY-1, chunkposition.chunkPosZ);
		case 3 :
			return new ChunkPosition(chunkposition.chunkPosX, chunkposition.chunkPosY+1, chunkposition.chunkPosZ);
		case 4 :
			return new ChunkPosition(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ-1);
		case 5 :
			return new ChunkPosition(chunkposition.chunkPosX-1, chunkposition.chunkPosY, chunkposition.chunkPosZ+1);
		}
		return chunkposition;
	}

	public static void FracIterator(World world, HashSet<ChunkPosition> hashset){
		ChunkPosition chunkposition;
		Iterator<ChunkPosition> iterator = hashset.iterator();
		while (iterator.hasNext()){
			chunkposition = iterator.next();
			fracStone(chunkposition.chunkPosX, chunkposition.chunkPosY, chunkposition.chunkPosZ, world);
		}
	}

	public static boolean fracStone(int x, int y, int z, World world){
		Block blockToSet = BlockSets.blockTransformer.get(new BlockInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), BlockSets.Modifier.cobblestone));
		if (blockToSet != null) { 
			world.setBlock(x,  y,  z, blockToSet, world.getBlockMetadata(x, y, z), 3);
			return true;
		}
		return false;
	}

}
