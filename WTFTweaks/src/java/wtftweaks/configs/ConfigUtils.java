package wtftweaks.configs;

import java.util.HashSet;
import java.util.Iterator;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import wtfcore.WTFCore;
import wtfcore.tweaksmethods.FracMethods;
import wtfcore.utilities.BlockSets;

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
				BlockSets.addGravity(blockToFall, Integer.parseInt(blockAndStability[1]));
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
				BlockSets.addOreBlock(oreBlock, FracMethods.defaultfrac);
				WTFCore.log.info("WTFTweaksConfig.AddOre: Block added to ore list: "+oreBlock.getUnlocalizedName());
			}
			else {
				WTFCore.log.info("WTFTweaksConfig.AddOre: Unable to find block for : "+"oreStringArray[loop]");
			}
		}
	}
	
	 
	
	
}
