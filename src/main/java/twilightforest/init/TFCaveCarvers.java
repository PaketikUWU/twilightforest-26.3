package twilightforest.init;

import twilightforest.world.components.CarverWallProvider;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.NoiseCarverWallProvider;
import twilightforest.world.components.TFCavesCarver;

import java.util.List;

//this was all put into 1 class because it seems like a waste to have it in 2
public class TFCaveCarvers {
	public static final DeferredRegister<MapCodec<? extends WorldCarver>> CARVER_TYPES = DeferredRegister.create(Registries.CARVER_TYPE, TwilightForestMod.ID);

	private static final CarverWallProvider TF_CAVES_WALLS = new NoiseCarverWallProvider(
		6972119253061020355L,
		NormalNoise.createParity(0, 1.0),
		0.5f,
		List.of(
			Blocks.DIRT.defaultBlockState(),
			Blocks.DIRT.defaultBlockState(),
			Blocks.ROOTED_DIRT.defaultBlockState(),
			Blocks.DIRT.defaultBlockState(),
			Blocks.DIRT.defaultBlockState(),
			Blocks.COARSE_DIRT.defaultBlockState(),
			Blocks.DIRT.defaultBlockState(),
			Blocks.DIRT.defaultBlockState()
		)
	);
	private static final Supplier<WeightedList<BlockState>> HIGHLAND_WALLS = Suppliers.memoize(() -> WeightedList.<BlockState>builder()
		.add(TFBlocks.TROLLSTEINN.value().defaultBlockState(), 1)
		.add(Blocks.STONE.defaultBlockState(), 3)
		.build());
	private static final CarverWallProvider HIGHLAND_CAVES_WALLS = (random, _) -> HIGHLAND_WALLS.get().getRandomOrThrow(random);

	public static final DeferredHolder<MapCodec<? extends WorldCarver>, MapCodec<TFCavesCarver>> TF_CAVES = CARVER_TYPES.register("tf_caves", () -> TFCavesCarver.codec(TFCaveCarvers.TF_CAVES, false, TF_CAVES_WALLS));
	public static final DeferredHolder<MapCodec<? extends WorldCarver>, MapCodec<TFCavesCarver>> HIGHLAND_CAVES = CARVER_TYPES.register("highland_caves", () -> TFCavesCarver.codec(TFCaveCarvers.HIGHLAND_CAVES, true, HIGHLAND_CAVES_WALLS));

	public static TFCavesCarver tfCaves(TFCavesCarver.Configuration config) {
		return new TFCavesCarver(config, TF_CAVES, false, TF_CAVES_WALLS);
	}

	public static TFCavesCarver highlandCaves(TFCavesCarver.Configuration config) {
		return new TFCavesCarver(config, HIGHLAND_CAVES, true, HIGHLAND_CAVES_WALLS);
	}

	public static final ResourceKey<WorldCarver> TFCAVES_CONFIGURED = registerKey("tf_caves");
	public static final ResourceKey<WorldCarver> HIGHLANDCAVES_CONFIGURED = registerKey("highland_caves");

	private static ResourceKey<WorldCarver> registerKey(String name) {
		return ResourceKey.create(Registries.CARVER, TwilightForestMod.prefix(name));
	}

}
