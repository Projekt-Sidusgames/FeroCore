package com.gestankbratwurst.ferocore.modules.skillmodule;

import java.util.function.Consumer;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 17.02.2021
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class SkillShot implements TickableSkill {

  public SkillShot(final Location startLoc, final double speed) {
    this(startLoc, speed, true);
  }

  public SkillShot(final Location startLoc, final double speed, final boolean ignorePassableBlocks) {
    this(startLoc, speed, ignorePassableBlocks, 0.5D);
  }

  public SkillShot(final Location startLoc, final double speed, final boolean ignorePassableBlocks, final double raySize) {
    this.location = startLoc;
    this.ignorePassableBlocks = ignorePassableBlocks;
    this.raySize = raySize;
    this.direction = startLoc.getDirection().multiply(speed);
    this.speed = this.direction.length();
  }

  private int ticksLived = 0;
  private boolean directionChanged = false;
  private final Location location;
  private boolean done = false;
  private final boolean ignorePassableBlocks;
  private final double raySize;
  private final Vector direction;
  private double speed;

  public void start() {
    SkillModule.addTickableSkill(this);
  }

  private void detectDirectionChanges() {
    if (this.directionChanged) {
      this.directionChanged = false;
      this.speed = this.direction.length();
    }
  }

  public void changeDirection(final Consumer<Vector> directionConsumer) {
    directionConsumer.accept(this.direction);
    this.directionChanged = true;
  }

  @Override
  public void tick() {
    if (this.ticksLived++ == this.getMaxTicksAlive()) {
      this.onRunout(this.location);
      this.done = true;
      return;
    }
    this.proceedFlight();
  }

  @Override
  public boolean isDone() {
    return this.done;
  }

  private void proceedFlight() {
    this.location.add(this.direction);
    this.onFlyTick(this.location.clone());
    final RayTraceResult traceResult = this.location.getWorld()
        .rayTrace(this.location, this.direction, this.speed, FluidCollisionMode.NEVER, this.ignorePassableBlocks, this.raySize,
            this::isValidTarget);

    if (traceResult == null) {
      return;
    }

    final Entity hitEntity = traceResult.getHitEntity();
    if (hitEntity != null) {
      this.done = this.onEntityImpact(hitEntity, traceResult.getHitPosition());
      return;
    }

    final Block hitBlock = traceResult.getHitBlock();
    if (hitBlock != null) {
      this.done = this.onBlockImpact(hitBlock, traceResult.getHitBlockFace());
      return;
    }
  }

  protected abstract boolean onBlockImpact(Block block, BlockFace face);

  protected abstract boolean onEntityImpact(Entity entity, Vector exactPosition);

  protected abstract void onRunout(Location location);

  protected abstract void onFlyTick(Location location);

  protected abstract int getMaxTicksAlive();

  protected abstract boolean isValidTarget(Entity entity);

}