package shadows.interact.common;

import java.util.UUID;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.interact.core.RemoteInteract;
import shadows.interact.proxy.CommonProxy;
import shadows.interact.util.EntityIDMessage;

public class ItemInteract extends Item {

	public ItemInteract(String name) {
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
		if(!stack.hasTagCompound()) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		UUID entityID = UUID.fromString(stack.getTagCompound().getString("entityID"));
		if (!world.isRemote) {
			EnumHand opposite = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
			Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(entityID);
			if(entity == null) return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			CommonProxy.INSTANCE.sendTo(new EntityIDMessage(entity.getEntityId(), 1, hand.ordinal()), (EntityPlayerMP) player);
			if(entity instanceof EntityLivingBase && player instanceof EntityPlayer) player.getHeldItem(opposite).getItem().itemInteractionForEntity(player.getHeldItem(opposite), (EntityPlayer) player, (EntityLivingBase) entity, opposite);
			entity.processInitialInteract(player, opposite);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		mapStack(entity.getPersistentID(), player.world, stack, player);
		return true;
	}

	private ItemStack mapStack(UUID entityID, World world, ItemStack stack, EntityPlayer player) {
		if (!world.isRemote) {
			Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(entityID);
			CommonProxy.INSTANCE.sendTo(new EntityIDMessage(entity.getEntityId(), 0, 0), (EntityPlayerMP) player);
			stack.setStackDisplayName("Interact With " + entity.getName());

			NBTTagString tag = new NBTTagString(entityID.toString());
			stack.setTagInfo("entityID", tag);
		}
		return stack;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, shadows.interact.proxy.ClientProxy.INTERACTION_MRL);
	}
}
