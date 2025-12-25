#version 330 core

//in vec3 vNormal;
//in vec3 vColor;
in vec2 vUV;

out vec4 FragColor;

uniform sampler2D uAtlas;

void main() {
    FragColor = texture(uAtlas, vUV);
}
