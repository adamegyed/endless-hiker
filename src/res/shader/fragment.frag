#version 330

// Fragment Shader

// in vec3 exColor; not used
in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {

// Set color
    // fragColor = vec4(exColor, 1.0); old
    fragColor = texture(texture_sampler, outTexCoord);
}
