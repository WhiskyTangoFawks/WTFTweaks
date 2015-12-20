package wtftweaks.configs;

import java.io.File;
import java.util.HashSet;

import net.minecraftforge.common.config.Configuration;
import wtfcore.api.BlockSets;
import wtftweaks.util.WTFEventMonitor;

public class WTFTweaksConfig {

	public static HashSet<String> setDefaultFallingBlock = BlockSets.defaultFallingBlocks;
	
	public static int nitreSpawnRate;
	public static int sulfurSpawnRate;

	public static float stoneBreakSpeed;
	public static float sedimentaryBreakSpeed;
	public static float metamorphicBreakSpeed;
	public static float igneousBreakSpeed;
	public static float sandstoneBreakSpeed;

	public static boolean replaceExplosives;
	public static boolean replaceCreepers;

	public static boolean oreFractures;
	public static boolean finiteTorches;
	public static boolean relightableTorches;
	public static boolean stoneFracturesBeforeBreaking;

	public static int enableFiniteTorch;
	public static int torchLifespan;

	public static float creeperPower;

	public static String[] oreList;
	public static boolean fallingBlocksDamage;
	public static boolean explosionFractures;
	
	public static boolean homescroll;

	public static boolean enableNameGetter;

	public static void customConfig() {

		Configuration config = new Configuration(new File("config/WTFTweaksConfig.cfg"));

		config.load();

		/**
		 * Utilities
		 */
		enableNameGetter = config.get("Name Getter", "When a player places a block, print out the blocks name", false).getBoolean();
		
		/**
		 *Ore Spawning
		 */
		nitreSpawnRate = config.get("Ore", "Nitre spawn rate", 4).getInt();
		sulfurSpawnRate = config.get("Ore", "Sulfur spawn rate", 4).getInt();

		/**
		 * Mining Options
		 */
		
		stoneBreakSpeed = 0.01F * config.get("Mining", "Stone break speed % modifier", 20).getInt();
		sedimentaryBreakSpeed = 0.01F * config.get("Mining", "UBCS Sedimentary Stone break speed % modifier", 50).getInt();
		metamorphicBreakSpeed = 0.01F * config.get("Mining", "UBC Metamorphic break speed % modifier", 30).getInt();
		igneousBreakSpeed = 0.01F * config.get("Mining", "UBC Igneous break speed % modifier", 10).getInt();
		sandstoneBreakSpeed = 0.01F * config.get("Mining", "UBC Sandstone break speed % modifier", 50).getInt();
		
		String defaultMiningSpeed = "minecraft:stone@0.15,minecraft:sandstone@0.3,UndergroundBiomes:igneousStone@0.05,UndergroundBiomes:metamorphicStone@0.2,UndergroundBiomes:sedimentaryStone@0.3";
		String readMiningSpeed = config.get("Mining", "Mining speed modifiers", defaultMiningSpeed).getString();
		ConfigUtils.parseMiningSpeeds(readMiningSpeed);
		
		oreFractures = config.get("Mining", "Ores fracture adjacent blocks when mined", true).getBoolean();
		stoneFracturesBeforeBreaking = config.get("Mining", "Stone fractures before breaking", true).getBoolean();
		

		String oreString = config.get("Mining", "Ores to add for fracturing- modname:blockname", "minecraft:emerald_ore").getString();
		ConfigUtils.parseOreFrac(oreString);

		/**
		 * Explosions Options
		 */
		
		//String explosiveBlocks = config.get("Explosives", "List of blocks the propagate explosions", ConfigUtils.getStringFromHashSet(hashset)).getString();
		replaceExplosives = config.get("Explosives", "TNT uses custom exploives", true).getBoolean();
		replaceCreepers = config.get("Explosives", "Creepers use custom explosives", true).getBoolean();
		creeperPower =  (float)config.get("Explosives", "Custom Creeper Power", 3).getDouble();
		explosionFractures = config.get("Explosives", "Explosions fracture stone", true).getBoolean();

		/**
		 * Gravity Options
		 */
		
		String fallingBlockString = config.get("Gravity", "Block name and number of identical blocks above requried to prevent falling if disturbed by player", ConfigUtils.getStringFromHashSet(setDefaultFallingBlock)).getString();
		ConfigUtils.parseFallingBlocks(fallingBlockString);
			
		fallingBlocksDamage = config.get("Gravity", "Enable damage from falling cobblestone and dirt", true).getBoolean();

		
		/**
		 * Torches Options
		 */
		enableFiniteTorch = config.get("Finite Torches", "0 = disable finite torches, 1 = Relight torches with a right click, 2= relight torches with a flint and steel only", 1).getInt();
		torchLifespan = config.get("Finite Torches", "Average Torch lifespan", 5).getInt();
		

		/**
		 * Item Options
		 */
		homescroll = config.get("Items", "Enable home scrolls", true).getBoolean();
		
		config.save();

	}
}
