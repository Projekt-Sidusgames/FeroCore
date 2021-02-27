package com.gestankbratwurst.ferocore.modules.rolemodule;

import com.gestankbratwurst.ferocore.FeroCore;
import com.gestankbratwurst.ferocore.modules.BaseModule;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemManager;
import com.gestankbratwurst.ferocore.modules.customitems.CustomItemModule;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeManager;
import com.gestankbratwurst.ferocore.modules.customrecipes.CustomRecipeModule;
import com.gestankbratwurst.ferocore.modules.rolemodule.itemhandles.AlchemistStaffItemHandle;
import com.gestankbratwurst.ferocore.modules.rolemodule.itemhandles.ShamanStaffItemHandle;
import com.gestankbratwurst.ferocore.modules.rolemodule.itemhandles.SpearItemHandle;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.AlchemistStaffHeadRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.AlchemistStaffRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.DiamondSpearPikeRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.DiamondSpearRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.GoldenSpearPikeRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.GoldenSpearRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.IronSpearPikeRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.IronSpearRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.NetheriteSpearPikeRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.NetheriteSpearRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.ShamanStaffHeadRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.ShamanStaffRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.StoneSpearPikeRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.StoneSpearRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.WoodenSpearPikeRecipe;
import com.gestankbratwurst.ferocore.modules.rolemodule.recipes.WoodenSpearRecipe;
import com.gestankbratwurst.ferocore.util.UtilModule;
import com.gestankbratwurst.ferocore.util.common.NameSpaceFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 02.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RoleModule implements BaseModule {

  private static NamespacedKey SPEAR_KEY;
  private static NamespacedKey ALCHEMIST_KEY;
  private static NamespacedKey SHAMAN_KEY;

  public static ItemStack tagAsAlchemistStaff(final ItemStack itemStack) {
    final ItemMeta meta = itemStack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(ALCHEMIST_KEY, PersistentDataType.INTEGER, 1);
    itemStack.setItemMeta(meta);
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(itemStack, "ALCHEMIST_STAFF");
    return itemStack;
  }

  public static boolean isAlchemistStaff(final ItemStack itemStack) {
    if (itemStack == null || itemStack.getType() == Material.AIR) {
      return false;
    }
    final ItemMeta meta = itemStack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    return container.has(ALCHEMIST_KEY, PersistentDataType.INTEGER);
  }

  public static ItemStack tagAsShamanStaff(final ItemStack itemStack) {
    final ItemMeta meta = itemStack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(SHAMAN_KEY, PersistentDataType.INTEGER, 1);
    itemStack.setItemMeta(meta);
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(itemStack, "SHAMAN_STAFF");
    return itemStack;
  }

  public static boolean isShamanStaff(final ItemStack itemStack) {
    if (itemStack == null || itemStack.getType() == Material.AIR) {
      return false;
    }
    final ItemMeta meta = itemStack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    return container.has(SHAMAN_KEY, PersistentDataType.INTEGER);
  }

  public static ItemStack tagAsSpear(final ItemStack itemStack) {
    final ItemMeta meta = itemStack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(SPEAR_KEY, PersistentDataType.INTEGER, 1);
    itemStack.setItemMeta(meta);
    FeroCore.getModule(CustomItemModule.class).getCustomItemManager().tagItem(itemStack, "SPEAR");
    return itemStack;
  }

  public static boolean isSpear(final ItemStack itemStack) {
    if (itemStack == null || itemStack.getType() == Material.AIR) {
      return false;
    }
    final ItemMeta meta = itemStack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    return container.has(SPEAR_KEY, PersistentDataType.INTEGER);
  }

  @Override
  public void enable(final FeroCore plugin) {
    SPEAR_KEY = NameSpaceFactory.provide("SPEAR");
    ALCHEMIST_KEY = NameSpaceFactory.provide("ALCHEMIST_STAFF");
    SHAMAN_KEY = NameSpaceFactory.provide("SHAMAN_STAFF");

    final CustomItemManager customItemManager = FeroCore.getModule(CustomItemModule.class).getCustomItemManager();
    customItemManager.registerHandle(new AlchemistStaffItemHandle());
    customItemManager.registerHandle(new ShamanStaffItemHandle());
    customItemManager.registerHandle(new SpearItemHandle());

    FeroCore.registerListener(new RoleListener(FeroCore.getModule(UtilModule.class).getHologramManager()));

    final CustomRecipeManager recipeManager = FeroCore.getModule(CustomRecipeModule.class).getCustomRecipeManager();
    recipeManager.registerShapedRecipe(new WoodenSpearPikeRecipe());
    recipeManager.registerShapedRecipe(new StoneSpearPikeRecipe());
    recipeManager.registerShapedRecipe(new GoldenSpearPikeRecipe());
    recipeManager.registerShapedRecipe(new IronSpearPikeRecipe());
    recipeManager.registerShapedRecipe(new DiamondSpearPikeRecipe());
    recipeManager.registerShapedRecipe(new NetheriteSpearPikeRecipe());
    recipeManager.registerShapedRecipe(new ShamanStaffHeadRecipe());
    recipeManager.registerShapedRecipe(new AlchemistStaffHeadRecipe());

    recipeManager.registerShapedRecipe(new WoodenSpearRecipe());
    recipeManager.registerShapedRecipe(new StoneSpearRecipe());
    recipeManager.registerShapedRecipe(new GoldenSpearRecipe());
    recipeManager.registerShapedRecipe(new IronSpearRecipe());
    recipeManager.registerShapedRecipe(new DiamondSpearRecipe());
    recipeManager.registerShapedRecipe(new NetheriteSpearRecipe());
    recipeManager.registerShapedRecipe(new AlchemistStaffRecipe());
    recipeManager.registerShapedRecipe(new ShamanStaffRecipe());
  }

  @Override
  public void disable(final FeroCore plugin) {

  }
}
