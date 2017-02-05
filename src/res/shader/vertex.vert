#version 330

// Vertex Shader

layout (location=0) in vec3 pos;
layout (location=1) in vec3 inColor;

out vec3 exColor;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main() {

// Append 4th dimension - needed for simultaneous affine and linear transformations

gl_Position = projectionMatrix * modelViewMatrix * vec4 (pos, 1.0);

exColor = inColor;

}