package shadows.interact.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.interact.core.RemoteInteract;
import shadows.interact.proxy.ClientProxy;

public class ItemBlockRemote extends Item {

	public ItemBlockRemote(String name) {
		setRegistryName(name);
		setUnlocalizedName(RemoteInteract.MODID + "." + name);
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			mapStack(pos, player.world, player.getHeldItemMainhand(), player, hitX, hitY, hitZ);
			return EnumActionResult.SUCCESS;
		}
		return onItemRightClick(world, player, hand).getType();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.hasTagCompound()) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		NBTTagCompound tag = stack.getTagCompound();
		BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
		EnumHand opposite = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		if (world.isBlockLoaded(pos)) {
			IBlockState state = world.getBlockState(pos);

			float x = tag.getFloat("x");
			float y = tag.getFloat("y");
			float z = tag.getFloat("z");

			boolean flag;//Need the tile to think the player is near the tile, whilist keeping the player over here.  Might need a player wrapper that delegates to two different players for position and gui opening.

			if (flag = !state.getBlock().onBlockActivated(world, pos, state, player, opposite, player.getHorizontalFacing(), x, y, z)) {
				EnumActionResult res = player.getHeldItem(opposite).onItemUse(player, world, pos, opposite, player.getHorizontalFacing(), x, y, z);
				return new ActionResult<ItemStack>(res, stack);
			} else if (!flag) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	private ItemStack mapStack(BlockPos pos, World world, ItemStack stack, EntityPlayer player, float hitX, float hitY, float hitZ) {
		Block block = world.getBlockState(pos).getBlock();
		stack.setStackDisplayName("Interact With " + Item.getItemFromBlock(world.getBlockState(pos).getBlock()).getItemStackDisplayName(block.getPickBlock(world.getBlockState(pos), new RayTraceResult(new Vec3d(hitX, hitY, hitZ), player.getHorizontalFacing(), pos), world, pos, player)));

		NBTTagLong tag = new NBTTagLong(pos.toLong());
		stack.setTagInfo("pos", tag);
		stack.getTagCompound().setFloat("x", hitX);
		stack.getTagCompound().setFloat("y", hitY);
		stack.getTagCompound().setFloat("z", hitZ);
		return stack;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, ClientProxy.INTERACTION_MRL);
	}
}
