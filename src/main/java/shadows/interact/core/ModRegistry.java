package shadows.interact.core;

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

}
