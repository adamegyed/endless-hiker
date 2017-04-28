package com.egyed.adam.endlesshiker.engine.graphics;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

public class ModelMesh {
        private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

        private final int vaoId;

        private final List<Integer> vboIdList;

        private final int vertexCount;

        private Texture texture;

        private Vector3f colour;

        public ModelMesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
            FloatBuffer posBuffer = null;
            FloatBuffer textCoordsBuffer = null;
            FloatBuffer vecNormalsBuffer = null;
            IntBuffer indicesBuffer = null;
            try {
                colour = DEFAULT_COLOUR;
                vertexCount = indices.length;
                vboIdList = new ArrayList();

                vaoId = glGenVertexArrays();
                glBindVertexArray(vaoId);

                // Position VBO
                int vboId = glGenBuffers();
                vboIdList.add(vboId);
                posBuffer = MemoryUtil.memAllocFloat(positions.length);
                posBuffer.put(positions).flip();
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

                // Texture coordinates VBO
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
                textCoordsBuffer.put(textCoords).flip();
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

                // Vertex normals VBO
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
                vecNormalsBuffer.put(normals).flip();
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

                // Index VBO
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                indicesBuffer = MemoryUtil.memAllocInt(indices.length);
                indicesBuffer.put(indices).flip();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

                glBindBuffer(GL_ARRAY_BUFFER, 0);
                glBindVertexArray(0);
            } finally {
                if (posBuffer != null) {
                    MemoryUtil.memFree(posBuffer);
                }
                if (textCoordsBuffer != null) {
                    MemoryUtil.memFree(textCoordsBuffer);
                }
                if (vecNormalsBuffer != null) {
                    MemoryUtil.memFree(vecNormalsBuffer);
                }
                if (indicesBuffer != null) {
                    MemoryUtil.memFree(indicesBuffer);
                }
            }
        }

        public boolean isTextured() {
            return this.texture != null;
        }

        public Texture getTexture() {
            return this.texture;
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }

        public void setColour(Vector3f colour) {
            this.colour = colour;
        }

        public Vector3f getColour() {
            return this.colour;
        }

        public int getVaoId() {
            return vaoId;
        }

        public int getVertexCount() {
            return vertexCount;
        }

        public void render() {
            if (texture != null) {
                // Activate firs texture bank
                glActiveTexture(GL_TEXTURE0);
                // Bind the texture
                glBindTexture(GL_TEXTURE_2D, texture.getId());
            }

            // Draw the mesh
            glBindVertexArray(getVaoId());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

            // Restore state
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glBindVertexArray(0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        public void cleanUp() {
            glDisableVertexAttribArray(0);

            // Delete the VBOs
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            for (int vboId : vboIdList) {
                glDeleteBuffers(vboId);
            }

            // Delete the texture
            if (texture != null) {
                texture.cleanup();
            }

            // Delete the VAO
            glBindVertexArray(0);
            glDeleteVertexArrays(vaoId);
        }
    }
