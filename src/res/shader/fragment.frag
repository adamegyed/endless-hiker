#version 330

// Fragment Shader

in vec3 exColor;

out vec4 fragColor;

void main() {

// Set color
fragColor = vec4(exColor, 1.0);

}
