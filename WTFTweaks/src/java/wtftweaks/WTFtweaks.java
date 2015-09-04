package wtftweaks;

import wtfcore.WTFCore;
import wtftweaks.blocks.BlockLitTorch;
import wtftweaks.blocks.BlockUnlitTorch;
import wtftweaks.blocks.WTFNitreOre;
import wtftweaks.blocks.WTFSulfurOre;
import wtftweaks.blocks.WTFtnt;
import wtftweaks.configs.WTFTweaksConfig;
import wtftweaks.entities.EntityHandler;
import wtftweaks.items.WTFItems;
import wtftweaks.proxy.CommonProxy;
import wtftweaks.util.WTFEventMonitor;
import wtftweaks.worldgen.WTFWorldGen;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
//import exterminatorJeff.undergroundBiomes.api.UBAPIHook;



@Mod(modid = WTFtweaks.modid, name = "WhiskyTangoFox's Tweaks", version = "1.23",  dependencies = "after:UndergroundBiomes;required-after:WTFCore@[1.52,);after:CaveBiomes")
public class WTFtweaks {
	public static final String modid = WTFCore.WTFTweaks;

	@Instance(modid)
	public static WTFtweaks instance;

	public static String alphaMaskDomain = "wtftweaks:textures/blocks/alphamasks/";
	public static String overlayDomain =   "wtftweaks:textures/blocks/overlays/";

	WTFWorldGen eventWorldGen = new WTFWorldGen();

	//items
	public static Item itemUnrefinedNitre;
	public static Item itemUnrefinedSulfur;

	//blocks
	public static Block oreNitreOre;
	public static Block oreSulfurOre;
	public static Block blockWTFtnt;
	public static Block finitetorch_lit;
	public static Block finitetorch_unlit;


	@SidedProxy(clientSide="wtftweaks.proxy.ClientProxy", serverSide="wtftweaks.proxy.CommonProxy" )
	public static CommonProxy proxy;

	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent)
	{

		//items
		itemUnrefinedSulfur = new WTFItems().setUnlocalizedName("UnrefinedSulfur");
		GameRegistry.registerItem(itemUnrefinedSulfur, "UnrefinedSulfur");

		itemUnrefinedNitre = new WTFItems().setUnlocalizedName("UnrefinedNitre");
		GameRegistry.registerItem(itemUnrefinedNitre, "UnrefinedNitre");

		//Blocks
		oreNitreOre = new WTFNitreOre().setBlockName("nitre_ore");
		GameRegistry.registerBlock(oreNitreOre, "nitre_ore");

		oreSulfurOre = new WTFSulfurOre().setBlockName("sulfur_ore");
		GameRegistry.registerBlock(oreSulfurOre, "sulfur_ore");

		blockWTFtnt = new WTFtnt().setBlockName("WTFtnt");
		GameRegistry.registerBlock(blockWTFtnt, "WTFtnt");

		finitetorch_lit = new BlockLitTorch().setBlockName("finite_torch_lit").setLightLevel(0.67F).setTickRandomly(true);
		GameRegistry.registerBlock(finitetorch_lit, "finite_torch_lit");

		finitetorch_unlit = new BlockUnlitTorch().setBlockName("finite_torch_unlit");
		GameRegistry.registerBlock(finitetorch_unlit, "finite_torch_unlit");

		EntityHandler.RegisterEntityList();



		//spawns
		GameRegistry.registerWorldGenerator(eventWorldGen, 0);

		proxy.registerRenderers();


	}
	@EventHandler public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new WTFEventMonitor());

	//recipes
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gunpowder), new Object[] {WTFtweaks.itemUnrefinedSulfur, WTFtweaks.itemUnrefinedNitre, WTFtweaks.itemUnrefinedNitre, WTFtweaks.itemUnrefinedNitre, new ItemStack(Items.coal, 1, 1)});
	}
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		WTFTweaksConfig.customConfig();

	}
}

