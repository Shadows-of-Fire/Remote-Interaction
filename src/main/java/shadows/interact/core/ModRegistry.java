package shadows.interact.core;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.interact.common.ItemBlockRemote;
import shadows.interact.common.ItemEntityRemote;

public class ModRegistry {
	public static final ItemBlockRemote REMOTE = new ItemBlockRemote("remote");
	public static final ItemEntityRemote REMOTE_E = new ItemEntityRemote("entity_remote");

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		REMOTE.initModel();
		REMOTE_E.initModel();
	}

	@SubscribeEvent
	public void onItemRegister(Register<Item> e) {
		e.getRegistry().registerAll(REMOTE, REMOTE_E);
	}

}
