package wtftweaks.proxy;



import wtftweaks.entities.WTFprimedTNT;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;

public class ClientProxy extends CommonProxy {


	@Override
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(WTFprimedTNT.class, new RenderTNTPrimed());
	}
}