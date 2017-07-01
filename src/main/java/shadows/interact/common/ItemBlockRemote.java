package shadows.interact.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.interact.core.RemoteInteract;

public class ItemBlockRemote extends Item {

	public ItemBlockRemote(String name) {
		setRegistryName(name);
		setUnlocalizedName(RemoteInteract.MODID + "." + name);
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
		GameRegistry.register(this);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		return onItemRightClick(world, player, hand).getType();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.hasTagCompound())
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		BlockPos pos = BlockPos.fromLong(stack.getTagCompound().getLong("pos"));
		EnumHand opposite = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		IBlockState state = world.getBlockState(pos);

		state.getBlock().onBlockActivated(world, pos, state, player, opposite, player.getHorizontalFacing(), 0.5F, 0.5F,
				0.5F);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		mapStack(pos, player.world, player.getHeldItemMainhand(), player);
		return true;
	}

	private ItemStack mapStack(BlockPos pos, World world, ItemStack stack, EntityPlayer player) {
		Block block = world.getBlockState(pos).getBlock();
		stack.setStackDisplayName("Interact With " + Item.getItemFromBlock(world.getBlockState(pos).getBlock())
				.getItemStackDisplayName(block.getPickBlock(world.getBlockState(pos),
						new RayTraceResult(new Vec3d(0.5, 0.5, 0.5), player.getHorizontalFacing(), pos), world, pos,
						player)));

		NBTTagLong tag = new NBTTagLong(pos.toLong());
		stack.setTagInfo("pos", tag);
		return stack;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, shadows.interact.proxy.ClientProxy.INTERACTION_MRL);
	}
}
