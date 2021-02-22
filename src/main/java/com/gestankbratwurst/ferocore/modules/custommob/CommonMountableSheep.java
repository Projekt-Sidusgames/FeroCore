package com.gestankbratwurst.ferocore.modules.custommob;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntitySheep;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumMoveType;
import net.minecraft.server.v1_16_R3.IJumpable;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.TagsFluid;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CommonMountableSheep extends EntitySheep {

  Player p;

  public CommonMountableSheep(final World world, final Player p) {
    super(EntityTypes.SHEEP, world);
    this.getWorld().addEntity(this);
    this.setNoAI(true);
    this.setSilent(true);
    this.p = p;
    if (jump == null) {
      try {
        jump = EntityLiving.class.getField("jumping");
      } catch (final NoSuchFieldException noSuchFieldException) {
        noSuchFieldException.printStackTrace();
      }
    }
  }

  protected double walkSpeed = 0.2F;
  protected boolean hasRider = false;
  protected boolean isFlying = false;
  protected float jumpPower = 0;
  private static Field jump;

  @Override
  public void g(final Vec3D vec3d) {
    if (this.getPassengers().isEmpty()) {
      final Entity entity = (Entity) this;
      entity.remove();
    }
    if (!this.isVehicle()) {
      super.e(vec3d);
      return;
    }

    if (this.onGround && this.isFlying) {
      this.isFlying = false;
      this.fallDistance = 0;
    }

    final EntityLiving passenger = (EntityLiving) this.getPassengers().get(0);

    if (this.a(TagsFluid.WATER)) {
      this.setMot(this.getMot().add(0, 0.4, 0));
    }

    // apply pitch & yaw
    this.lastYaw = (this.yaw = passenger.yaw);
    this.pitch = passenger.pitch * 0.5F;
    this.setYawPitch(this.yaw, this.pitch);
    this.as = (this.aA = this.yaw);
    final double motionSideways = passenger.aR * this.walkSpeed;
    double motionForward = passenger.aT * this.walkSpeed;
    // backwards is slower
    if (motionForward <= 0.0F) {
      motionForward *= 0.25F;
    }

    final float speed = 0.22222F * (1F + (5));
    double jumpHeight = this.jumpPower;
    this.ride(motionSideways, motionForward, vec3d.y, speed); // apply motion

    if (jump != null && this.isVehicle()) {
      boolean doJump = false;
      if (this instanceof IJumpable) {
        if (this.jumpPower > 0.0F) {
          doJump = true;
          this.jumpPower = 0.0F;
        } else if (!this.onGround && jump != null) {
          try {
            doJump = jump.getBoolean(passenger);
          } catch (final IllegalAccessException ignored) {
          }
        }
      } else {
        if (jump != null) {
          try {
            doJump = jump.getBoolean(passenger);
          } catch (final IllegalAccessException ignored) {
          }
        }
      }

      if (doJump) {
        if (this.onGround) {
          jumpHeight = new BigDecimal(jumpHeight).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
          Double jumpVelocity = 0.8;
          jumpVelocity = jumpVelocity == null ? 0.44161199999510264 : jumpVelocity;
          this.setMot(this.getMot().x, jumpVelocity, this.getMot().z);
        }
      }

    }
    super.a(vec3d);
  }


  private void ride(final double motionSideways, final double motionForward, final double motionUpwards, final float speedModifier) {
    InventoryClickEvent e;
    double locY;
    float f2;
    final float speed;
    final float swimSpeed;

    if (this.a(TagsFluid.WATER)) {
      locY = this.locY();
      speed = 0.8F;
      swimSpeed = 0.02F;

      this.a(swimSpeed, new Vec3D(motionSideways, motionUpwards, motionForward));
      this.move(EnumMoveType.SELF, this.getMot());
      final double motX = this.getMot().x * speed;
      double motY = this.getMot().y * 0.800000011920929D;
      final double motZ = this.getMot().z * speed;
      motY -= 0.02D;
      if (this.positionChanged && this.e(this.getMot().x,
          this.getMot().y + 0.6000000238418579D - this.locY() + locY, this.getMot().z)) {
        motY = 0.30000001192092896D;
      }
      this.setMot(motX, motY, motZ);
    } else if (this.a(TagsFluid.LAVA)) {
      locY = this.locY();
      this.a(2F, new Vec3D(motionSideways, motionUpwards, motionForward));
      this.move(EnumMoveType.SELF, this.getMot());
      final double motX = this.getMot().x * 0.5D;
      double motY = this.getMot().y * 0.5D;
      final double motZ = this.getMot().z * 0.5D;
      motY -= 0.02D;
      if (this.positionChanged && this.e(this.getMot().x,
          this.getMot().y + 0.6000000238418579D - this.locY() + locY, this.getMot().z)) {
        motY = 0.30000001192092896D;
      }
      this.setMot(motX, motY, motZ);
    } else {
      float friction = 0.91F;

      speed = speedModifier * (0.16277136F / (friction * friction * friction));

      this.a(speed, new Vec3D(motionSideways, motionUpwards, motionForward));
      friction = 0.91F;

      double motX = this.getMot().x;
      double motY = this.getMot().y;
      double motZ = this.getMot().z;

      if (this.isClimbing()) {
        swimSpeed = 0.15F;
        motX = MathHelper.a(motX, -swimSpeed, swimSpeed);
        motZ = MathHelper.a(motZ, -swimSpeed, swimSpeed);
        this.fallDistance = 0.0F;
        if (motY < -0.15D) {
          motY = -0.15D;
        }
      }

      final Vec3D mot = new Vec3D(motX, motY, motZ);

      this.move(EnumMoveType.SELF, mot);
      if (this.positionChanged && this.isClimbing()) {
        motY = 0.2D;
      }

      motY -= 0.08D;

      motY *= 0.9800000190734863D;
      motX *= friction;
      motZ *= friction;

      this.setMot(motX, motY, motZ);
    }

    this.as = this.aD;
    locY = this.locX() - this.lastX;
    final double d1 = this.locZ() - this.lastZ;
    f2 = MathHelper.sqrt(locY * locY + d1 * d1) * 4.0F;
    if (f2 > 1.0F) {
      f2 = 1.0F;
    }

    this.aD += (f2 - this.aD) * 0.4F;
    this.aE += this.aD;

  }

}
