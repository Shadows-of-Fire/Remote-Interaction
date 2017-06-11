package shadows.interact.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.interact.core.ModRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModRegistry.initModels();
	}

	public static final ModelResourceLocation INTERACTION_MRL = new ModelResourceLocation(ModRegistry.REMOTE.getRegistryName(), "inventory");
	public static final ModelResourceLocation INTERACTION_MRL_GUI = new ModelResourceLocation(ModRegistry.REMOTE.getRegistryName() + "_gui", "inventory");
	public static final ModelResourceLocation INTERACTION_MRL_BLANK = new ModelResourceLocation(ModRegistry.REMOTE.getRegistryName() + "_blank", "inventory");
}
