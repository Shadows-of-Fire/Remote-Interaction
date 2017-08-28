package shadows.interact.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadows.interact.core.ModRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void models(ModelRegistryEvent e) {
		ModRegistry.initModels();
	}

	public static final ModelResourceLocation INTERACTION_MRL = new ModelResourceLocation(ModRegistry.REMOTE.getRegistryName(), "inventory");
}
