package shadows.interact.proxy;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import shadows.interact.core.ModRegistry;
import shadows.interact.core.RecipeRegistry;
import shadows.interact.core.RemoteInteract;
import shadows.interact.util.EntityIDMessage;
import shadows.interact.util.EntityIDMessage.IDHandler;

public class CommonProxy {

	public static Configuration config;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(RemoteInteract.MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		RecipeRegistry.init();
	}

	public void init(FMLInitializationEvent e) {
		INSTANCE.registerMessage(IDHandler.class, EntityIDMessage.class, 0, Side.CLIENT);
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
