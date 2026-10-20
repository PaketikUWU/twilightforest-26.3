package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

import java.util.function.Supplier;

public class TFStructurePlacementTypes {
	public static final DeferredRegister<MapCodec<? extends StructurePlacement>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends StructurePlacement>, MapCodec<LandmarkGridPlacement>> GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("landmark_grid", () -> LandmarkGridPlacement.CODEC);
	public static final DeferredHolder<MapCodec<? extends StructurePlacement>, MapCodec<AvoidLandmarkGridPlacement>> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("avoid_landmark_grid", () -> AvoidLandmarkGridPlacement.CODEC);

	private static <P extends StructurePlacement> DeferredHolder<MapCodec<? extends StructurePlacement>, MapCodec<P>> registerPlacer(String name, Supplier<MapCodec<P>> factory) {
		return STRUCTURE_PLACEMENT_TYPES.register(name, factory);
	}
}
