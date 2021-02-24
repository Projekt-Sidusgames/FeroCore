package com.gestankbratwurst.ferocore.resourcepack.sounds;

import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomSoundEffect;
import net.minecraft.server.v1_16_R3.SoundCategory;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 26.04.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public enum CustomSound {

  COINS_SOUND(),
  PEACE_SOUND(),
  TRUMPET(),
  DARK_WAVE(),
  DARK_WAVE_HIT(),
  ORC_HORN(),
  ORC_CRY(),
  HEART_BEAT_10S(),
  UNDEAD_THEME(),
  ORC_THEME(),
  ELF_THEME(),
  HUMAN_THEME(),
  DWARF_THEME(),
  WAR_HORN();

  private MinecraftKey key = null;

  public void playHeadArtificialDist(final Location location, final double radius, final float pitch,
      final SoundCategory soundCategory) {
    final double maxSquared = -(radius * radius);
    location.getNearbyPlayers(radius).forEach(
        player -> this.play(player, location, soundCategory, this.calcVolumeSquared(location, player.getLocation(), maxSquared), pitch));
  }

  private float calcVolumeSquared(final Location soundPoint, final Location distPoint, final double maxSq) {
    final double distSq = soundPoint.distanceSquared(distPoint);
    return (float) (1.0 - (distSq / maxSq));
  }

  public void play(final Player player) {
    this.play(player, player.getEyeLocation(), SoundCategory.NEUTRAL, 1F, 1F);
  }

  public void play(final Player player, final Location location) {
    this.play(player, location, SoundCategory.NEUTRAL, 1F, 1F);
  }

  public void play(final Player player, final Location location, final float volume, final float pitch) {
    this.play(player, location, SoundCategory.NEUTRAL, volume, pitch);
  }

  public void play(final Player player, final Location location, final SoundCategory soundCategory) {
    this.play(player, location, soundCategory, 1F, 1F);
  }

  public void play(final Player player, final SoundCategory soundCategory, final float volume, final float pitch) {
    this.play(player, player.getEyeLocation(), soundCategory, volume, pitch);
  }

  public void play(final Player player, final float volume, final float pitch) {
    this.play(player, player.getEyeLocation(), SoundCategory.NEUTRAL, volume, pitch);
  }

  public void playAt(final Location location, final org.bukkit.SoundCategory soundCategory, final float volume, final float pitch) {
    if (this.key == null) {
      this.key = new MinecraftKey("custom." + this.toString().toLowerCase());
    }
    location.getWorld().playSound(location, this.key.getKey(), soundCategory, volume, pitch);
  }

  public void play(final Player player, final Location location, final SoundCategory soundCategory, final float volume, final float pitch) {
    if (this.key == null) {
      this.key = new MinecraftKey("custom." + this.toString().toLowerCase());
    }
    final Vec3D vec = new Vec3D(location.getX(), location.getY(), location.getZ());
    final PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(this.key, soundCategory, vec, volume, pitch);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

}
