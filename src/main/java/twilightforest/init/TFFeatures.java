package twilightforest.init;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.feature.legacy.*;
import twilightforest.world.components.feature.templates.*;
import twilightforest.world.components.feature.trees.*;
import twilightforest.world.components.feature.trollcave.TrollBigMushgloomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeBrownMushroomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeRedMushroomFeature;

import java.util.function.Supplier;

public class TFFeatures {

	public static final DeferredRegister<MapCodec<? extends net.minecraft.world.level.levelgen.feature.Feature>> FEATURES = DeferredRegister.create(Registries.FEATURE_TYPE, TwilightForestMod.ID);

	public static final Supplier<Feature<TFSmallLakeFeature.Configuration>> SMALL_LAKE = register("small_lake", () -> new TFSmallLakeFeature(TFSmallLakeFeature.Configuration.CODEC));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> BIG_MUSHGLOOM = register("big_mushgloom", () -> new BigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> CANOPY_BROWN_MUSHROOM = register("canopy_brown_mushroom", () -> new BrownCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_VANILLA_MUSHROOM = register("canopy_red_vanilla_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 0));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_SMOOTH_MUSHROOM = register("canopy_red_smooth_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 1));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_SPHEROID_MUSHROOM = register("canopy_red_spheroid_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 2));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_FLAT_MUSHROOM = register("canopy_red_flat_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 3));
	public static final Supplier<Feature<TFTreeFeatureConfig>> MEGA_OAK = register("mega_oak", () -> new MegaOakTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Supplier<Feature<TFTreeFeatureConfig>> MEGA_CANOPY = register("mega_canopy", () -> new MegaCanopyTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Supplier<Feature<NoneFeatureConfiguration>> CAVE_STALACTITE = register("block_spike", () -> new BlockSpikeFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<TreeFeature>> DARK_CANOPY_TREE = register("dark_canopy_tree", () -> new DarkCanopyTreeFeature(TreeFeature.CODEC.codec()));
	public static final Supplier<Feature<SwizzleConfig>> DRUID_HUT = register("druid_hut", () -> new DruidHutFeature(SwizzleConfig.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> FALLEN_HOLLOW_LOG = register("fallen_hollow_log", () -> new FallenHollowLogFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> FALLEN_LEAVES = register("fallen_leaves", () -> new FallenLeavesFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<HollowLogConfig>> FALLEN_SMALL_LOG = register("fallen_small_log", () -> new SmallFallenLogFeature(HollowLogConfig.CODEC));
	public static final Supplier<Feature<BlockStateConfiguration>> FIRE_JET = register("fire_jet", () -> new FireJetFeature(BlockStateConfiguration.CODEC));
	public static final Supplier<Feature<RuinedFoundationConfig>> FOUNDATION = register("foundation", () -> new FoundationFeature(RuinedFoundationConfig.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> GRAVEYARD = register("graveyard", () -> new GraveyardFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> GROVE_RUINS = register("grove_ruins", () -> new GroveRuinsFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<TFTreeFeatureConfig>> HOLLOW_STUMP = register("hollow_stump", () -> new HollowStumpFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Supplier<Feature<NoneFeatureConfiguration>> HUGE_LILY_PAD = register("huge_lily_pad", () -> new HugeLilypadFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> HUGE_WATER_LILY = register("huge_water_lily", () -> new HugeWaterLilyFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<BlockStateConfiguration>> LAMPPOSTS = register("lampposts", () -> new LampostFeature(BlockStateConfiguration.CODEC));
	public static final Supplier<Feature<TFTreeFeatureConfig>> LARGE_WINTER_TREE = register("large_winter_tree", () -> new LargeWinterTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Supplier<Feature<TFTreeFeatureConfig>> MINERS_TREE = register("miners_tree", () -> new MiningTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Supplier<Feature<NoneFeatureConfiguration>> MONOLITH = register("monolith", () -> new MonolithFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<DiskConfiguration>> MYCELIUM_BLOB = register("mycelium_blob", () -> new CheckAbovePatchFeature(DiskConfiguration.CODEC));
	public static final Supplier<Feature<BerryBushConfig>> BERRY_BUSH = register("berry_bush", () -> new BerryBushFeature(BerryBushConfig.CODEC));
	public static final Supplier<Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS = register("underground_plants", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC));
	public static final Supplier<Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS_IN_STRUCTURE = register("underground_plants_in_structure", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final Supplier<Feature<BlockStateConfiguration>> OREBERRY_BUSHES = register("oreberry_bushes", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC, 1));
	public static final Supplier<Feature<BlockStateConfiguration>> TROLL_VINES = register("troll_vines", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> TROLL_BIG_MUSHGLOOM = register("troll_cave_big_mushgloom", () -> new TrollBigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> TROLL_HUGE_RED_MUSHROOM = register("troll_cave_huge_red_mushroom", () -> new TrollHugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> TROLL_HUGE_BROWN_MUSHROOM = register("troll_cave_huge_brown_mushroom", () -> new TrollHugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> STONE_CIRCLE = register("stone_circle", () -> new StoneCircleFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<ThornsConfig>> THORNS = register("thorns", () -> new ThornFeature(ThornsConfig.CODEC));
	public static final Supplier<Feature<TFTreeFeatureConfig>> TREE_OF_TIME = register("tree_of_time", () -> new TimeTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final Supplier<Feature<NoneFeatureConfiguration>> WEBS = register("webs", () -> new WebFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<SwizzleConfig>> SIMPLE_WELL = register("simple_well", () -> new SimpleWellFeature(SwizzleConfig.CODEC));
	public static final Supplier<Feature<SwizzleConfig>> FANCY_WELL = register("fancy_well", () -> new FancyWellFeature(SwizzleConfig.CODEC));
	public static final Supplier<Feature<RootConfig>> WOOD_ROOTS = register("wood_roots", () -> new WoodRootFeature(RootConfig.CODEC));
	public static final Supplier<Feature<NoneFeatureConfiguration>> SNOW_UNDER_TREES = register("snow_under_trees", () -> new SnowUnderTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<TreeFeature>> SNOW_TREE = register("anywhere_tree", () -> new SnowTreeFeature(TreeFeature.CODEC.codec()));
	public static final Supplier<Feature<NoneFeatureConfiguration>> ENCHANTED_FOREST_VINES = register("enchanted_forest_vines", () -> new EnchantedForestVinesFeature(NoneFeatureConfiguration.CODEC));
	public static final Supplier<Feature<WeightedListFeatureConfig>> WEIGHTED_LIST_SELECTOR = register("weighted_list", () -> new WeightedListFeature(WeightedListFeatureConfig.CODEC));

	private static <FC, F extends Feature<FC>> Supplier<F> register(String name, Supplier<F> factory) {
		Supplier<F> feature = Suppliers.memoize(factory::get);
		FEATURES.register(name, () -> feature.get().configuredCodec());
		return feature;
	}
}
