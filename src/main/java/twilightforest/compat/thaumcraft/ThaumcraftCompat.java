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
 * Thaumcraft aspects for Twilight Forest blocks and items.
 * Values are taken from Twilight Forest's own Thaumcraft integration for 1.12 where an item existed there.
 * Thaumcraft is an optional dependency, so its API is reached through reflection.
 */
public final class ThaumcraftCompat {
	private static final Map<String, String> ASPECTS = new LinkedHashMap<>();

	private static void put(String aspects, String... blocks) {
		for (String block : blocks)
			ASPECTS.put(block, aspects);
	}

	static {
		put("TREE:1,ORDER:1", "acacia_banister", "bamboo_banister", "birch_banister", "canopy_banister", "cherry_banister", "crimson_banister",
			"dark_banister", "dark_oak_banister", "jungle_banister", "mangrove_banister", "mining_banister", "oak_banister", "pale_oak_banister",
			"sorting_banister", "spruce_banister", "time_banister", "transformation_banister", "twilight_oak_banister", "vangrove_banister", "warped_banister");
		put("TREE:2,AIR:2", "acacia_drying_rack", "bamboo_drying_rack", "birch_drying_rack", "canopy_drying_rack", "cherry_drying_rack",
			"crimson_drying_rack", "dark_drying_rack", "dark_oak_drying_rack", "jungle_drying_rack", "mangrove_drying_rack", "mining_drying_rack",
			"oak_drying_rack", "pale_oak_drying_rack", "sorting_drying_rack", "spruce_drying_rack", "time_drying_rack", "transformation_drying_rack",
			"twilight_oak_drying_rack", "vangrove_drying_rack", "warped_drying_rack");
		put("CLOTH:2,SENSES:1,MAGIC:1", "alpha_yeti_banner_pattern", "hydra_banner_pattern", "knight_phantom_banner_pattern", "lich_banner_pattern",
			"minoshroom_banner_pattern", "naga_banner_pattern", "quest_ram_banner_pattern", "snow_queen_banner_pattern", "ur_ghast_banner_pattern");
		put("MAGIC:8,COLD:6,ELDRITCH:2,TRAP:2", "alpha_yeti_boss_spawner", "snow_queen_boss_spawner");
		put("BEAST:48,COLD:28", "alpha_yeti_fur");
		put("DEATH:45,SENSES:20,BEAST:60,COLD:60", "alpha_yeti_trophy");
		put("MAGIC:4,ENTROPY:4,MECHANISM:3", "antibuilder");
		put("BEAST:2,COLD:2,ARMOR:2", "arctic_boots", "arctic_helmet");
		put("BEAST:2,COLD:2,ARMOR:4", "arctic_chestplate");
		put("BEAST:12,COLD:7", "arctic_fur");
		put("BEAST:2,COLD:2,ORDER:1", "arctic_fur_block");
		put("BEAST:2,COLD:2,ARMOR:3", "arctic_leggings");
		put("METAL:2", "armor_shard");
		put("METAL:18,ARMOR:2", "armor_shard_cluster");
		put("COLD:6,AURA:1", "aurora_block");
		put("COLD:6,ORDER:2,AURA:1", "aurora_pillar");
		put("COLD:3,ORDER:1", "aurora_slab");
		put("COLD:2,LIGHT:2,CRYSTAL:2", "auroralized_glass");
		put("PLANT:5", "beanstalk_leaves", "canopy_leaves", "clover_patch", "fiddlehead", "mangrove_leaves", "mayapple", "rainbow_oak_leaves",
			"thorn_leaves", "twilight_oak_leaves");
		put("FLESH:2,HUNGER:2", "beef_jerky", "chicken_jerky", "cod_jerky", "fugu_jerky", "meef_jerky", "monster_jerky", "mutton_jerky", "pork_jerky",
			"rabbit_jerky", "salmon_jerky", "tropical_fish_jerky", "venison_jerky");
		put("HUNGER:3,PLANT:2,LIFE:1", "berry_medley");
		put("HUNGER:1,PLANT:1,LIFE:1", "blackberry", "blueberry", "maloberry", "raspberry");
		put("PLANT:2,CROP:2,LIFE:1", "blackberry_bush", "blueberry_bush", "maloberry_bush", "raspberry_bush");
		put("HUNGER:1,PLANT:1,ENTROPY:1", "blightberry");
		put("PLANT:2,CROP:2,ENTROPY:1", "blightberry_bush");
		put("METAL:171,CRAFT:57,TOOL:16,WEAPON:16", "block_and_chain");
		put("AURA:20,MECHANISM:10,TRAP:5", "blue_castle_door", "pink_castle_door", "violet_castle_door", "yellow_castle_door");
		put("ORDER:5,MECHANISM:1,AURA:3", "blue_castle_rune_brick", "pink_castle_rune_brick", "violet_castle_rune_brick", "yellow_castle_rune_brick");
		put("AURA:40", "blue_force_field", "green_force_field", "orange_force_field", "pink_force_field", "violet_force_field");
		put("ORDER:5,MECHANISM:1", "bold_castle_brick_pillar", "bold_castle_brick_stairs", "bold_castle_brick_tile", "castle_brick", "castle_brick_stairs",
			"castle_roof_tile", "cracked_castle_brick_stairs", "encased_castle_brick_pillar", "encased_castle_brick_stairs", "encased_castle_brick_tile",
			"mossy_castle_brick_stairs", "thick_castle_brick", "worn_castle_brick", "worn_castle_brick_stairs");
		put("EARTH:2,ORDER:1", "bold_stone_pillar");
		put("EXCHANGE:10", "borer_essence");
		put("METAL:3,FIRE:4,CRAFT:2", "brazier");
		put("CRYSTAL:2,VOID:1,ENTROPY:1", "brittle_potion_flask");
		put("PLANT:5,WEAPON:4", "brown_thorns", "green_thorns");
		put("ENTROPY:5", "burnt_thorns");
		put("METAL:2,LIGHT:3,FIRE:1", "candelabra");
		put("TREE:4,WATER:2,MOTION:2", "canopy_boat", "dark_boat", "mangrove_boat", "mining_boat", "sorting_boat", "time_boat", "transformation_boat",
			"twilight_oak_boat");
		put("TREE:3,MIND:4", "canopy_bookshelf", "chiseled_canopy_bookshelf");
		put("MECHANISM:1", "canopy_button", "dark_button", "mangrove_button", "mining_button", "sorting_button", "time_button", "transformation_button",
			"twilight_oak_button");
		put("TREE:4,VOID:4", "canopy_chest", "dark_chest", "mangrove_chest", "mining_chest", "sorting_chest", "time_chest", "transformation_chest",
			"twilight_oak_chest");
		put("TREE:6,WATER:2,MOTION:2,VOID:4", "canopy_chest_boat", "dark_chest_boat", "mangrove_chest_boat", "mining_chest_boat", "sorting_chest_boat",
			"time_chest_boat", "transformation_chest_boat", "twilight_oak_chest_boat");
		put("TREE:1,MECHANISM:1,MOTION:1", "canopy_door", "dark_door", "mangrove_door", "mining_door", "sorting_door", "time_door", "transformation_door",
			"twilight_oak_door");
		put("TREE:1", "canopy_fence", "canopy_planks", "canopy_slab", "canopy_stairs", "dark_fence", "dark_planks", "dark_slab", "dark_stairs",
			"mangrove_fence", "mangrove_planks", "mangrove_slab", "mangrove_stairs", "mining_fence", "mining_planks", "mining_slab", "mining_stairs",
			"sorting_fence", "sorting_planks", "sorting_slab", "sorting_stairs", "time_fence", "time_planks", "time_slab", "time_stairs",
			"transformation_fence", "transformation_planks", "transformation_slab", "transformation_stairs", "twilight_oak_fence", "twilight_oak_planks",
			"twilight_oak_slab", "twilight_oak_stairs");
		put("TREE:2,MECHANISM:1", "canopy_fence_gate", "canopy_trapdoor", "dark_fence_gate", "dark_trapdoor", "mangrove_fence_gate", "mangrove_trapdoor",
			"mining_fence_gate", "mining_trapdoor", "sorting_fence_gate", "sorting_trapdoor", "time_fence_gate", "time_trapdoor", "transformation_fence_gate",
			"transformation_trapdoor", "twilight_oak_fence_gate", "twilight_oak_trapdoor");
		put("TREE:1,SENSES:1,METAL:1", "canopy_hanging_sign", "dark_hanging_sign", "mangrove_hanging_sign", "mining_hanging_sign", "sorting_hanging_sign",
			"time_hanging_sign", "transformation_hanging_sign", "twilight_oak_hanging_sign");
		put("TREE:20", "canopy_log", "canopy_wood", "stripped_canopy_log", "stripped_canopy_wood", "stripped_twilight_oak_log", "stripped_twilight_oak_wood",
			"twilight_oak_log", "twilight_oak_wood");
		put("TREE:1,MECHANISM:1,SENSES:1", "canopy_pressure_plate", "dark_pressure_plate", "mangrove_pressure_plate", "mining_pressure_plate",
			"sorting_pressure_plate", "time_pressure_plate", "transformation_pressure_plate", "twilight_oak_pressure_plate");
		put("PLANT:2,LIFE:2", "canopy_sapling", "mangrove_sapling", "twilight_oak_sapling");
		put("TREE:1,SENSES:1", "canopy_sign", "dark_sign", "mangrove_sign", "mining_sign", "sorting_sign", "time_sign", "transformation_sign",
			"twilight_oak_sign");
		put("TREE:4,VOID:4,TRAP:2", "canopy_trapped_chest", "dark_trapped_chest", "mangrove_trapped_chest", "mining_trapped_chest", "sorting_trapped_chest",
			"time_trapped_chest", "transformation_trapped_chest", "twilight_oak_trapped_chest");
		put("TREE:1,CRYSTAL:1", "canopy_window", "canopy_window_pane");
		put("EXCHANGE:38,ENERGY:30,TAINT:26,SOUL:7,UNDEAD:3", "carminite");
		put("EXCHANGE:12,ENERGY:9,TAINT:6", "carminite_block");
		put("MECHANISM:4,MAGIC:4,ORDER:3", "carminite_builder");
		put("ENERGY:8,MAGIC:6,ENTROPY:4,MECHANISM:3", "carminite_reactor");
		put("EXCHANGE:16,MAGIC:8", "charm_of_keeping_1");
		put("EXCHANGE:24,MAGIC:12", "charm_of_keeping_2");
		put("EXCHANGE:32,MAGIC:16", "charm_of_keeping_3");
		put("LIFE:10,DEATH:10", "charm_of_life_1");
		put("LIFE:20,DEATH:20", "charm_of_life_2");
		put("SENSES:6,LIFE:2", "cicada");
		put("CRYSTAL:4,SENSES:4,LIFE:1", "cicada_jar");
		put("FIRE:4,EXCHANGE:3,MAGIC:2,CRAFT:2", "cinder_furnace");
		put("FIRE:4,ENTROPY:12", "cinder_log", "cinder_wood");
		put("BEAST:5,LIFE:5,CRAFT:1", "cooked_meef", "cooked_venison");
		put("METAL:2,EXCHANGE:1", "copper_berry");
		put("PLANT:2,METAL:2,EXCHANGE:1", "copper_oreberry_bush");
		put("CLOTH:3,GREED:2,ORDER:1", "coronation_carpet");
		put("ORDER:5,MECHANISM:1,ENTROPY:1", "cracked_castle_brick");
		put("EARTH:1,VOID:1,ENTROPY:1", "cracked_deadrock");
		put("EARTH:5,ENTROPY:1", "cracked_etched_nagastone", "cracked_nagastone_pillar", "cracked_nagastone_stairs_left", "cracked_nagastone_stairs_right");
		put("EARTH:3,TRAP:1,ENTROPY:1", "cracked_mazestone");
		put("TREE:15,ENTROPY:3", "cracked_towerwood");
		put("EARTH:3,ENTROPY:1", "cracked_underbrick");
		put("DEATH:2,ENTROPY:2,LIGHT:2,FIRE:1", "creeper_skull_candle");
		put("GREED:3,MAGIC:2,WEAPON:2", "crown_splinter");
		put("BEAST:35,SENSES:30,MAGIC:32", "crumble_horn");
		put("VOID:8,ENTROPY:6,MAGIC:6,WEAPON:3", "cube_of_annihilation");
		put("MAGIC:3,VOID:2", "cube_talisman", "vanishing_block");
		put("EARTH:3,TRAP:1,ORDER:1", "cut_mazestone", "decorative_mazestone", "mazestone_border", "mazestone_mosaic");
		put("PLANT:5,DARKNESS:1", "dark_leaves", "hardened_dark_leaves");
		put("TREE:20,DARKNESS:5", "dark_log", "dark_wood", "stripped_dark_log", "stripped_dark_wood");
		put("MAGIC:4,DARKNESS:2,CRAFT:3,GREED:2", "dark_tower_miniature_structure");
		put("PLANT:2,LIFE:2,DARKNESS:1", "darkwood_sapling");
		put("EARTH:1,VOID:1", "deadrock", "weathered_deadrock");
		put("WEAPON:20,TOOL:16,GREED:44,CRYSTAL:44", "diamond_minotaur_axe", "gold_minotaur_axe");
		put("HUNGER:1,PLANT:1,DARKNESS:1", "duskberry");
		put("PLANT:2,CROP:2,DARKNESS:1", "duskberry_bush");
		put("CLOTH:4,MAGIC:2,GREED:2", "emperors_cloth");
		put("PLANT:16,FIRE:46,ENERGY:22,MECHANISM:20", "encased_fire_jet");
		put("PLANT:33,AIR:16,ENERGY:30,MECHANISM:20", "encased_smoker");
		put("TREE:4,ORDER:2,METAL:1", "encased_towerwood");
		put("WEAPON:10,FLIGHT:5,ELDRITCH:30", "ender_bow");
		put("MAGIC:2,SOUL:1", "essence_berry");
		put("PLANT:2,MAGIC:2,SOUL:1", "essence_oreberry_bush");
		put("EARTH:5,ORDER:1", "etched_nagastone", "nagastone_pillar", "nagastone_stairs_left", "nagastone_stairs_right", "spiral_bricks");
		put("UNDEAD:4,SOUL:3,DEATH:2", "exanimate_essence");
		put("TAINT:15,EXCHANGE:2", "experiment_115");
		put("PLANT:1,AIR:1", "fallen_leaves");
		put("FIRE:20,METAL:8,MAGIC:4", "fiery_block");
		put("FIRE:40,LIFE:22", "fiery_blood");
		put("METAL:45,FIRE:120,ARMOR:16,AURA:36", "fiery_boots");
		put("METAL:90,FIRE:240,ARMOR:36,AURA:72", "fiery_chestplate");
		put("METAL:56,FIRE:150,ARMOR:16,AURA:45", "fiery_helmet");
		put("FIRE:40,METAL:15,AURA:12", "fiery_ingot");
		put("METAL:78,FIRE:210,ARMOR:28,AURA:63", "fiery_leggings");
		put("FIRE:8,METAL:4,TOOL:2,MINE:2", "fiery_pickaxe");
		put("FIRE:8,METAL:4,WEAPON:3", "fiery_sword");
		put("FIRE:40,WATER:22", "fiery_tears");
		put("PLANT:18,MIND:16,MAGIC:12,SENSES:10,LIGHT:5", "filled_magic_map", "magic_map");
		put("PLANT:18,MIND:16,MAGIC:12,SENSES:10,TRAP:10", "filled_maze_map", "maze_map");
		put("PLANT:18,MIND:16,MAGIC:12,SENSES:10,GREED:72", "filled_ore_map", "ore_map");
		put("MAGIC:10,ELDRITCH:6,DARKNESS:4,TRAP:2", "final_boss_boss_spawner");
		put("EARTH:5,FIRE:16", "fire_jet");
		put("LIGHT:5,LIFE:2", "firefly", "firefly_particle_spawner", "moonworm");
		put("CRYSTAL:35,LIGHT:20,SENSES:10", "firefly_jar");
		put("AIR:20,FLIGHT:5", "fluffy_cloud", "rainy_cloud", "snowy_cloud", "wispy_cloud");
		put("MAGIC:20,ARMOR:12,ORDER:6", "fortification_scepter");
		put("PLANT:2,MAGIC:2,GREED:2", "four_leaf_clover");
		put("SLIME:2,EARTH:1,ORDER:1", "gelatinous_maze_slime_drop");
		put("SLIME:2,WATER:1", "gelatinous_slime_drop");
		put("MAGIC:4,TRAP:4,UNDEAD:2", "ghast_trap");
		put("EARTH:320,ENTROPY:64", "giant_cobblestone");
		put("PLANT:320", "giant_leaves");
		put("TREE:1280", "giant_log");
		put("EARTH:320,FIRE:320,DARKNESS:320", "giant_obsidian");
		put("TOOL:12,MINE:12,EARTH:8", "giant_pickaxe");
		put("WEAPON:16,EARTH:8", "giant_sword");
		put("WEAPON:148,CRYSTAL:96", "glass_sword");
		put("METAL:2,GREED:1", "gold_berry");
		put("PLANT:2,METAL:2,GREED:1", "gold_oreberry_bush");
		put("CRYSTAL:4,VOID:2,MAGIC:1", "greater_potion_flask");
		put("PLANT:5,WEAPON:2", "hedge");
		put("TREE:3,VOID:2", "hollow_acacia_log", "hollow_birch_log", "hollow_canopy_log", "hollow_cherry_log", "hollow_crimson_stem", "hollow_dark_log",
			"hollow_dark_oak_log", "hollow_jungle_log", "hollow_mangrove_log", "hollow_mining_log", "hollow_oak_log", "hollow_pale_oak_log",
			"hollow_sorting_log", "hollow_spruce_log", "hollow_time_log", "hollow_transformation_log", "hollow_twilight_oak_log", "hollow_vangrove_log",
			"hollow_warped_stem");
		put("PLANT:2,LIFE:2,VOID:1", "hollow_oak_sapling");
		put("PLANT:25,WATER:5", "huge_lily_pad");
		put("PLANT:5,LIGHT:5,AIR:5", "huge_mushgloom", "huge_mushgloom_stem", "mushgloom");
		put("PLANT:25", "huge_stalk");
		put("PLANT:10,WATER:2", "huge_water_lily");
		put("MAGIC:8,BEAST:6,ELDRITCH:2,TRAP:2", "hydra_boss_spawner", "minoshroom_boss_spawner", "naga_boss_spawner");
		put("BEAST:25,LIFE:25,FIRE:5", "hydra_chop");
		put("DEATH:45,SENSES:20,BEAST:30,FIRE:80", "hydra_trophy");
		put("COLD:22,MOTION:8", "ice_bomb");
		put("WEAPON:10,FLIGHT:5,COLD:30", "ice_bow");
		put("WEAPON:16,COLD:24", "ice_sword");
		put("TREE:15,ENTROPY:3,BEAST:5", "infested_towerwood");
		put("METAL:2,PLANT:1", "iron_berry");
		put("METAL:1,MOTION:1", "iron_ladder");
		put("PLANT:2,METAL:2,MINE:1", "iron_oreberry_bush");
		put("METAL:2,TREE:1,LIFE:1,TOOL:2,TREE:1", "ironwood_axe");
		put("METAL:2,TREE:1,LIFE:1,ORDER:1", "ironwood_block");
		put("METAL:2,TREE:1,LIFE:1,ARMOR:2", "ironwood_boots", "ironwood_helmet");
		put("METAL:2,TREE:1,LIFE:1,ARMOR:4", "ironwood_chestplate");
		put("METAL:2,TREE:1,LIFE:1,TOOL:2,HARVEST:1", "ironwood_hoe");
		put("METAL:8,PLANT:3", "ironwood_ingot");
		put("METAL:2,TREE:1,LIFE:1,ARMOR:3", "ironwood_leggings");
		put("METAL:2,TREE:1,LIFE:1,TOOL:2,MINE:2", "ironwood_pickaxe");
		put("METAL:2,TREE:1,LIFE:1,TOOL:2,EARTH:1", "ironwood_shovel");
		put("METAL:2,TREE:1,LIFE:1,WEAPON:3", "ironwood_sword");
		put("TREE:2,DEATH:2,GREED:2,MAGIC:1", "keepsake_casket");
		put("MAGIC:8,UNDEAD:6,ELDRITCH:2,TRAP:2", "knight_phantom_boss_spawner", "lich_boss_spawner", "ur_ghast_boss_spawner");
		put("DEATH:45,SENSES:20,SOUL:80,MAN:40", "knight_phantom_trophy");
		put("METAL:3,ARMOR:1,TOOL:2,TREE:1", "knightmetal_axe");
		put("METAL:121,CRAFT:40,WEAPON:6", "knightmetal_block");
		put("METAL:3,ARMOR:1,ARMOR:2", "knightmetal_boots", "knightmetal_helmet");
		put("METAL:3,ARMOR:1,ARMOR:4", "knightmetal_chestplate");
		put("METAL:18,CRAFT:6", "knightmetal_ingot");
		put("METAL:3,ARMOR:1,ARMOR:3", "knightmetal_leggings");
		put("METAL:3,ARMOR:1,TOOL:2,MINE:2", "knightmetal_pickaxe");
		put("METAL:4,GREED:2", "knightmetal_ring");
		put("METAL:12,ARMOR:8", "knightmetal_shield");
		put("METAL:3,ARMOR:1,WEAPON:3", "knightmetal_sword");
		put("MAGIC:53,FIRE:87,ENERGY:42", "lamp_of_cinders");
		put("MAGIC:4,UNDEAD:2,CRAFT:3,GREED:2", "lich_tower_miniature_structure");
		put("DEATH:45,SENSES:20,UNDEAD:75,MAN:5,ELDRITCH:40", "lich_trophy");
		put("MAGIC:60,LIFE:26,EXCHANGE:18", "lifedrain_scepter");
		put("EARTH:9,LIFE:6,PLANT:7", "liveroot");
		put("PLANT:15,EARTH:10,LIFE:10", "liveroot_block");
		put("MAGIC:3,VOID:3,ORDER:2", "locked_vanishing_block");
		put("PLANT:8,MAGIC:36", "magic_beans");
		put("FLIGHT:5,AIR:5,LIGHT:14", "magic_map_focus");
		put("SENSES:4,CLOTH:2,MAGIC:4", "magic_painting");
		put("TREE:20,WATER:5", "mangrove_log", "mangrove_wood", "stripped_mangrove_log", "stripped_mangrove_wood");
		put("PLANT:15,EARTH:10", "mangrove_root", "root");
		put("CRYSTAL:4,VOID:2", "mason_jar");
		put("TRAP:10,MECHANISM:15", "maze_map_focus");
		put("SLIME:2,EARTH:1", "maze_slime_ball");
		put("SLIME:9,EARTH:3,ORDER:1", "maze_slime_block");
		put("PLANT:2", "maze_wafer");
		put("TOOL:16,TRAP:8,METAL:33", "mazebreaker_pickaxe");
		put("EARTH:5,TRAP:1", "mazestone");
		put("EARTH:3,TRAP:1", "mazestone_brick");
		put("BEAST:25,MAN:15,LIFE:25,CRAFT:5", "meef_stroganoff");
		put("PLANT:5,GREED:1", "mining_leaves");
		put("TREE:20,GREED:3", "mining_log", "mining_wood", "stripped_mining_log", "stripped_mining_wood");
		put("PLANT:20,GREED:55", "mining_log_core");
		put("PLANT:15,LIFE:5,GREED:40", "mining_sapling");
		put("DEATH:45,SENSES:20,BEAST:70,MAN:50", "minoshroom_trophy");
		put("MAGIC:4,EARTH:2,CRAFT:3,GREED:2", "minotaur_labyrinth_miniature_structure", "naga_courtyard_miniature_structure");
		put("MECHANISM:3,SENSES:2,LIGHT:2", "moon_dial");
		put("BEAST:35,LIGHT:54,LIFE:36,EXCHANGE:28", "moonworm_queen");
		put("PLANT:5,WATER:2", "moss_patch");
		put("HUNGER:3,PLANT:2", "moss_soup");
		put("ORDER:5,MECHANISM:1,PLANT:1", "mossy_castle_brick");
		put("EARTH:5,ORDER:1,PLANT:1", "mossy_etched_nagastone", "mossy_nagastone_pillar", "mossy_nagastone_stairs_left", "mossy_nagastone_stairs_right");
		put("EARTH:3,TRAP:1,PLANT:1", "mossy_mazestone");
		put("TREE:18", "mossy_towerwood");
		put("EARTH:3,PLANT:1", "mossy_underbrick");
		put("SENSES:4,AIR:2,MAGIC:1", "music_disc_findings", "music_disc_home", "music_disc_maker", "music_disc_motion", "music_disc_radiance",
			"music_disc_steps", "music_disc_superstitious", "music_disc_thread", "music_disc_wayfarer");
		put("GREED:6,MAGIC:4,MIND:3", "mystic_crown");
		put("ARMOR:28,BEAST:12,MOTION:15", "naga_chestplate");
		put("ARMOR:24,BEAST:10,MOTION:12", "naga_leggings");
		put("ARMOR:5,BEAST:4,MOTION:2", "naga_scale");
		put("DEATH:45,SENSES:20,BEAST:80,MOTION:40", "naga_trophy");
		put("EARTH:3,ORDER:1", "nagastone", "nagastone_head");
		put("METAL:24,GREED:30", "ore_magnet");
		put("SENSES:17,GREED:8,MECHANISM:15,MIND:10", "ore_meter");
		put("BEAST:35,MOTION:48,FLIGHT:60,MAGIC:32", "peacock_feather_fan");
		put("ARMOR:32,SOUL:62,DEATH:19,ELDRITCH:22,TRAP:48", "phantom_chestplate");
		put("ARMOR:12,SOUL:24,DEATH:7,ELDRITCH:8,TRAP:22", "phantom_helmet");
		put("DEATH:2,GREED:2,LIGHT:2,FIRE:1", "piglin_skull_candle");
		put("DEATH:2,MAN:2,LIGHT:2,FIRE:1", "player_skull_candle");
		put("MECHANISM:3,METAL:2,ORDER:2", "pocket_watch");
		put("LIFE:45,GREED:40,EXCHANGE:40", "quest_ram_trophy");
		put("PLANT:2,LIFE:2,MAGIC:1", "rainbow_oak_sapling");
		put("FLIGHT:5,AIR:5,DARKNESS:1", "raven_feather");
		put("EARTH:9,LIFE:6,TREE:7,METAL:16,GREED:1", "raw_ironwood");
		put("BEAST:5,LIFE:5,EARTH:5", "raw_meef", "raw_venison");
		put("MAGIC:3,ORDER:2,VOID:1", "reappearing_block");
		put("CLOTH:2,MAGIC:2,SENSES:1", "red_thread");
		put("PLANT:1,TREE:1", "root_strand");
		put("CLOTH:1,PLANT:1", "rope");
		put("WEAPON:10,FLIGHT:5,MECHANISM:30", "seeker_bow");
		put("HUNGER:2,CROP:2", "shika_senbei");
		put("MAGIC:6,DEATH:4,DARKNESS:3,TRAP:2", "sinister_spawner");
		put("DEATH:2,UNDEAD:2,LIGHT:2,FIRE:1", "skeleton_skull_candle", "wither_skeleton_skull_candle", "zombie_skull_candle");
		put("TREE:3,DEATH:2,GREED:1", "skull_chest");
		put("HUNGER:1,PLANT:1,AIR:1", "skyberry");
		put("PLANT:2,CROP:2,AIR:1", "skyberry_bush");
		put("MAGIC:3,MECHANISM:3,MOTION:2", "slider");
		put("EARTH:5,AIR:16", "smoker");
		put("DEATH:45,SENSES:20,COLD:80,MAN:40", "snow_queen_trophy");
		put("PLANT:5,ORDER:1", "sorting_leaves");
		put("TREE:20,ORDER:3", "sorting_log", "sorting_wood", "stripped_sorting_log", "stripped_sorting_wood");
		put("PLANT:20,ORDER:55", "sorting_log_core");
		put("PLANT:15,LIFE:5,ORDER:40", "sorting_sapling");
		put("HUNGER:1,CROP:1,ENTROPY:1", "stale_bread");
		put("PLANT:2,METAL:1,TOOL:2,TREE:1", "steeleaf_axe");
		put("PLANT:2,METAL:1,ORDER:1", "steeleaf_block");
		put("PLANT:2,METAL:1,ARMOR:2", "steeleaf_boots", "steeleaf_helmet");
		put("PLANT:2,METAL:1,ARMOR:4", "steeleaf_chestplate");
		put("PLANT:2,METAL:1,TOOL:2,HARVEST:1", "steeleaf_hoe");
		put("PLANT:12,METAL:4", "steeleaf_ingot");
		put("PLANT:2,METAL:1,ARMOR:3", "steeleaf_leggings");
		put("PLANT:2,METAL:1,TOOL:2,MINE:2", "steeleaf_pickaxe");
		put("PLANT:2,METAL:1,TOOL:2,EARTH:1", "steeleaf_shovel");
		put("PLANT:2,METAL:1,WEAPON:3", "steeleaf_sword");
		put("HUNGER:1,PLANT:1,WEAPON:1", "stingberry");
		put("PLANT:2,CROP:2,WEAPON:1", "stingberry_bush");
		put("TREE:20,AURA:3", "stripped_time_log", "stripped_time_wood", "time_log", "time_wood");
		put("TREE:20,EXCHANGE:3", "stripped_transformation_log", "stripped_transformation_wood", "transformation_log", "transformation_wood");
		put("EARTH:5,ARMOR:5", "stronghold_shield");
		put("BEAST:2,CLOTH:2", "tanned_leather");
		put("TREE:2,WATER:1", "tannin");
		put("EARTH:2,FIRE:1,SENSES:1", "terrorcotta_arcs", "terrorcotta_curves", "terrorcotta_lines");
		put("PLANT:5,WEAPON:2,SENSES:2", "thorn_rose");
		put("PLANT:5,AURA:1", "time_leaves");
		put("PLANT:20,AURA:55", "time_log_core");
		put("PLANT:15,LIFE:5,AURA:40", "time_sapling");
		put("LIGHT:8,PLANT:3", "torchberries");
		put("LIGHT:10,PLANT:5", "torchberry_plant");
		put("TRAP:16", "tower_key");
		put("TREE:4,ORDER:1", "towerwood");
		put("PLANT:5,EXCHANGE:1", "transformation_leaves");
		put("PLANT:20,EXCHANGE:55", "transformation_log_core");
		put("EXCHANGE:30,MAGIC:12", "transformation_powder");
		put("PLANT:15,LIFE:5,EXCHANGE:40", "transformation_sapling");
		put("TRAVEL:4,CLOTH:2,MOTION:2", "travellers_belt");
		put("TRAVEL:4,ARMOR:2,MOTION:3", "travellers_boots");
		put("TRAVEL:3,CLOTH:2,TOOL:1", "travellers_gloves");
		put("TRAVEL:3,SENSES:3,CRYSTAL:1", "travellers_goggles");
		put("TRAVEL:4,ARMOR:3,CLOTH:2", "travellers_vest");
		put("TRAVEL:4,FLIGHT:4,AIR:2", "travellers_wings");
		put("BEAST:2,CLOTH:2,ARMOR:1", "treated_leather");
		put("WEAPON:10,FLIGHT:5,EXCHANGE:30", "triple_bow");
		put("PLANT:5,LIGHT:25,DARKNESS:3", "trollber");
		put("EARTH:5", "trollsteinn");
		put("PLANT:5,DARKNESS:3", "trollvidr");
		put("MECHANISM:35,SENSES:10,EARTH:5", "trophy_pedestal");
		put("MAGIC:4,TRAVEL:3,CRAFT:3,GREED:2", "twilight_portal_miniature_structure");
		put("MAGIC:60,DARKNESS:30,VOID:15", "twilight_scepter");
		put("EARTH:2,ENTROPY:1", "twisted_stone");
		put("EARTH:2,ENTROPY:1,ORDER:1", "twisted_stone_pillar");
		put("EARTH:5,PLANT:2,LIFE:15", "uberous_soil");
		put("CRAFT:6,ENTROPY:6,MAGIC:4,EXCHANGE:3", "uncrafting_table");
		put("EARTH:3", "underbrick", "underbrick_floor");
		put("PLANT:5,DARKNESS:6", "unripe_trollber");
		put("DEATH:45,SENSES:20,TAINT:80,EXCHANGE:40", "ur_ghast_trophy");
		put("METAL:1", "wrought_iron_bar");
		put("METAL:2,ORDER:1", "wrought_iron_fence");
		put("BEAST:144,COLD:84,ARMOR:12,AURA:36", "yeti_boots");
		put("BEAST:288,COLD:168,ARMOR:28,AURA:72", "yeti_chestplate");
		put("BEAST:180,COLD:105,ARMOR:16,AURA:45", "yeti_helmet");
		put("BEAST:252,COLD:147,ARMOR:24,AURA:63", "yeti_leggings");
		put("MAGIC:60,DEATH:25,SOUL:25,TRAP:22", "zombie_scepter");
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
			int count = 0;
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
				count++;
			}
			TwilightForestMod.LOGGER.info("Added Thaumcraft aspects to {} Twilight Forest items", count);
		} catch (ReflectiveOperationException e) {
			TwilightForestMod.LOGGER.error("Could not add Twilight Forest aspects to Thaumcraft", e);
		}
	}
}
