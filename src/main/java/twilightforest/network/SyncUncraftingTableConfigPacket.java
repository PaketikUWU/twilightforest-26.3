package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.config.TFConfig;
import twilightforest.TwilightForestMod;

import java.util.List;

public record SyncUncraftingTableConfigPacket(
	double uncraftingMultiplier, double repairingMultiplier,
	boolean allowShapeless, boolean disableIngredientSwitching, boolean disabledUncrafting, boolean disabledTable,
	List<? extends String> disabledRecipes, boolean flipRecipeList,
	List<? extends String> disabledModids, boolean flipModidList) implements CustomPacketPayload {

	public static final Type<SyncUncraftingTableConfigPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_uncrafting_config"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncUncraftingTableConfigPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncUncraftingTableConfigPacket::write, SyncUncraftingTableConfigPacket::new);

	private static final StreamCodec<ByteBuf, List<String>> STRING_LIST = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list());

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(),
			buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
			STRING_LIST.decode(buf), buf.readBoolean(),
			STRING_LIST.decode(buf), buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier());
		buf.writeDouble(this.repairingMultiplier());
		buf.writeBoolean(this.allowShapeless());
		buf.writeBoolean(this.disableIngredientSwitching());
		buf.writeBoolean(this.disabledUncrafting());
		buf.writeBoolean(this.disabledTable());
		STRING_LIST.encode(buf, List.copyOf(this.disabledRecipes()));
		buf.writeBoolean(this.flipRecipeList());
		STRING_LIST.encode(buf, List.copyOf(this.disabledModids()));
		buf.writeBoolean(this.flipModidList());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncUncraftingTableConfigPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			TFConfig.uncraftingXpCostMultiplier = message.uncraftingMultiplier();
			TFConfig.repairingXpCostMultiplier = message.repairingMultiplier();
			TFConfig.allowShapelessUncrafting = message.allowShapeless();
			TFConfig.disableIngredientSwitching = message.disableIngredientSwitching();
			TFConfig.disableUncraftingOnly = message.disabledUncrafting();
			TFConfig.disableEntireTable = message.disabledTable();
			TFConfig.disableUncraftingRecipes = message.disabledRecipes();
			TFConfig.reverseRecipeBlacklist = message.flipRecipeList();
			TFConfig.blacklistedUncraftingModIds = message.disabledModids();
			TFConfig.flipUncraftingModIdList = message.flipModidList();
		});
	}
}
