package com.egyed.adam.endlesshiker.engine.graphics;

import org.joml.Vector3f;

/**
 * Created by Adam on 5/18/16.
 * Location tracking for a pseudo-camera
 */
public class Camera {

  private final Vector3f position;

  private final Vector3f rotation;

  public Camera() {
    position = new Vector3f(0, 0, 0);
    rotation = new Vector3f(0, 0, 0);
  }

  public Camera(Vector3f position, Vector3f rotation) {
    this.position = position;
    this.rotation = rotation;
  }

  public Vector3f getPosition() {
    return position;
  }

  public Vector3f getRotation() {
    return rotation;
  }

  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }

  public void setPosition(Vector3f pos) {
    position.x = pos.x;
    position.y = pos.y;
    position.z = pos.z;
  }

  public void setRotation(float x, float y, float z) {
    rotation.x = x;
    rotation.y = y;
    rotation.z = z;
  }

  public void setRotation(Vector3f rot) {
    rotation.x = rot.x;
    rotation.y = rot.y;
    rotation.z = rot.z;
  }

  public void movePosition(float offsetX, float offsetY, float offsetZ) {
    if ( offsetZ != 0 ) {
      position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
      position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
    }
    if ( offsetX != 0) {
      position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
      position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
    }
    position.y += offsetY;
  }

  public void moveRotation(float offsetX, float offsetY, float offsetZ) {
    rotation.x += offsetX;
    rotation.y += offsetY;
    rotation.y += offsetZ;
  }
}