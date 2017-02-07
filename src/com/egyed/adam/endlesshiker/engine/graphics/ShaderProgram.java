package com.egyed.adam.endlesshiker.engine.graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Adam on 5/12/16.
 * Handles shader program creation and compilation
 */
public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String,Integer> uniforms;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();

        if (programId == 0) {
            throw new Exception("Could not create shader");
        }

        uniforms = new HashMap<>();

    }

    public void createVertexShader(String shaderCode) throws Exception {

        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);

    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }


    protected int createShader(String shaderCode, int shaderType) throws Exception {

        // Create shader of its respective type
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            throw new Exception("Error creating shader. Code: " + shaderId);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: " + glGetShaderInfoLog(shaderId));
        }

        glAttachShader(programId, shaderId);

        return shaderId;

    }

    public void link() throws Exception {

        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: "+glGetShaderInfoLog(programId));
        }

        // Validating shader code
        // Should be removed before shipping
        /*
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: "+glGetShaderInfoLog(programId));
        }*/


    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {

        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }

            glDeleteProgram(programId);
        }

    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception ("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    private static Object glUniformMatrixLockObject = new Object();

    public void setUniform(String uniformName, Matrix4f value) {
        // Put matrix into a FloatBuffer
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        value.get(floatBuffer);
        synchronized (glUniformMatrixLockObject) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, floatBuffer);
        }

    }
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

}
