package wtftweaks.entities;



import java.util.Random;

import wtftweaks.WTFtweaks;
import net.minecraft.entity.EntityList;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityHandler {

	public static void RegisterEntityList()
	{
		EntityHandler.registerEntity(WTFprimedTNT.class, "WTFprimedTNT", false);
		EntityHandler.registerEntity(WTFcreeper.class, "WTFcreeper", true);
	}

	public static void registerEntity(Class entityClass, String name, Boolean doEgg)
	{
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		long x = name.hashCode();
		Random random = new Random(x);
		int mainColor = random.nextInt() * 16777215;
		int subColor = random.nextInt() * 16777215;

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, WTFtweaks.instance, 64, 1, true);
		if (doEgg)
		{
			EntityList.entityEggs.put(Integer.valueOf(entityId), new EntityList.EntityEggInfo(entityId, mainColor, subColor));
		}
	}

}