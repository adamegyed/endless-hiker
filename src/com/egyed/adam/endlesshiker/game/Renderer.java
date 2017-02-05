package com.egyed.adam.endlesshiker.game;


import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.MainWindow;
import com.egyed.adam.endlesshiker.engine.Util;
import com.egyed.adam.endlesshiker.engine.graphics.Camera;
import com.egyed.adam.endlesshiker.engine.graphics.ShaderProgram;
import com.egyed.adam.endlesshiker.engine.graphics.Mesh;
import com.egyed.adam.endlesshiker.engine.graphics.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import sun.reflect.annotation.ExceptionProxy;

import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Adam on 5/11/16.
 * Handles OpenGL rendering for RubiksGame
 */
public class Renderer {

    private final static float FOV = (float) Math.toRadians(70.0f);

    private final static float Z_NEAR = 0.01f;

    private final static float Z_FAR = 1000.0f;

    private Matrix4f projectionMatrix;

    private int vboId;

    private int vaoId;

    private final Transformation transformation;

    ShaderProgram shaderProgram;

    public Renderer() {

        transformation = new Transformation();


    }

    public void init(MainWindow mainWindow) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Util.loadResource("/res/shader/vertex.vert"));
        shaderProgram.createFragmentShader(Util.loadResource("/res/shader/fragment.frag"));
        shaderProgram.link();

        // Create projection matrix
        float aspectRatio = (float) mainWindow.getWidth() / mainWindow.getHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(MainWindow window, Camera camera, GameItem[] gameItems) {
        clear();

        if ( window.isResized() ) {
          glViewport(0, 0, window.getWidth() * 2, window.getHeight() * 2);
          window.setResized(false);

          //glMatrixMode(GL_PROJECTION);
          //glLoadIdentity();
          //glOrtho(0, window.getWidth(), 0, window.getHeight(), -1, 1);
        }

        shaderProgram.bind();


        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Render each gameItem
        // Rendering is normally very cpu-intensive, so it is paralleled with a foreach over a stream of the gameItems

        Arrays.stream(gameItems).forEach( gameItem -> {

            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            // Render the mes for this game item
            gameItem.getMesh().render();
        });


        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }


}
