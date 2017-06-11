package shadows.interact.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.interact.proxy.CommonProxy;

@Mod(modid = RemoteInteract.MODID, version = RemoteInteract.VERSION, name = RemoteInteract.MODNAME)

public class RemoteInteract {
	public static final String MODID = "interaction";
	public static final String MODNAME = "Remote Interaction";
	public static final String VERSION = "1.0.0";

	@SidedProxy(clientSide = "shadows.interact.proxy.ClientProxy", serverSide = "shadows.interact.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static RemoteInteract instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
