from material import Material
from blocks import blocks
from alts import find_alt

import os
import json

PATH_BASE = 'python/base'
PATH_BLOCK = 'python/block'

PATH_LANG = 'resources/assets/anything/lang'
PATH_MODELS = 'resources/assets/anything/models/item'
PATH_TEXTURES = 'resources/assets/anything/textures/item'

PATH_RECIPE = 'resources/data/anything/recipe'
PATH_TAG = 'resources/data/minecraft/tags/item'
PATH_LAYERS = 'resources/assets/anything/textures/models/armor'

TOOLS = ['axe', 'hoe', 'pickaxe', 'shovel', 'sword']

def empty_directories():
  for path in [PATH_LANG, PATH_MODELS, PATH_TEXTURES, PATH_RECIPE, PATH_TAG, PATH_LAYERS]:
    for file in os.listdir(path):
      os.remove(f'{path}/{file}')

def generate_lang(lang: dict):
  with open(f'{PATH_LANG}/en_us.json', 'w') as file:
    json.dump(lang, file, indent=2)

def generate_tag(name: str, items: list[str]):
  with open(f'{PATH_TAG}/{name}.json', 'w') as file:
    json.dump({
      'replace': False,
      'values': items
    }, file, indent=2)

def main():
  empty_directories()

  materials: list[Material] = []

  lang = {
    'anything.display_name': 'Anything',
    'anything.description': 'Create tools and armor out of any block! This mod adds 3800+ tools and armor pieces to the game, for each of the blocks in the game.',
    'anything.luxmiyu': 'luxmiyu',
    'anything.discord': 'Discord',
    'message.anything.command_not_player': 'You must be a player to run this command!',
    'message.anything.command_no_permission': 'You don\'t have permission to use this command',
  }

  tags = {
    'sword': [],
    'pickaxe': [],
    'axe': [],
    'shovel': [],
    'hoe': [],
    'head_armor': [],
    'chest_armor': [],
    'leg_armor': [],
    'foot_armor': [],
  }

  for block in blocks:
    material = Material(block, find_alt(block))
    materials.append(material)

    for tool in TOOLS:
      tags[tool].append(f'anything:{block}_{tool}')
    
    tags['head_armor'].append(f'anything:{block}_helmet')
    tags['chest_armor'].append(f'anything:{block}_chestplate')
    tags['leg_armor'].append(f'anything:{block}_leggings')
    tags['foot_armor'].append(f'anything:{block}_boots')

  total_count = materials.__len__()
  current_count = 0

  for material in materials:
    for key, value in material.get_langs():
      lang[key] = value

    material.generate_models()
    material.generate_textures()
    material.generate_recipes()

    current_count += 1
    print(f'[{current_count}/{total_count}] generated: {material.block}')

  generate_lang(lang)
  print('generated lang file')

  for tool in TOOLS:
    generate_tag(tool + 's', tags[tool])
  
  generate_tag('head_armor', tags['head_armor'])
  generate_tag('chest_armor', tags['chest_armor'])
  generate_tag('leg_armor', tags['leg_armor'])
  generate_tag('foot_armor', tags['foot_armor'])

if __name__ == '__main__':
  main()
