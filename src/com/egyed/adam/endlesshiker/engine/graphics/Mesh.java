package com.egyed.adam.endlesshiker.engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE0;

/**
 * Created by Adam on 5/16/16.
 * Handles creation and rendering of meshes with a given color
 * Prepares VAO with 3 VBOs to hold Mesh's positions, texture cooridinates, and position index
 */
public class Mesh {

  private final int vaoId;

  private final int posVboId;

  //private final int colourVboId;

  private final int idxVboId;

  private final int vertexCount;

  private final Texture texture;
  //public Mesh(float[] positions, float[] colours, int[] indices) {
  public Mesh(float[] positions, float[] textCoords, int[] indices, Texture texture) {
    this.texture = texture;
    vertexCount = indices.length;

    vaoId = glGenVertexArrays();
    glBindVertexArray(vaoId);

    // Position VBO
    posVboId = glGenBuffers();
    FloatBuffer posBuffer = BufferUtils.createFloatBuffer(positions.length);
    FloatBuffer textCoordsBuffer = null;
    posBuffer.put(positions).flip();
    glBindBuffer(GL_ARRAY_BUFFER, posVboId);
    glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        /*
        // Colour VBO - Commented out when working on texture - Nishan
        colourVboId = glGenBuffers();
        FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colours.length);
        colourBuffer.put(colours).flip();
        glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
        glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        */
    //Texture VBO
    int vboId = glGenBuffers();
    textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
    textCoordsBuffer.put(textCoords).flip();
    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

    // Index VBO
    idxVboId = glGenBuffers();
    IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
    indicesBuffer.put(indices).flip();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  public int getVaoId() {
    return vaoId;
  }

  public int getVertexCount() {
    return vertexCount;
  }

  public void cleanUp() {
    texture.cleanup();
    glDisableVertexAttribArray(0);
    // Delete the VBOs
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glDeleteBuffers(posVboId);
    //glDeleteBuffers(colourVboId);
    glDeleteBuffers(idxVboId);

    // Delete the VAO
    glBindVertexArray(0);
    glDeleteVertexArrays(vaoId);
  }

  public void render() {
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texture.getId());

    // Draw the mesh
    glBindVertexArray(getVaoId());
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);

    glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

    // Restore state
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    glBindVertexArray(0);
  }
}
