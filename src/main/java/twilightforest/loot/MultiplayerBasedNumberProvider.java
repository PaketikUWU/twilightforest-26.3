package twilightforest.loot;

import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

import java.util.Set;

// 26.3: registered as both an int and a float provider type, so it can be used for pool rolls and nested inside other TF providers like before
public record MultiplayerBasedNumberProvider(Holder<ContextFloatProvider> rollsPerPlayer, Holder<ContextFloatProvider> defaultRolls) implements ContextIntProvider, ContextFloatProvider, LootContextUser {
	public static final MapCodec<MultiplayerBasedNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextFloatProviders.CODEC.fieldOf("per_player_rolls").forGetter(MultiplayerBasedNumberProvider::rollsPerPlayer),
			ContextFloatProviders.CODEC.fieldOf("default_rolls").forGetter(MultiplayerBasedNumberProvider::defaultRolls))
		.apply(instance, MultiplayerBasedNumberProvider::new)
	);

	@Override
	public MapCodec<MultiplayerBasedNumberProvider> codec() {
		return CODEC;
	}

	public static MultiplayerBasedNumberProvider rollsForPlayers(Holder<ContextFloatProvider> rollsPerPlayer, Holder<ContextFloatProvider> defaultRolls) {
		return new MultiplayerBasedNumberProvider(rollsPerPlayer, defaultRolls);
	}

	@Override
	public float getFloatUnsafe(LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (context.hasParameter(LootContextParams.THIS_ENTITY) && context.getOptional(LootContextParams.THIS_ENTITY).hasData(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = context.getOptional(LootContextParams.THIS_ENTITY).getData(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				float total = this.defaultRolls.value().getFloat(context);
				for (int i = 0; i < qualifiedPlayers - 1; i++) {
					total += Math.max(0, this.rollsPerPlayer.value().getFloat(context));
				}
				return total;
			}
		}
		return this.defaultRolls.value().getFloat(context);

	}

	@Override
	public int getIntUnsafe(LootContext context) {
		return Math.round(this.getFloatUnsafe(context));
	}

	/**
	 * Get the parameters used by this object.
	 */
	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Sets.union(referencedParams(this.rollsPerPlayer), referencedParams(this.defaultRolls));
	}

	@Override
	public void validate(ValidationContext context) {
		LootContextUser.super.validate(context);
		Validatable.validateHolder(context, "per_player_rolls", this.rollsPerPlayer);
		Validatable.validateHolder(context, "default_rolls", this.defaultRolls);
	}

	static Set<ContextKey<?>> referencedParams(Holder<? extends Validatable> holder) {
		return holder.value() instanceof LootContextUser user ? user.getReferencedContextParams() : Set.of();
	}
}
