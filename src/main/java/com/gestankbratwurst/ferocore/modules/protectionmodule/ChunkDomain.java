package com.gestankbratwurst.ferocore.modules.protectionmodule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 01.07.2020
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ChunkDomain {

  public ChunkDomain() {
    this.regions = new TreeSet<>();
  }

  private final SortedSet<ProtectedRegion> regions;

  public void addRegion(final ProtectedRegion region) {
    this.regions.add(region);
  }

  public ProtectedRegion getHighestPriorityRegion(final Vector vector) {
    for (final ProtectedRegion region : this.regions) {
      if (region.getRegionBox().contains(vector)) {
        return region;
      }
    }
    return null;
  }

  public List<ProtectedRegion> listRegions() {
    final ArrayList<ProtectedRegion> regionList = new ArrayList<>(this.regions);
    Collections.sort(regionList);
    return regionList;
  }

}