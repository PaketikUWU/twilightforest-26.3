package twilightforest.world.registration;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import twilightforest.block.FireflyBlock;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;
import twilightforest.world.components.feature.trees.treeplacers.*;

import java.util.OptionalInt;

public class TreeConfigurations {

	private static final int canopyDistancing = 5;

	public static final Holder<BlockStateProvider> PLACE_BELOW_OVERWORLD_TRUNKS = Holder.direct(RuleBasedStateProvider.ifTrueThenProvide(
		BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK)), Blocks.DIRT
	));

	// 26.1's TreeConfigurationBuilder defaulted the below trunk provider, 26.3's TreeFeature.Builder takes it explicitly
	private static TreeFeature.Builder builder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, FeatureSize minimumSize) {
		return new TreeFeature.Builder(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, minimumSize, PLACE_BELOW_OVERWORLD_TRUNKS);
	}

	static WeightedList.Builder<BlockState> createBlockList() {
		return WeightedList.builder();
	}

	public static final TreeFeature TWILIGHT_OAK = builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new StraightTrunkPlacer(4, 2, 0),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
		new TwoLayersFeatureSize(1, 0, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.build();

	public static final TreeFeature LARGE_TWILIGHT_OAK = builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new FancyTrunkPlacer(3, 11, 0),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
		new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4))
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.build();

	public static final TreeFeature SWAMPY_OAK = builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new StraightTrunkPlacer(4, 2, 0),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
		new TwoLayersFeatureSize(1, 0, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS, new LeaveVineDecorator(0.125F)))
		.build();

	private final static int LEAF_SHAG_FACTOR = 24;
	public static final TreeFeature CANOPY_TREE = builder(
		BlockStateProvider.of(TFBlocks.CANOPY_LOG.get()),
		new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.of(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
		BlockStateProvider.of(TFBlocks.CANOPY_LEAVES.get()),
		new LeafSpheroidFoliagePlacer((float) Math.sqrt(Math.pow(4, 2) + Math.pow(1, 2)), 1.5f, ConstantInt.of(0), 0, 0, -0.2f, LEAF_SHAG_FACTOR),
		new TwoLayersFeatureSize(20, 0, canopyDistancing)
	)
		.decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TreeFeature CANOPY_TREE_FIREFLY = builder(
		BlockStateProvider.of(TFBlocks.CANOPY_LOG.get()),
		new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.of(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
		BlockStateProvider.of(TFBlocks.CANOPY_LEAVES.get()),
		new LeafSpheroidFoliagePlacer((float) Math.sqrt(Math.pow(4, 2) + Math.pow(1, 2)), 1.5f, ConstantInt.of(0), 0, 0, -0.2f, LEAF_SHAG_FACTOR),
		new TwoLayersFeatureSize(20, 1, canopyDistancing)
	)
		.decorators(ImmutableList.of(
			TreeDecorators.LIVING_ROOTS,
			TreeDecorators.FIREFLY,
			new TrunkSideDecorator( // A few more Fireflies!
				4,
				0.5f,
				BlockStateProvider.of(TFBlocks.FIREFLY.get().defaultBlockState().setValue(FireflyBlock.FACING, Direction.NORTH))
			),
			new DangleFromTreeDecorator(
				1,
				1,
				2,
				5,
				15,
				new WeightedStateProvider(createBlockList()
					.add(TFBlocks.ROPE.get().defaultBlockState(), 3)
					.add(Blocks.IRON_CHAIN.defaultBlockState(), 1).build()),
				new WeightedStateProvider(createBlockList()
					.add(TFBlocks.FIREFLY_JAR.get().defaultBlockState(), 10)
					.add(TFBlocks.CICADA_JAR.get().defaultBlockState(), 1).build())
			)
		))
		.ignoreVines()
		.build();

	public static final TreeFeature CANOPY_TREE_DEAD = builder(
		BlockStateProvider.of(TFBlocks.CANOPY_LOG.get()),
		new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.of(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
		BlockStateProvider.of(Blocks.AIR),
		LeafSpheroidFoliagePlacer.NO_OP,
		new TwoLayersFeatureSize(20, 0, canopyDistancing)
	)
		.decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TFTreeFeatureConfig MEGA_CANOPY = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(TFBlocks.CANOPY_LOG.get()),
		BlockStateProvider.of(TFBlocks.CANOPY_LEAVES.get()),
		BlockStateProvider.of(TFBlocks.CANOPY_WOOD.get()),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.minHeight(30)
		.chanceFirstFive(2)
		.chanceSecondFive(3)
		.build();

	public static final TreeFeature MANGROVE_TREE = builder(
		BlockStateProvider.of(TFBlocks.MANGROVE_LOG.get()),
		new TrunkRiser(4, new BranchingTrunkPlacer(7, 4, 0, 6, new BranchesConfig(BlockStateProvider.of(TFBlocks.MANGROVE_WOOD.get()), 0, 3, 6, 2, 0.3, 0.25), false, false)),
		BlockStateProvider.of(TFBlocks.MANGROVE_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(2.5f, 1.5f, ConstantInt.of(0), 2, 0, -0.25f, (int) (LEAF_SHAG_FACTOR * 0.666f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(
				TreeDecorators.FIREFLY,
				new TreeRootsDecorator(3, 1, 12, 0, BlockStateProvider.of(TFBlocks.MANGROVE_ROOT.get().defaultBlockState()), (new WeightedStateProvider(WeightedList.<BlockState>builder()
					.add(TFBlocks.ROOT_BLOCK.get().defaultBlockState(), 4)
					.add(TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState(), 1).build())), 1),
				new LeaveVineDecorator(0.125F)
			)
		)
		.build();

	public static final TreeFeature DARKWOOD_TREE = builder(
		BlockStateProvider.of(TFBlocks.DARK_LOG.get()),
		new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.of(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
		BlockStateProvider.of(TFBlocks.HARDENED_DARK_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TreeFeature HOMEGROWN_DARKWOOD_TREE = builder(
		BlockStateProvider.of(TFBlocks.DARK_LOG.get()),
		new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.of(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
		BlockStateProvider.of(TFBlocks.DARK_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TreeFeature DARKWOOD_LANTERN_TREE = builder(
		BlockStateProvider.of(TFBlocks.DARK_LOG.get()),
		new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.of(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
		BlockStateProvider.of(TFBlocks.DARK_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(
			TreeDecorators.LIVING_ROOTS,
			new DangleFromTreeDecorator(
				0,
				1,
				2,
				4,
				2,
				new WeightedStateProvider(createBlockList().add(Blocks.IRON_CHAIN.defaultBlockState(), 1).build()),
				new WeightedStateProvider(createBlockList().add(Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), 1).build())
			)
		))
		.ignoreVines()
		.build();

	public static final TreeFeature SMALL_JUNGLE = builder(
		BlockStateProvider.of(Blocks.JUNGLE_LOG),
		new StraightTrunkPlacer(3, 2, 2),
		BlockStateProvider.of(Blocks.JUNGLE_LEAVES),
		new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
		new TwoLayersFeatureSize(1, 0, 1)
	)
		.decorators(ImmutableList.of(
			new CocoaDecorator(0.7F),
			TrunkVineDecorator.INSTANCE,
			new LeaveVineDecorator(0.25F)))
		.ignoreVines()
		.build();

	//[VanillaCopy] of JUNGLE_BUSH
	public static final TreeFeature OAK_BUSH = builder(
		BlockStateProvider.of(Blocks.OAK_LOG),
		new StraightTrunkPlacer(1, 0, 0),
		BlockStateProvider.of(Blocks.OAK_LEAVES),
		new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
		new TwoLayersFeatureSize(0, 0, 0))
		.ignoreVines()
		.build();

	//[VanillaCopy] of Features.MEGA_SPRUCE, just without all the nasty podzol so it fits better
	public static final TreeFeature BIG_SPRUCE = builder(
		BlockStateProvider.of(Blocks.SPRUCE_LOG),
		new GiantTrunkPlacer(13, 2, 14),
		BlockStateProvider.of(Blocks.SPRUCE_LEAVES),
		new MegaPineFoliagePlacer(ConstantInt.of(0),
			ConstantInt.of(0),
			UniformInt.of(13, 17)),
		new TwoLayersFeatureSize(4, 1, 2))
		.build();

	// Requires Hollowtree gen
	public static final TFTreeFeatureConfig TIME_TREE = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(TFBlocks.TIME_LOG.get()),
		BlockStateProvider.of(TFBlocks.TIME_LEAVES.get()),
		BlockStateProvider.of(TFBlocks.TIME_WOOD.get()),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.build();

	public static final TreeFeature TRANSFORM_TREE = builder(
		BlockStateProvider.of(TFBlocks.TRANSFORMATION_LOG.get()),
		new BranchingTrunkPlacer(6, 5, 5, 7, new BranchesConfig(BlockStateProvider.of(TFBlocks.TRANSFORMATION_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, false),
		BlockStateProvider.of(TFBlocks.TRANSFORMATION_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantInt.of(0), 1, 0, -0.25f, 0),
		new TwoLayersFeatureSize(4, 1, canopyDistancing)
	)
		.ignoreVines()
		.decorators(ImmutableList.of(new TreeCorePlacer(3, BlockStateProvider.of(TFBlocks.TRANSFORMATION_LOG_CORE.get().defaultBlockState()))))
		.build();

	public static final TFTreeFeatureConfig MINING_TREE = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(TFBlocks.MINING_LOG.get()),
		BlockStateProvider.of(TFBlocks.MINING_LEAVES.get()),
		BlockStateProvider.of(TFBlocks.MINING_WOOD.get()),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.build();

	public static final TreeFeature SORT_TREE = builder(
		BlockStateProvider.of(TFBlocks.SORTING_LOG.get()),
		new StraightTrunkPlacer(5, 0, 0),
		BlockStateProvider.of(TFBlocks.SORTING_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(1.5F, 2.5F, ConstantInt.of(-2), 0, 0, 0.5F, 0),
		new TwoLayersFeatureSize(1, 1, 1)
	)
		.ignoreVines()
		.belowTrunkProvider(PLACE_BELOW_OVERWORLD_TRUNKS)
		.decorators(ImmutableList.of(new TreeCorePlacer(2, BlockStateProvider.of(TFBlocks.SORTING_LOG_CORE.get().defaultBlockState()))))
		.build();

	public static final TFTreeFeatureConfig LARGE_WINTER = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(Blocks.SPRUCE_LOG),
		BlockStateProvider.of(Blocks.SPRUCE_LEAVES),
		BlockStateProvider.of(Blocks.SPRUCE_LOG),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.build();

	public static final TFTreeFeatureConfig FOREST_MEGA_OAK = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_WOOD.get()),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.minHeight(24)
		.build();

	public static final TFTreeFeatureConfig SAVANNAH_MEGA_OAK = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_WOOD.get()),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.minHeight(16)
		.build();

	public static final TFTreeFeatureConfig HOLLOW_TREE = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_WOOD.get()),
		BlockStateProvider.of(TFBlocks.ROOT_BLOCK.get())
	)
		.build();

	public static final TreeFeature RAINBOAK_TREE = builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new StraightTrunkPlacer(4, 2, 0),
		BlockStateProvider.of(TFBlocks.RAINBOW_OAK_LEAVES.get()),
		new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
		new TwoLayersFeatureSize(1, 1, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.build();

	public static final TreeFeature LARGE_RAINBOAK_TREE = builder(
		BlockStateProvider.of(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new FancyTrunkPlacer(3, 11, 0),
		BlockStateProvider.of(TFBlocks.RAINBOW_OAK_LEAVES.get()),
		new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
		new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4))
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.build();
}
