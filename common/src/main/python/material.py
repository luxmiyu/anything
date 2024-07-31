import os
import json

from PIL import Image # type: ignore
import numpy as np # type: ignore

PATH_BASE = 'python/base'
PATH_BLOCK = 'python/block'

PATH_LANG = 'resources/assets/anything/lang'
PATH_MODELS = 'resources/assets/anything/models/item'
PATH_TEXTURES = 'resources/assets/anything/textures/item'
PATH_LAYERS = 'resources/assets/anything/textures/models/armor'

PATH_RECIPE = 'resources/data/anything/recipe'

IMAGE_WIDTH = 16
IMAGE_HEIGHT = 16

LAYER_WIDTH = 64
LAYER_HEIGHT = 32

TOOLS = ['axe', 'hoe', 'pickaxe', 'shovel', 'sword']
ARMORS = ['helmet', 'chestplate', 'leggings', 'boots']

def tool_model(name, tool):
  return {
    'parent': 'minecraft:item/handheld',
    'textures': {
      'layer0': f'anything:item/{name}_{tool}'
    }
  }

def armor_model(name, armor):
  return {
    'parent': 'minecraft:item/generated',
    'textures': {
      'layer0': f'anything:item/{name}_{armor}'
    }
  }

def pattern(equipment):
  match equipment:
    case 'axe': return [
      '##',
      '#I',
      ' I'
    ]
    case 'hoe': return [
      '##',
      ' I',
      ' I'
    ]
    case 'pickaxe': return [
      '###',
      ' I ',
      ' I '
    ]
    case 'shovel': return [
      '#',
      'I',
      'I'
    ]
    case 'sword': return [
      '#',
      '#',
      'I'
    ]
    case 'helmet': return [
      '###',
      '# #'
    ]
    case 'chestplate': return [
      '# #',
      '###',
      '###'
    ]
    case 'leggings': return [
      '###',
      '# #',
      '# #'
    ]
    case 'boots': return [
      '# #',
      '# #'
    ]

def recipe(name, equipment):
  key = {
    '#': {
      'item': f'minecraft:{name}'
    },
    'I': {
      'item': 'minecraft:stick'
    }
  } if equipment in TOOLS else {
    '#': {
      'item': f'minecraft:{name}'
    }
  }

  return {
    'type': 'minecraft:crafting_shaped',
    'category': 'equipment',
    'pattern': pattern(equipment),
    'key': key,
    'result': {
      'id': f'anything:{name}_{equipment}',
      'count': 1
    }
  }

def smelt_recipe(name, equipment, blasting = False):
  return {
    'type': 'minecraft:blasting' if blasting else 'minecraft:smelting',
    'category': 'misc',
    'ingredient': [
      {
        'item': f'anything:{name}_sword'
      },
      {
        'item': f'anything:{name}_pickaxe'
      },
      {
        'item': f'anything:{name}_axe'
      },
      {
        'item': f'anything:{name}_shovel'
      },
      {
        'item': f'anything:{name}_hoe'
      },
      {
        'item': f'anything:{name}_helmet'
      },
      {
        'item': f'anything:{name}_chestplate'
      },
      {
        'item': f'anything:{name}_leggings'
      },
      {
        'item': f'anything:{name}_boots'
      },
    ],
    'result': {
      'id': f'minecraft:{name}'
    },
    'experience': 0.1,
    'cookingtime': 100 if blasting else 200
  }

def open_image_as_rgba(image_path: str) -> Image.Image:
    img = Image.open(image_path)
    if img.mode != 'RGBA':
        img = img.convert('RGBA')
    return img

class Material:
  def __init__(self, block, texture = None):
    self.block = block
    self.texture = block if texture is None else texture
  
  def get_lang(self, equipment):
    key = f'item.anything.{self.block}_{equipment}'
    names = ' '.join([word.capitalize() for word in self.block.split('_')])
    value = f'{names} {equipment.capitalize()}'

    return [key, value]

  def get_langs(self):
    return [self.get_lang(equipment) for equipment in TOOLS + ARMORS]

  def generate_models(self):
    for tool in TOOLS:
      path = f'{PATH_MODELS}/{self.block}_{tool}.json'

      with open(path, 'w') as file:
        json.dump(tool_model(self.block, tool), file, indent = 2)

    for armor in ARMORS:
      path = f'{PATH_MODELS}/{self.block}_{armor}.json'

      with open(path, 'w') as file:
        json.dump(armor_model(self.block, armor), file, indent = 2)

  def generate_recipes(self):
    for equipment in TOOLS + ARMORS:
      path = f'{PATH_RECIPE}/{self.block}_{equipment}.json'

      with open(path, 'w') as file:
        json.dump(recipe(self.block, equipment), file, indent = 2)
    
    path_smelting = f'{PATH_RECIPE}/smelting_{self.block}.json'
    path_blasting = f'{PATH_RECIPE}/blasting_{self.block}.json'

    with open(path_smelting, 'w') as file:
      json.dump(smelt_recipe(self.block, False), file, indent = 2)

    with open(path_blasting, 'w') as file:
      json.dump(smelt_recipe(self.block, True), file, indent = 2)

  def generate_tool_texture(self, tool):
    is_glass = f"{self.block}".endswith("glass") or self.block == "barrier"

    overlay_path = f'{PATH_BASE}/{tool}_overlay.png' if not is_glass else f'{PATH_BASE}/{tool}_overlay_glass.png'
    outline_path = f'{PATH_BASE}/{tool}_overlay_glass.png'
    texture_path = f'{PATH_BLOCK}/{self.texture}.png'
    handle_path = f'{PATH_BASE}/{tool}_handle.png'
    result_path = f'{PATH_TEXTURES}/{self.block}_{tool}.png'

    overlay = open_image_as_rgba(overlay_path)
    outline = open_image_as_rgba(outline_path)
    texture = open_image_as_rgba(texture_path)
    handle = open_image_as_rgba(handle_path)

    overlay_array = np.array(overlay, dtype=np.float32)
    outline_array = np.array(outline, dtype=np.float32)
    texture_array = np.array(texture, dtype=np.float32)
    handle_array = np.array(handle, dtype=np.float32)
    result_array = np.zeros((IMAGE_HEIGHT, IMAGE_WIDTH, 4), dtype=np.float32)

    for y in range(IMAGE_HEIGHT):
      for x in range(IMAGE_WIDTH):
        overlay_pixel = overlay_array[y, x]
        outline_pixel = outline_array[y, x]
        texture_pixel = texture_array[y, x]
        handle_pixel = handle_array[y, x]

        # apply overlay in multiply mode if present, always apply on outline
        if overlay_pixel[3] > 0 and (texture_pixel[3] > 0 or outline_pixel[3] > 0):
          result_array[y, x][0] = (texture_pixel[0] * overlay_pixel[0]) // 255
          result_array[y, x][1] = (texture_pixel[1] * overlay_pixel[1]) // 255
          result_array[y, x][2] = (texture_pixel[2] * overlay_pixel[2]) // 255
          result_array[y, x][3] = overlay_pixel[3]

        # apply handle if present
        if handle_pixel[3] > 0:
          result_array[y, x][0] = handle_pixel[0]
          result_array[y, x][1] = handle_pixel[1]
          result_array[y, x][2] = handle_pixel[2]
          result_array[y, x][3] = handle_pixel[3]
    
    result_array = np.clip(result_array, 0, 255).astype(np.uint8)

    result = Image.fromarray(result_array)

    result.save(result_path)

  def generate_armor_texture(self, armor):
    is_glass = f"{self.block}".endswith("glass") or self.block == "barrier"

    overlay_path = f'{PATH_BASE}/{armor}_overlay.png' if not is_glass else f'{PATH_BASE}/{armor}_overlay_glass.png'
    outline_path = f'{PATH_BASE}/{armor}_overlay_glass.png'
    texture_path = f'{PATH_BLOCK}/{self.texture}.png'
    result_path = f'{PATH_TEXTURES}/{self.block}_{armor}.png'

    overlay = open_image_as_rgba(overlay_path)
    outline = open_image_as_rgba(outline_path)
    texture = open_image_as_rgba(texture_path)

    overlay_array = np.array(overlay, dtype=np.float32)
    outline_array = np.array(outline, dtype=np.float32)
    texture_array = np.array(texture, dtype=np.float32)
    result_array = np.zeros((IMAGE_HEIGHT, IMAGE_WIDTH, 4), dtype=np.float32)

    for y in range(IMAGE_HEIGHT):
      for x in range(IMAGE_WIDTH):
        overlay_pixel = overlay_array[y, x]
        outline_pixel = outline_array[y, x]
        texture_pixel = texture_array[y, x]

        # apply overlay in multiply mode if present, always apply on outline
        if overlay_pixel[3] > 0 and (texture_pixel[3] > 0 or outline_pixel[3] > 0):
          result_array[y, x][0] = (texture_pixel[0] * overlay_pixel[0]) // 255
          result_array[y, x][1] = (texture_pixel[1] * overlay_pixel[1]) // 255
          result_array[y, x][2] = (texture_pixel[2] * overlay_pixel[2]) // 255
          result_array[y, x][3] = overlay_pixel[3]
    
    result_array = np.clip(result_array, 0, 255).astype(np.uint8)

    result = Image.fromarray(result_array)

    result.save(result_path)

  def generate_layer_texture(self):
    is_glass = f"{self.block}".endswith("glass") or self.block == "barrier"

    overlay_1_path = f'{PATH_BASE}/armor_overlay_1.png' if not is_glass else f'{PATH_BASE}/armor_overlay_1_glass.png'
    overlay_2_path = f'{PATH_BASE}/armor_overlay_2.png' if not is_glass else f'{PATH_BASE}/armor_overlay_2_glass.png'
    texture_path = f'{PATH_BLOCK}/{self.texture}.png'
    result_1_path = f'{PATH_LAYERS}/{self.block}_layer_1.png'
    result_2_path = f'{PATH_LAYERS}/{self.block}_layer_2.png'

    overlay_1 = open_image_as_rgba(overlay_1_path)
    overlay_2 = open_image_as_rgba(overlay_2_path)
    texture = open_image_as_rgba(texture_path)

    overlay_1_array = np.array(overlay_1, dtype=np.float32)
    overlay_2_array = np.array(overlay_2, dtype=np.float32)
    texture_array = np.array(texture, dtype=np.float32)
    result_1_array = np.zeros((LAYER_HEIGHT, LAYER_WIDTH, 4), dtype=np.float32)
    result_2_array = np.zeros((LAYER_HEIGHT, LAYER_WIDTH, 4), dtype=np.float32)

    for y in range(LAYER_HEIGHT):
      for x in range(LAYER_WIDTH):
        overlay_1_pixel = overlay_1_array[y, x]
        overlay_2_pixel = overlay_2_array[y, x]
        texture_pixel = texture_array[y % 16, x % 16]

        # apply overlay in multiply mode if present
        if overlay_1_pixel[3] > 0 and texture_pixel[3] > 0:
          result_1_array[y, x][0] = (texture_pixel[0] * overlay_1_pixel[0]) // 255
          result_1_array[y, x][1] = (texture_pixel[1] * overlay_1_pixel[1]) // 255
          result_1_array[y, x][2] = (texture_pixel[2] * overlay_1_pixel[2]) // 255
          result_1_array[y, x][3] = overlay_1_pixel[3]
        if overlay_2_pixel[3] > 0 and texture_pixel[3] > 0:
          result_2_array[y, x][0] = (texture_pixel[0] * overlay_2_pixel[0]) // 255
          result_2_array[y, x][1] = (texture_pixel[1] * overlay_2_pixel[1]) // 255
          result_2_array[y, x][2] = (texture_pixel[2] * overlay_2_pixel[2]) // 255
          result_2_array[y, x][3] = overlay_2_pixel[3]
    
    result_1_array = np.clip(result_1_array, 0, 255).astype(np.uint8)
    result_2_array = np.clip(result_2_array, 0, 255).astype(np.uint8)

    result_1 = Image.fromarray(result_1_array)
    result_2 = Image.fromarray(result_2_array)

    result_1.save(result_1_path)
    result_2.save(result_2_path)

  def generate_textures(self):
    try:
      for tool in TOOLS:
        self.generate_tool_texture(tool)
      for armor in ARMORS:
        self.generate_armor_texture(armor)
        
      self.generate_layer_texture()
    except FileNotFoundError:
      print(f'Error: {self.texture}')
