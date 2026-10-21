package twilightforest.compat.thaumcraft;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import twilightforest.TwilightForestMod;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Thaumcraft aspects for Twilight Forest blocks that Thaumcraft can't work out by itself.
 * Blocks in vanilla tags (logs, planks, leaves...) and blocks with crafting recipes get their aspects from Thaumcraft directly.
 * Thaumcraft is an optional dependency, so its API is reached through reflection.
 */
public final class ThaumcraftCompat {
	private static final Map<String, String> ASPECTS = new LinkedHashMap<>();

	private static void put(String aspects, String... blocks) {
		for (String block : blocks)
			ASPECTS.put(block, aspects);
	}

	static {
		// boss trophies
		put("BEAST:4,DEATH:3,MAGIC:4,GREED:2", "hydra_trophy", "naga_trophy", "minoshroom_trophy");
		put("UNDEAD:4,MAGIC:6,DEATH:3,GREED:2", "lich_trophy", "knight_phantom_trophy");
		put("COLD:6,MAGIC:4,DEATH:3,GREED:2", "snow_queen_trophy", "alpha_yeti_trophy");
		put("FIRE:3,UNDEAD:4,MAGIC:4,DEATH:3,GREED:2", "ur_ghast_trophy");
		put("BEAST:3,CLOTH:3,MAGIC:2,GREED:2", "quest_ram_trophy");
		put("EARTH:4,ORDER:3,MAGIC:2", "trophy_pedestal");

		// boss spawners
		put("MAGIC:8,BEAST:6,ELDRITCH:2,TRAP:2", "hydra_boss_spawner", "naga_boss_spawner", "minoshroom_boss_spawner");
		put("MAGIC:8,UNDEAD:6,ELDRITCH:2,TRAP:2", "lich_boss_spawner", "knight_phantom_boss_spawner", "ur_ghast_boss_spawner");
		put("MAGIC:8,COLD:6,ELDRITCH:2,TRAP:2", "snow_queen_boss_spawner", "alpha_yeti_boss_spawner");
		put("MAGIC:10,ELDRITCH:6,DARKNESS:4,TRAP:2", "final_boss_boss_spawner");
		put("MAGIC:6,DEATH:4,DARKNESS:3,TRAP:2", "sinister_spawner");

		// miniature structures
		put("MAGIC:4,TRAVEL:3,CRAFT:3,GREED:2", "twilight_portal_miniature_structure");
		put("MAGIC:4,UNDEAD:2,CRAFT:3,GREED:2", "lich_tower_miniature_structure");
		put("MAGIC:4,EARTH:2,CRAFT:3,GREED:2", "naga_courtyard_miniature_structure", "minotaur_labyrinth_miniature_structure");
		put("MAGIC:4,DARKNESS:2,CRAFT:3,GREED:2", "dark_tower_miniature_structure");

		// portal, force fields, vanishing and built blocks
		put("MAGIC:6,TRAVEL:6,ELDRITCH:2", "twilight_portal");
		put("MAGIC:3,ORDER:3,ENERGY:2", "blue_force_field", "green_force_field", "orange_force_field", "pink_force_field", "violet_force_field");
		put("MAGIC:3,VOID:3,ORDER:2", "locked_vanishing_block", "unbreakable_vanishing_block");
		put("MAGIC:2,ORDER:2,TREE:1", "built_block");
		put("MAGIC:2,ENTROPY:2,TREE:1", "antibuilt_block");
		put("MAGIC:4,ENTROPY:4,MECHANISM:3", "antibuilder");
		put("MAGIC:3,ENERGY:3,ENTROPY:2", "reactor_debris");
		put("MAGIC:3,MECHANISM:3,MOTION:2", "slider");
		put("FIRE:4,MECHANISM:2,TRAP:2", "fire_jet");
		put("FIRE:4,MAGIC:3,DARKNESS:2", "ominous_fire");
		put("MAGIC:4,TRAP:4,UNDEAD:2", "ghast_trap");
		put("METAL:4,ORDER:3,ARMOR:2", "stronghold_shield");
		put("CLOTH:2,MAGIC:2,SENSES:1", "red_thread");

		// castle
		put("EARTH:3,ORDER:2,MAGIC:1", "castle_brick");
		put("EARTH:2,ORDER:2,MAGIC:3", "blue_castle_rune_brick", "pink_castle_rune_brick", "violet_castle_rune_brick", "yellow_castle_rune_brick");
		put("MAGIC:4,ORDER:3,MECHANISM:2", "blue_castle_door", "pink_castle_door", "violet_castle_door", "yellow_castle_door");

		// stones
		put("EARTH:4,ENTROPY:1", "deadrock", "cracked_deadrock", "weathered_deadrock");
		put("EARTH:3,BEAST:2,ORDER:1", "nagastone", "etched_nagastone", "nagastone_pillar");
		put("EARTH:3,BEAST:3,SENSES:1", "nagastone_head");
		put("EARTH:3,ORDER:2,MAGIC:1", "mazestone");
		put("EARTH:3,ORDER:1", "underbrick");
		put("EARTH:6,ENTROPY:2", "giant_cobblestone");
		put("EARTH:6,FIRE:3,DARKNESS:3", "giant_obsidian");
		put("EARTH:4,COLD:2,ORDER:1", "trollsteinn");
		put("EARTH:2,MAGIC:2,LIFE:2", "uberous_soil");
		put("CRYSTAL:2,GREED:1,ELDRITCH:1", "fake_diamond");
		put("METAL:2,GREED:1,ELDRITCH:1", "fake_gold");

		// aurora and clouds
		put("COLD:3,LIGHT:2,CRYSTAL:1", "aurora_block");
		put("COLD:2,LIGHT:2,CRYSTAL:2", "auroralized_glass");
		put("AIR:3,WEATHER:2", "fluffy_cloud");
		put("AIR:2,WEATHER:1", "wispy_cloud");

		// trees
		put("TREE:4", "twilight_oak_log", "canopy_log", "mangrove_log", "dark_log",
			"stripped_twilight_oak_log", "stripped_canopy_log", "stripped_mangrove_log", "stripped_dark_log");
		put("TREE:4,MAGIC:1", "time_log", "transformation_log", "mining_log", "sorting_log",
			"stripped_time_log", "stripped_transformation_log", "stripped_mining_log", "stripped_sorting_log");
		put("TREE:3,FIRE:2,ENTROPY:1", "cinder_log", "cinder_wood");
		put("TREE:8,EARTH:2", "giant_log");
		put("PLANT:4,TREE:2", "giant_leaves");
		put("PLANT:1,DARKNESS:1,EARTH:1", "hardened_dark_leaves");
		put("TREE:3,MAGIC:4,TRAVEL:4", "time_log_core");
		put("TREE:3,MAGIC:4,EXCHANGE:4", "transformation_log_core");
		put("TREE:3,MAGIC:4,MINE:4", "mining_log_core");
		put("TREE:3,MAGIC:4,ORDER:4", "sorting_log_core");
		put("TREE:3,LIFE:2,DARKNESS:1", "infested_towerwood");
		put("TREE:2,EARTH:1", "root", "mangrove_root");
		put("TREE:2,LIFE:2,SENSES:1", "liveroot_block");
		put("PLANT:1,TREE:1", "root_strand");
		put("PLANT:3,TREE:1", "hedge");
		put("PLANT:3,TRAP:2,WEAPON:1", "brown_thorns", "green_thorns");
		put("PLANT:2,FIRE:1,TRAP:1", "burnt_thorns");
		put("PLANT:6,LIFE:2,WATER:1", "huge_stalk");

		// small plants and bushes
		put("PLANT:1,AIR:1", "fallen_leaves");
		put("PLANT:1", "clover_patch", "moss_patch", "fiddlehead", "mayapple");
		put("PLANT:3,WATER:2", "huge_lily_pad", "huge_water_lily");
		put("PLANT:2,LIGHT:2,FIRE:1", "torchberry_plant");
		put("PLANT:2,CROP:2,LIFE:1", "blackberry_bush", "blueberry_bush", "raspberry_bush", "maloberry_bush");
		put("PLANT:2,CROP:2,ENTROPY:1", "blightberry_bush");
		put("PLANT:2,CROP:2,DARKNESS:1", "duskberry_bush");
		put("PLANT:2,CROP:2,AIR:1", "skyberry_bush");
		put("PLANT:2,CROP:2,WEAPON:1", "stingberry_bush");
		put("PLANT:2,METAL:2,MINE:1", "iron_oreberry_bush");
		put("PLANT:2,METAL:2,GREED:1", "gold_oreberry_bush");
		put("PLANT:2,METAL:2,EXCHANGE:1", "copper_oreberry_bush");
		put("PLANT:2,MAGIC:2,SOUL:1", "essence_oreberry_bush");
		put("PLANT:3,LIGHT:2,EARTH:1", "trollber");
		put("PLANT:2,EARTH:1", "unripe_trollber");
		put("PLANT:3,EARTH:2", "trollvidr");

		// mushrooms
		put("PLANT:1,DARKNESS:1,LIGHT:1", "mushgloom");
		put("PLANT:3,DARKNESS:1,LIGHT:2", "huge_mushgloom", "huge_mushgloom_stem");

		// creatures
		put("BEAST:1,LIGHT:2,AIR:1", "firefly");
		put("BEAST:1,SENSES:2", "cicada");
		put("BEAST:1,LIGHT:2,EARTH:1", "moonworm");

		// candles and skull candles
		put("LIGHT:2,FIRE:1,DARKNESS:1,MAGIC:1", "ominous_candle", "ominous_white_candle", "ominous_orange_candle", "ominous_magenta_candle",
			"ominous_light_blue_candle", "ominous_yellow_candle", "ominous_lime_candle", "ominous_pink_candle", "ominous_gray_candle",
			"ominous_light_gray_candle", "ominous_cyan_candle", "ominous_purple_candle", "ominous_blue_candle", "ominous_brown_candle",
			"ominous_green_candle", "ominous_red_candle", "ominous_black_candle");
		put("DEATH:2,UNDEAD:2,LIGHT:2,FIRE:1", "skeleton_skull_candle", "wither_skeleton_skull_candle", "zombie_skull_candle");
		put("DEATH:2,MAN:2,LIGHT:2,FIRE:1", "player_skull_candle");
		put("DEATH:2,ENTROPY:2,LIGHT:2,FIRE:1", "creeper_skull_candle");
		put("DEATH:2,GREED:2,LIGHT:2,FIRE:1", "piglin_skull_candle");

		// containers and utility blocks
		put("TREE:2,DEATH:2,GREED:2,MAGIC:1", "keepsake_casket");
		put("TREE:2,DEATH:2,GREED:2,ENTROPY:1", "chipped_keepsake_casket", "damaged_keepsake_casket");
		put("TREE:3,DEATH:2,GREED:1", "skull_chest");
		put("CLOTH:3,GREED:2,ORDER:1", "coronation_carpet");
		put("METAL:3,FIRE:4,CRAFT:2", "brazier");
		put("FIRE:3,EARTH:2,CRAFT:2", "smoker");
		put("FIRE:4,EXCHANGE:3,MAGIC:2,CRAFT:2", "cinder_furnace");
		put("LIFE:4,MAGIC:2,HUNGER:3", "experiment_115");
	}

	private ThaumcraftCompat() {
	}

	public static void init() {
		try {
			Class<?> aspectSync = Class.forName("thaumcraft.aspect.AspectSync");
			Method addRegistrar = aspectSync.getMethod("addRegistrar", Consumer.class);
			addRegistrar.invoke(null, (Consumer<Object>) ThaumcraftCompat::registerAspects);
		} catch (ReflectiveOperationException e) {
			TwilightForestMod.LOGGER.error("Could not add Twilight Forest aspects to Thaumcraft", e);
		}
	}

	private static void registerAspects(Object registry) {
		try {
			Class<?> aspectClass = Class.forName("thaumcraft.aspect.Aspect");
			Class<?> listClass = Class.forName("thaumcraft.aspect.AspectList");
			Method add = listClass.getMethod("add", aspectClass, int.class);
			Method register = registry.getClass().getMethod("register", Item.class, listClass);
			for (Map.Entry<String, String> entry : ASPECTS.entrySet()) {
				Item item = BuiltInRegistries.ITEM.getValue(TwilightForestMod.prefix(entry.getKey()));
				if (item == Items.AIR)
					continue;
				Object aspects = listClass.getConstructor().newInstance();
				for (String part : entry.getValue().split(",")) {
					String[] aspect = part.split(":");
					add.invoke(aspects, aspectClass.getField(aspect[0]).get(null), Integer.parseInt(aspect[1]));
				}
				register.invoke(registry, item, aspects);
			}
		} catch (ReflectiveOperationException e) {
			TwilightForestMod.LOGGER.error("Could not add Twilight Forest aspects to Thaumcraft", e);
		}
	}
}
