package wtftweaks;

import java.io.File;
import wtfcore.WTFCore;
import wtfcore.utilities.BlockSets;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class WTFTweaksConfig {


	public static int nitreSpawnRate;
	public static int sulfurSpawnRate;

	public static float stoneBreakSpeed;

	public static boolean replaceExplosives;
	public static boolean replaceCreepers;

	public static boolean dirtFall;
	public static boolean cobbleFall;
	public static boolean oreFractures;
	public static boolean finiteTorches;
	public static boolean relightableTorches;
	public static boolean stoneFracturesBeforeBreaking;

	public static int enableFiniteTorch;
	public static int torchLifespan;
	public static boolean defaultOreUbification;

	public static float creeperPower;

	public static String[] oreList;
	public static boolean fallingBlocksDamage;
	public static boolean explosionFractures;

	public static boolean enableNameGetter;

	public static void customConfig() {

		Configuration config = new Configuration(new File("config/WTFTweaksConfig.cfg"));

		config.load();

		enableNameGetter = config.get("Name Getter", "When a player places a block, print out the blocks name", false).getBoolean();

		defaultOreUbification = config.get("Old UBification compatability", "Use to maintain ubification fo TCon ores for existing worlds", false).getBoolean();
		nitreSpawnRate = config.get("Ore", "Nitre spawn rate", 4).getInt();
		sulfurSpawnRate = config.get("Ore", "Sulfur spawn rate", 4).getInt();

		stoneBreakSpeed = 0.01F * (float)config.get("Mining", "Stone break speed % modifier", 20).getInt();
		oreFractures = config.get("Mining", "Ores fracture adjacent blocks when mined", true).getBoolean();
		stoneFracturesBeforeBreaking = config.get("Mining", "Stone fractures before breaking", true).getBoolean();
		explosionFractures = config.get("Mining", "Explosions fracture stone", true).getBoolean();

		String oreString = config.get("Mining", "Unlocalised block name list", "modname:unlocalisedblockname,modname:unlocalisedblockname").getString();

		String[] oreStringArray = oreString.split(",");
		for(int loop = 0; loop < oreStringArray.length; loop++)
		{
			WTFCore.log.info("WTFTweaksConfig.AddOre: block for : "+ oreStringArray[loop]);
			Block oreBlock = GameData.getBlockRegistry().getObject(oreStringArray[loop]);
			if (oreBlock != Blocks.air){
				BlockSets.addOreBlock(oreBlock);
				WTFCore.log.info("WTFTweaksConfig.AddOre: Block added to ore list: "+oreBlock.getUnlocalizedName());
			}
			else {
				WTFCore.log.info("WTFTweaksConfig.AddOre: Unable to find block for : "+"oreStringArray[loop]");
			}
		}

		replaceExplosives = config.get("Explosives", "TNT uses custom exploives", true).getBoolean();
		replaceCreepers = config.get("Explosives", "Creepers use custom explosives", true).getBoolean();
		creeperPower =  (float)config.get("Explosives", "Custom Creeper Power", 3).getDouble();

		dirtFall = config.get("Gravity", "Dirt falls when disturbed", true).getBoolean();
		cobbleFall = config.get("Gravity", "Cobblestone falls when disturbed", true).getBoolean();
		fallingBlocksDamage = config.get("Gravity", "Enable damage from falling cobblestone and dirt", true).getBoolean();

		enableFiniteTorch = config.get("Finite Torches", "0 = disable finite torches, 1 = Relight torches with a right click, 2= relight torches with a flint and steel only", 1).getInt();
		torchLifespan = config.get("Finite Torches", "Average Torch lifespan", 5).getInt();

		config.save();

	}
}
