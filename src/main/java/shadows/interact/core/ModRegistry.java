package shadows.interact.core;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.interact.common.ItemInteract;

public class ModRegistry {
	public static final ItemInteract REMOTE = new ItemInteract("remote");
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		REMOTE.initModel();
	}

}
