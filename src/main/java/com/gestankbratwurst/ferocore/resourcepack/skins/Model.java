package com.gestankbratwurst.ferocore.resourcepack.skins;

import com.gestankbratwurst.ferocore.resourcepack.packing.BoxedFontChar;
import com.gestankbratwurst.ferocore.util.common.UtilItem;
import com.gestankbratwurst.ferocore.util.items.ItemBuilder;
import com.gestankbratwurst.ferocore.util.nbtapi.NBTItem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import net.crytec.libs.protocol.skinclient.data.Skin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 24.11.2019
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public enum Model {

  BLACK_ARROW_DOWN(Material.STICK, 1000, false, false),
  BLACK_ARROW_LEFT(Material.STICK, 1001, false, false),
  BLACK_ARROW_RIGHT(Material.STICK, 1002, false, false),
  BLACK_ARROW_UP(Material.STICK, 1003, false, false),
  GREEN_CHECK(Material.STICK, 1004, false, false),
  RED_X(Material.STICK, 1005, false, false),
  DOUBLE_GRAY_ARROW_UP(Material.STICK, 1006, false, false),
  DOUBLE_GRAY_ARROW_DOWN(Material.STICK, 1007, false, false),
  DOUBLE_GRAY_ARROW_LEFT(Material.STICK, 1008, false, false),
  DOUBLE_GRAY_ARROW_RIGHT(Material.STICK, 1009, false, false),
  DOUBLE_RED_ARROW_UP(Material.STICK, 1010, false, false),
  DOUBLE_GREEN_ARROW_DOWN(Material.STICK, 1011, false, false),
  GREEN_PLUS(Material.STICK, 1012, false, false),
  SCROLL(Material.STICK, 1013, false, false),

  LETTER_NO(Material.STICK, 1014, false, false),
  LETTER_YES(Material.STICK, 1015, false, false),
  LOCK_OPEN(Material.STICK, 1016, false, false),
  LOCK_CLOSED(Material.STICK, 1017, false, false),

  HUMAN_ICON(Material.STICK, 1018, false, false),
  ORC_ICON(Material.STICK, 1019, false, false),
  DWARF_ICON(Material.STICK, 1020, false, false),
  ELF_ICON(Material.STICK, 1021, false, false),
  UNDEAD_ICON(Material.STICK, 1022, false, false),
  WAR_ICON(Material.STICK, 1023, false, false),
  PEACE_ICON(Material.STICK, 1024, false, false),
  BROWN_BOOK(Material.STICK, 1025, false, false),
  HORN_ICON(Material.STICK, 1026, false, false),
  ELF_ORB(Material.STICK, 1027, false, false),
  CEMENT_BALL(Material.STICK, 1028, false, false),

  GOLD_PILE_TINY(Material.STICK, 1100, false, true),
  GOLD_PILE_SMALL(Material.STICK, 1101, false, true),
  GOLD_PILE_MEDIUM(Material.STICK, 1102, false, true),
  GOLD_PILE_BIG(Material.STICK, 1103, false, true),
  GOLD_PILE_HUGE(Material.STICK, 1104, false, true),
  GOLD_PILE_BAR_SMALL(Material.STICK, 1105, false, true),
  GOLD_PILE_BAR_MEDIUM(Material.STICK, 1106, false, true),
  GOLD_PILE_BAR_BIG(Material.STICK, 1107, false, true),
  SILVER_PILE_TINY(Material.STICK, 1108, false, true),
  SILVER_PILE_SMALL(Material.STICK, 1109, false, true),
  SILVER_PILE_MEDIUM(Material.STICK, 1110, false, true),
  SILVER_PILE_BIG(Material.STICK, 1111, false, true),
  SILVER_PILE_HUGE(Material.STICK, 1112, false, true),
  COPPER_PILE_TINY(Material.STICK, 1113, false, true),
  COPPER_PILE_SMALL(Material.STICK, 1114, false, true),
  COPPER_PILE_MEDIUM(Material.STICK, 1115, false, true),
  COPPER_PILE_BIG(Material.STICK, 1116, false, true),
  COPPER_PILE_HUGE(Material.STICK, 1117, false, true),
  COINS_GOLD_0(Material.STICK, 1118, false, false),
  COINS_GOLD_1(Material.STICK, 1119, false, false),
  COINS_GOLD_2(Material.STICK, 1120, false, false),
  COINS_GOLD_3(Material.STICK, 1121, false, false),
  COINS_GOLD_4(Material.STICK, 1122, false, false),
  COINS_GOLD_5(Material.STICK, 1123, false, false),
  COINS_SILVER_0(Material.STICK, 1124, false, false),
  COINS_SILVER_1(Material.STICK, 1125, false, false),
  COINS_SILVER_2(Material.STICK, 1126, false, false),
  COINS_SILVER_3(Material.STICK, 1127, false, false),
  COINS_SILVER_4(Material.STICK, 1128, false, false),
  COINS_SILVER_5(Material.STICK, 1129, false, false),
  COINS_COPPER_0(Material.STICK, 1130, false, false),
  COINS_COPPER_1(Material.STICK, 1131, false, false),
  COINS_COPPER_2(Material.STICK, 1132, false, false),
  COINS_COPPER_3(Material.STICK, 1133, false, false),
  COINS_COPPER_4(Material.STICK, 1134, false, false),
  COINS_COPPER_5(Material.STICK, 1135, false, false),
  BARS_GOLD_0(Material.STICK, 1136, false, false),
  BARS_GOLD_1(Material.STICK, 1137, false, false),
  BARS_GOLD_2(Material.STICK, 1138, false, false),

  DWARF_CANON(Material.STICK, 1139, false, true),
  HUMAN_CROWN(Material.STICK, 1140, false, true),
  DWARF_CROWN(Material.STICK, 1141, false, true),
  ELF_CROWN(Material.STICK, 1142, false, true),
  UNDEAD_CROWN(Material.STICK, 1143, false, true),
  CANON_BALL(Material.STICK, 1144, false, true),
  HEALING_FOUNTAIN(Material.STICK, 1145, false, true),
  ELF_ORB_MODEL(Material.STICK, 1146, false, true),
  UNDEAD_TOTEM(Material.STICK, 1147, false, true),
  ORC_HORN(Material.STICK, 1148, false, true),

  TRADE_GUI(Material.STICK, 2000, false, true),
  ADMIN_SHOP_BORDER(Material.STICK, 2001, false, true);

  Model(final Material baseMaterial, final int modelID, final boolean headEnabled, final boolean customModelDataEnabled) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = ModelData.defaultGenerated();
    this.fontMeta = FontMeta.common();
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
  }

  Model(final Material baseMaterial, final int modelID, final ModelData modelData, final FontMeta fontMeta, final boolean headEnabled,
      final boolean customModelDataEnabled) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = modelData;
    this.fontMeta = fontMeta;
    this.boxedFontChar = new BoxedFontChar();
    this.headSkinEnabled = headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
  }

  @Getter
  private final Material baseMaterial;
  @Getter
  private final int modelID;
  @Getter
  private final ModelData modelData;
  @Getter
  private final FontMeta fontMeta;
  @Getter
  private final BoxedFontChar boxedFontChar;
  @Getter
  private final boolean headSkinEnabled;
  @Getter
  private final boolean customModelDataEnabled;
  @Getter
  @Setter
  private Skin skin;
  @Getter
  @Setter
  private File linkedImageFile;
  @Getter
  private GameProfile gameProfile;

  private ItemStack head;

  private ItemStack item;

  private void initProfile() {
    if (this.gameProfile == null && this.skin != null) {
      this.gameProfile = new GameProfile(this.skin.data.uuid, this.skin.name);
      this.gameProfile.getProperties()
          .put("textures", new Property("textures", this.skin.data.texture.value, this.skin.data.texture.signature));
    }
  }

  public char getChar() {
    return this.boxedFontChar.getAsCharacter();
  }

  public ItemStack getItem() {
    if (this.item == null) {
      this.item = new ItemBuilder(this.baseMaterial)
          .modelData(this.modelID)
          .name(this.toString())
          .build();
      final NBTItem nbt = new NBTItem(this.item);
      nbt.setString("Model", this.toString());
      this.item = nbt.getItem();
    }
    return this.item.clone();
  }

  public ItemStack getHead() {
    if (this.head != null) {
      return this.head.clone();
    }
    this.initProfile();

    this.head = UtilItem.produceHead(this.gameProfile);

    final NBTItem nbt = new NBTItem(this.head);
    nbt.setString("ModelHead", this.toString());
    this.head = nbt.getItem();
    return this.head.clone();
  }

}