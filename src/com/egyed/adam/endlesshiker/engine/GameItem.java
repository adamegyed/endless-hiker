package com.egyed.adam.endlesshiker.engine;

import com.egyed.adam.endlesshiker.engine.graphics.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Adam on 5/18/16.
 * Utilities for manipulating a Mesh as an item with rotations and position
 */
public class GameItem {

  private final Mesh mesh;

  private final Vector3f position;

  private float scale;

  private Matrix4f rotation;

  public GameItem(Mesh mesh) {
    this.mesh = mesh;
    position = new Vector3f(0, 0, 0);
    scale = 1;
    rotation = new Matrix4f();
    rotation.identity();
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(float x, float y, float z) {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
  }

  public void addPosition(float x, float y, float z) {
    this.position.x += x;
    this.position.y += y;
    this.position.z += z;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public Matrix4f getRotationMatrix() {
    return rotation;
  }

  /**
   * Cannot find a way to get x,y,z rotation from the matrix - this does not work
   */
  @Deprecated
  public Vector3f getRotationVector() {
    Vector3f rotationVector = new Vector3f();
    rotation.getEulerAnglesZYX(rotationVector);

    float z = rotationVector.z;

    rotationVector.z = rotationVector.x;
    rotationVector.x = z;

    return rotationVector;
  }


  public void setRotation(float x, float y, float z) {
    this.rotation = new Matrix4f();
    this.rotation.mul(getRotationMultiplier(x,y,z));
  }

  public void setRotation(Vector3f rot) {
    this.rotation = new Matrix4f();
    this.rotation.mul(getRotationMultiplier(rot.x, rot.y, rot.z));
  }

  public void addRotation(Matrix4f additionalRotation) {
    Matrix4f additional = new Matrix4f(additionalRotation);
    this.rotation = additional.mul(this.rotation);
  }

  public void addRotation(float x, float y, float z) {
    Matrix4f additionalRotation = getRotationMultiplier(x,y,z);
    this.rotation = additionalRotation.mul(this.rotation);

  }

  public Mesh getMesh() {
    return mesh;
  }

  /**
   * Get the additional rotation matrix, which should be multiplied by the current rotation matrix to get the new rotation matrix
   */
  public static Matrix4f getRotationMultiplier(float x, float y, float z) {

    float rotateXAngle = (float) Math.toRadians(x);
    float rotateYAngle = (float) Math.toRadians(y);
    float rotateZAngle = (float) Math.toRadians(z);

    Matrix4f rotationUpdate = new Matrix4f().identity();

    if (rotateXAngle!=0) {
      Matrix4f xRotation = new Matrix4f().identity();
      float sinAlpha = (float) Math.sin(rotateXAngle);
      float cosAlpha = (float) Math.cos(rotateXAngle);
      xRotation.m11(cosAlpha);
      xRotation.m12(-sinAlpha);
      xRotation.m21(sinAlpha);
      xRotation.m22(cosAlpha);

      rotationUpdate.mul(xRotation);
    }
    if (rotateYAngle!=0) {
      Matrix4f yRotation = new Matrix4f().identity();
      float sinBeta = (float) Math.sin(rotateYAngle);
      float cosBeta = (float) Math.cos(rotateYAngle);
      yRotation.m00(cosBeta);
      yRotation.m02(sinBeta);
      yRotation.m20(-sinBeta);
      yRotation.m22(cosBeta);

      rotationUpdate.mul(yRotation);
    }
    if (rotateZAngle!=0) {
      Matrix4f zRotation = new Matrix4f().identity();
      float sinGamma = (float) Math.sin(rotateZAngle);
      float cosGamma = (float) Math.cos(rotateZAngle);
      zRotation.m00(cosGamma);
      zRotation.m01(-sinGamma);
      zRotation.m10(sinGamma);
      zRotation.m11(cosGamma);

      rotationUpdate.mul(zRotation);
    }

    return rotationUpdate;


  }

}