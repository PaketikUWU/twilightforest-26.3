package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

import java.util.Set;

// 26.3: registered as both an int and a float provider type, matching the old single NumberProvider behaviour
public class LootingEnchantNumberProvider implements ContextIntProvider, ContextFloatProvider, LootContextUser {
	public static final MapCodec<LootingEnchantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Enchantment.CODEC.fieldOf("enchantment").forGetter(provider -> provider.enchantment),
			ContextFloatProviders.CODEC.fieldOf("base_value").forGetter(provider -> provider.baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	private final Holder<Enchantment> enchantment;
	private final Holder<ContextFloatProvider> baseValue;

	protected LootingEnchantNumberProvider(Holder<Enchantment> enchantment, Holder<ContextFloatProvider> baseValue) {
		this.enchantment = enchantment;
		this.baseValue = baseValue;
	}

	@Override
	public MapCodec<LootingEnchantNumberProvider> codec() {
		return CODEC;
	}

	public static LootingEnchantNumberProvider applyLootingLevelTo(HolderLookup.Provider provider, Holder<ContextFloatProvider> baseValue) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		return new LootingEnchantNumberProvider(registrylookup.getOrThrow(Enchantments.LOOTING), baseValue);
	}

	@Override
	public float getFloatUnsafe(LootContext context) {
		if (context.getOptional(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living) {
			return EnchantmentHelper.getEnchantmentLevel(this.enchantment, living) + this.baseValue.value().getFloat(context);
		}
		return this.baseValue.value().getFloat(context);
	}

	@Override
	public int getIntUnsafe(LootContext context) {
		return Math.round(this.getFloatUnsafe(context));
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return MultiplayerBasedNumberProvider.referencedParams(this.baseValue);
	}

	@Override
	public void validate(ValidationContext context) {
		LootContextUser.super.validate(context);
		Validatable.validateHolder(context, "base_value", this.baseValue);
	}
}
