#version 330

// Fragment Shader

in vec3 exColor;
in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {

// Set color
    fragColor = vec4(exColor, 1.0);
    fragColor = texture(texture_sampler, outTexCoord);
}
