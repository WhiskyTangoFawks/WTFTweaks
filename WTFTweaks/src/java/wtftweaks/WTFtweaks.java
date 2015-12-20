package wtftweaks;

import wtfcore.WTFCore;
import wtftweaks.configs.WTFTweaksConfig;
import wtftweaks.entities.EntityHandler;
import wtftweaks.proxy.CommonProxy;
import wtftweaks.util.WTFEventMonitor;
import wtftweaks.worldgen.WTFWorldGen;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;




@Mod(modid = WTFtweaks.modid, name = "WhiskyTangoFox's Tweaks", version = "1.30",  dependencies = "after:UndergroundBiomes;required-after:WTFCore@[1.61,);after:CaveBiomes")
public class WTFtweaks {
	public static final String modid = WTFCore.WTFTweaks;

	@Instance(modid)
	public static WTFtweaks instance;

	public static String alphaMaskDomain = "wtftweaks:textures/blocks/alphamasks/";
	public static String overlayDomain =   "wtftweaks:textures/blocks/overlays/";

	WTFWorldGen eventWorldGen = new WTFWorldGen();

	//items


	//blocks
	
	public static Item homeScroll;

	@SidedProxy(clientSide="wtftweaks.proxy.ClientProxy", serverSide="wtftweaks.proxy.CommonProxy" )
	public static CommonProxy proxy;

	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent)
	{
		WTFTweaksConfig.customConfig();
		
		WTFItems.registerItems();
		WTFBlocks.registerBlocks();
		WTFRecipes.registerRecipes();
		WTFLoot.registerLoot();
		EntityHandler.RegisterEntityList();

		//spawns
		GameRegistry.registerWorldGenerator(eventWorldGen, 0);

		proxy.registerRenderers();

	}
	@EventHandler public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new WTFEventMonitor());
	}
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		
	}
}

