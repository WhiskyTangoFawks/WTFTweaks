package wtftweaks.configs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import wtfcore.WTFCore;
import wtfcore.api.BlockSets;
import wtfcore.utilities.LoadBlockSets;
import wtfcore.utilities.UBCblocks;
import wtftweaks.util.FracMethods;
import wtftweaks.util.WTFEventMonitor;

public class ConfigUtils {

	public static String getStringFromHashSet(HashSet<String> hashset){
		String string = "";
		Iterator<String> stringIterator = hashset.iterator();
		while (stringIterator.hasNext()){
			string = string + stringIterator.next();
		}
		return string;
	}
	
	public static void parseFallingBlocks(String fallingBlockString){
		String[] fallingBlockStringArray = fallingBlockString.split(",");
		for(int loop = 0; loop < fallingBlockStringArray.length; loop++)
		{
			String[] blockAndStability = fallingBlockStringArray[loop].split("@");
			WTFCore.log.info("WTFTweaksConfig Add Falling Block : "+ blockAndStability[0]);
			Block blockToFall = GameData.getBlockRegistry().getObject(blockAndStability[0]);
			if (blockToFall != Blocks.air){
				LoadBlockSets.addGravity(blockToFall, Integer.parseInt(blockAndStability[1]));
				WTFCore.log.info("WTFTweaksConfig.AddOre: Block added to falling block list: "+blockToFall.getUnlocalizedName());
			}
			else {
				WTFCore.log.info("WTFTweaksConfig.Add falling block: Unable to find block for : " + blockAndStability[0]);
			}
		}
	}
	
	public static void parseOreFrac(String oreString){
		
		String[] oreStringArray = oreString.split(",");
		for(int loop = 0; loop < oreStringArray.length; loop++)
		{
			WTFCore.log.info("WTFTweaksConfig.AddOre: block for : "+ oreStringArray[loop]);
			Block oreBlock = GameData.getBlockRegistry().getObject(oreStringArray[loop]);
			if (oreBlock != Blocks.air){
				LoadBlockSets.addOreBlock(oreBlock);
				WTFCore.log.info("WTFTweaksConfig.AddOre: Block added to ore list: "+oreBlock.getUnlocalizedName());
			}
			else {
				WTFCore.log.info("WTFTweaksConfig.AddOre: Unable to find block for : "+"oreStringArray[loop]");
			}
		}
	}

	public static void parseMiningSpeeds(String readMiningSpeed) {
		String[] blockStringArray = readMiningSpeed.split(",");
		for(int loop = 0; loop < blockStringArray.length; loop++)
		{
			String[] blockAndSpeed = blockStringArray[loop].split("@");
			Block block = GameData.getBlockRegistry().getObject(blockAndSpeed[0]);
			float speed = Float.parseFloat(blockAndSpeed[1]);
			
			if (block != Blocks.air){
				WTFEventMonitor.speedMod.put(block, speed);
				WTFCore.log.info("WTFTweaksConfig.AddOre: Block added to mining speed list: " + block.getUnlocalizedName() + "@"+speed);
			}
			else {
				WTFCore.log.info("WTFTweaksConfig.AddOre: Unable to find block for : "+"blockStringArray[loop]");
			}
			if (speed == 0){
				WTFCore.log.info("Speed float not parsed correctly for " + blockStringArray[loop]);
			}
		}	}
	
	
	 
	
	
}
