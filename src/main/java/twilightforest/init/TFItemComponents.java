package twilightforest.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import twilightforest.TwilightForestMod;
import twilightforest.block.BanisterBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * 26.3 moved composting and furnace fuel from NeoForge data maps to item components; this keeps the values of TF's old data maps.
 */
public class TFItemComponents {
	private static final Map<String, ResourceKey<ContextIntProvider>> COMPOSTABLES = new HashMap<>();

	static {
		COMPOSTABLES.put("beanstalk_leaves", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("blackberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("blackberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("blightberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("blightberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("blueberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("blueberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("canopy_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("canopy_sapling", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("clover_patch", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("dark_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("darkwood_sapling", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("duskberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("duskberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("experiment_115", ContextIntProviders.COMPOSTABLE_MEDIUM_HIGH);
		COMPOSTABLES.put("fallen_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("fiddlehead", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("hedge", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("hollow_oak_sapling", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("huge_lily_pad", ContextIntProviders.COMPOSTABLE_MEDIUM_HIGH);
		COMPOSTABLES.put("huge_mushgloom", ContextIntProviders.COMPOSTABLE_MEDIUM_HIGH);
		COMPOSTABLES.put("huge_mushgloom_stem", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("huge_stalk", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("huge_water_lily", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("liveroot", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("liveroot_block", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("magic_beans", ContextIntProviders.COMPOSTABLE_MEDIUM_HIGH);
		COMPOSTABLES.put("maloberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("maloberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("mangrove_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("mangrove_sapling", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("mayapple", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("maze_wafer", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("mining_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("mining_sapling", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("moss_patch", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("mushgloom", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("rainbow_oak_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("rainbow_oak_sapling", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("raspberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("raspberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("root", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("root_strand", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("skyberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("skyberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("sorting_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("sorting_sapling", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("stingberry", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("stingberry_bush", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("thorn_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("thorn_rose", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("time_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("time_sapling", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("torchberries", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("torchberry_plant", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("transformation_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("transformation_sapling", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("trollber", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("trollvidr", ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
		COMPOSTABLES.put("twilight_oak_leaves", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("twilight_oak_sapling", ContextIntProviders.COMPOSTABLE_LOW);
		COMPOSTABLES.put("uberous_soil", ContextIntProviders.COMPOSTABLE_MEDIUM);
		COMPOSTABLES.put("unripe_trollber", ContextIntProviders.COMPOSTABLE_MEDIUM);
	}

	public static void modifyComponents(ModifyDefaultComponentsEvent event) {
		for (Item item : BuiltInRegistries.ITEM) {
			Identifier id = BuiltInRegistries.ITEM.getKey(item);
			if (!TwilightForestMod.ID.equals(id.getNamespace()))
				continue;
			ResourceKey<ContextIntProvider> compostable = COMPOSTABLES.get(id.getPath());
			if (compostable != null)
				event.modify(item, (components, context, key) -> components.set(DataComponents.COMPOSTABLE, new Compostable(compostable)));
			if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BanisterBlock)
				event.modify(item, (components, context, key) -> components.set(DataComponents.COOKING_FUEL, new CookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS, ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER)));
		}
	}
}
