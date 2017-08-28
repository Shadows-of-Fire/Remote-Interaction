package shadows.interact.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EntityIDMessage implements IMessage {
	// A default constructor is always required
	public EntityIDMessage() {
	}

	private int entityID;
	private int ver;
	private int hand;
	private EnumHand curHand;

	public EntityIDMessage(int id, int version, int handOrdinal) {
		entityID = id;
		ver = version;
		hand = handOrdinal;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(ver);
		buf.writeInt(hand);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		ver = buf.readInt();
		curHand = EnumHand.values()[buf.readInt()];
	}

	public static class IDHandler implements IMessageHandler<EntityIDMessage, IMessage> {
		public IDHandler() {
		};

		@Override
		public IMessage onMessage(EntityIDMessage message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			EnumHand opposite = message.curHand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
			mc.addScheduledTask(message.ver == 0 ? new Runnable() {
				@Override
				public void run() {
					EntityPlayerSP player = mc.player;
					World world = mc.world;
					Entity entity = world.getEntityByID(message.entityID);
					ItemStack stack = player.getHeldItemMainhand();
					stack.setStackDisplayName("Interact With " + entity.getName());

					NBTTagString tag = new NBTTagString(entity.getPersistentID().toString());
					stack.setTagInfo("entityID", tag);
				}
			} : new Runnable() {
				@Override
				public void run() {
					EntityPlayerSP player = mc.player;
					World world = mc.world;
					Entity entity = world.getEntityByID(message.entityID);
					if (entity instanceof EntityLivingBase && player instanceof EntityPlayer)
						player.getHeldItem(opposite).getItem().itemInteractionForEntity(player.getHeldItem(opposite), (EntityPlayer) player, (EntityLivingBase) entity, opposite);
					entity.processInitialInteract(player, opposite);
				}
			});
			return null;
		}
	}

}
