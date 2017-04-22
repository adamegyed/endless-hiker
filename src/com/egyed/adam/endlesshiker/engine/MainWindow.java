package com.egyed.adam.endlesshiker.engine;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Adam on 5/10/16.
 * Handle creating and maintaining a GLFW window
 */
public class MainWindow {

  // Window title
  private String title;
  private WindowOptions opts;
  private int width;
  private int height;
  // Identifier for GLFW
  private long windowHandle;

  // Window callbacks
  private GLFWErrorCallback errorCallback;
  private GLFWKeyCallback keyCallback;
  private GLFWWindowSizeCallback windowSizeCallback;

  private boolean resized;
  private boolean vSync;
  private boolean shouldCameraReset;

  public MainWindow(String title, int width, int height, boolean vSync) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.vSync = vSync;
    this.resized = false;
    shouldCameraReset = false;

  }

  public void init() {

    // Create error callback to print any error messages to system.err
    errorCallback = GLFWErrorCallback.createPrint(System.err);
    glfwSetErrorCallback(errorCallback);

    // Initialize GLFW
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Window Hints

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

    // Window Creation
    // Last two parameters are which monitor and which window to share resources with
    // monitor left at NULL for windowed mode, share left at NULL for no shared resources
    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

    if (windowHandle == NULL) {
      throw new RuntimeException("Failed to create GLFW Window");
    }


    // Construct and set window resize callback
    windowSizeCallback = new GLFWWindowSizeCallback() {
      @Override
      public void invoke(long window, int width, int height) {
        MainWindow.this.width = width;
        MainWindow.this.height = height;
        MainWindow.this.setResized(true);
      }
    };
    glfwSetWindowSizeCallback(windowHandle, windowSizeCallback);

    // Construct and set key callback
    // Define behavior of keypresses
    keyCallback = new GLFWKeyCallback() {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods) {

        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetWindowShouldClose(windowHandle, true);
        }
        if (key == GLFW_KEY_R && action == GLFW_RELEASE) {
          shouldCameraReset = true;
        }

      }
    };
    glfwSetKeyCallback(windowHandle, keyCallback);

    // Get resolution of the primary monitor
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

    // Center window
    glfwSetWindowPos(windowHandle,
        (vidmode.width() - width) / 2,
        (vidmode.height() - height) / 2 );

    // Make OpenGL context current
    glfwMakeContextCurrent(windowHandle);

    // Enable vsync
    if (vSync) {
      glfwSwapInterval(1);
    }

    // Make window visible
    glfwShowWindow(windowHandle);

    GL.createCapabilities();

    glEnable(GL_DEPTH_TEST);
    glEnable(GL_STENCIL_TEST);
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);
    //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);


    /*
    if (opts.antialiasing) {
      glfwWindowHint(GLFW_SAMPLES, 4);
    }
    */


  }
  public static class WindowOptions {
    public boolean cullFace;
    public boolean showTriangles;
    public boolean showFps;
    public boolean compatibleProfile;
    public boolean antialiasing;
    public boolean frustumCulling;
  }

  public void setClearColor(float r, float g, float b, float alpha) {
    glClearColor(r, g, b, alpha);
  }

  /**
   * Get whether or not a given key is pressed at the moment
   */
  public boolean isKeyPressed(int keyCode) {
    return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
  }

  public boolean isKeyReleased(int keyCode) {
    return glfwGetKey(windowHandle, keyCode) == GLFW_RELEASE;
  }

  public boolean windowShouldClose() {
    return glfwWindowShouldClose(windowHandle);
  }

  public String getTitle() {
    return title;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isResized() {
    return resized;
  }

  public void setResized(boolean resized) {
    this.resized = resized;
  }

  public boolean isvSync() {
    return vSync;
  }

  public void setvSync(boolean vSync) {
    this.vSync = vSync;
  }

  public void update() {
    glfwSwapBuffers(windowHandle);
    glfwPollEvents();
  }

  public long getWindowHandle() {
    return windowHandle;
  }

  public boolean getShouldCameraReset() {
    return shouldCameraReset;
  }

  public void setShouldCameraReset(boolean shouldCameraReset) {
    this.shouldCameraReset = shouldCameraReset;
  }

  public void restoreState() {
    /*
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_STENCIL_TEST);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    if (opts.cullFace) {
      glEnable(GL_CULL_FACE);
      glCullFace(GL_BACK);
    }
    */
  }

  public WindowOptions getOptions() {
    return opts;
  }

}