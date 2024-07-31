alts = {
  'podzol': 'podzol_side',
  'suspicious_sand': 'suspicious_sand_2',
  'suspicious_gravel': 'suspicious_gravel_2',
  'mangrove_roots': 'mangrove_roots_top',
  'muddy_mangrove_roots': 'muddy_mangrove_roots_top',
  'oak_wood': 'oak_log',
  'spruce_wood': 'spruce_log',
  'birch_wood': 'birch_log',
  'jungle_wood': 'jungle_log',
  'acacia_wood': 'acacia_log',
  'cherry_wood': 'cherry_log',
  'dark_oak_wood': 'dark_oak_log',
  'mangrove_wood': 'mangrove_log',
  'stripped_oak_wood': 'stripped_oak_log',
  'stripped_spruce_wood': 'stripped_spruce_log',
  'stripped_birch_wood': 'stripped_birch_log',
  'stripped_jungle_wood': 'stripped_jungle_log',
  'stripped_acacia_wood': 'stripped_acacia_log',
  'stripped_cherry_wood': 'stripped_cherry_log',
  'stripped_dark_oak_wood': 'stripped_dark_oak_log',
  'stripped_mangrove_wood': 'stripped_mangrove_log',
  'dispenser': 'dispenser_front',
  'sticky_piston': 'piston_top_sticky',
  'piston': 'piston_top',
  'tnt': 'tnt_side',
  'chiseled_bookshelf': 'chiseled_bookshelf_occupied',
  'crafting_table': 'crafting_table_top',
  'furnace': 'furnace_front',
  'snow_block': 'snow',
  'jukebox': 'jukebox_top',
  'pumpkin': 'pumpkin_side',
  'basalt': 'basalt_top',
  'polished_basalt': 'polished_basalt_top',
  'infested_stone': 'stone',
  'infested_cobblestone': 'cobblestone',
  'infested_stone_bricks': 'bricks',
  'infested_mossy_stone_bricks': 'stone_bricks',
  'infested_cracked_stone_bricks': 'cracked_stone_bricks',
  'infested_chiseled_stone_bricks': 'chiseled_stone_bricks',
  'melon': 'melon_side',
  'mycelium': 'mycelium_side',
  'command_block': 'command_block_front',
  'quartz_block': 'quartz_block_side',
  'dropper': 'dropper_front',
  'barrier': 'redstone_block',
  'hay_block': 'hay_block_side',
  'smooth_sandstone': 'cut_sandstone',
  'smooth_quartz': 'quartz_block_top',
  'smooth_red_sandstone': 'cut_red_sandstone',
  'repeating_command_block': 'repeating_command_block_front',
  'chain_command_block': 'chain_command_block_front',
  'frosted_ice': 'frosted_ice_0',
  'magma_block': 'magma',
  'bone_block': 'bone_block_top',
  'observer': 'observer_front',
  'dried_kelp_block': 'dried_kelp_top',
  'loom': 'loom_top',
  'barrel': 'barrel_side',
  'smoker': 'smoker_front',
  'blast_furnace': 'blast_furnace_front',
  'cartography_table': 'cartography_table_side2',
  'fletching_table': 'fletching_table_front',
  'smithing_table': 'smithing_table_front',
  'warped_hyphae': 'warped_stem',
  'stripped_warped_hyphae': 'stripped_warped_stem',
  'crimson_hyphae': 'crimson_stem',
  'stripped_crimson_hyphae': 'stripped_crimson_stem',
  'jigsaw': 'jigsaw_top',
  'target': 'target_side',
  'bee_nest': 'bee_nest_front',
  'beehive': 'beehive_front',
  'ancient_debris': 'ancient_debris_side',
  'respawn_anchor': 'respawn_anchor_side0',
  'lodestone': 'lodestone_side',
  'sculk_catalyst': 'sculk_catalyst_side',
  'waxed_copper_block': 'copper_block',
  'waxed_weathered_copper': 'weathered_copper',
  'waxed_exposed_copper': 'exposed_copper',
  'waxed_oxidized_copper': 'oxidized_copper',
  'waxed_oxidized_cut_copper': 'oxidized_cut_copper',
  'waxed_weathered_cut_copper': 'weathered_cut_copper',
  'waxed_exposed_cut_copper': 'exposed_cut_copper',
  'waxed_cut_copper': 'cut_copper',
  'infested_deepslate': 'deepslate',
  'ochre_froglight': 'ochre_froglight_side',
  'verdant_froglight': 'verdant_froglight_side',
  'pearlescent_froglight': 'pearlescent_froglight_side',
  'reinforced_deepslate': 'reinforced_deepslate_side',
  'stonecutter': 'smooth_stone',

  'waxed_oxidized_chiseled_copper': 'oxidized_chiseled_copper',
  'waxed_weathered_chiseled_copper': 'weathered_chiseled_copper',
  'waxed_exposed_chiseled_copper': 'exposed_chiseled_copper',
  'waxed_chiseled_copper': 'chiseled_copper',
  'waxed_copper_grate': 'copper_grate',
  'waxed_exposed_copper_grate': 'exposed_copper_grate',
  'waxed_weathered_copper_grate': 'weathered_copper_grate',
  'waxed_oxidized_copper_grate': 'oxidized_copper_grate',
  'waxed_copper_bulb': 'copper_bulb',
  'waxed_exposed_copper_bulb': 'exposed_copper_bulb',
  'waxed_weathered_copper_bulb': 'weathered_copper_bulb',
  'waxed_oxidized_copper_bulb': 'oxidized_copper_bulb',
  'crafter': 'crafter_top_crafting',
  'trial_spawner': 'trial_spawner_side_inactive',
  'vault': 'vault_side_off',

  'glass': 'snow',
  'honey_block': 'honey_block_top',

  'grass_block': 'custom_grass_block',
  'acacia_leaves': 'custom_acacia_leaves',
  'birch_leaves': 'custom_birch_leaves',
  'dark_oak_leaves': 'custom_dark_oak_leaves',
  'jungle_leaves': 'custom_jungle_leaves',
  'mangrove_leaves': 'custom_mangrove_leaves',
  'oak_leaves': 'custom_oak_leaves',
  'spruce_leaves': 'custom_spruce_leaves',
}

def find_alt(name):
  return alts.get(name, name)