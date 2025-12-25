#version 330 core

in vec3 vNormal;
in vec3 vColor;

out vec4 FragColor;

void main() {
    vec3 lightDir = normalize(vec3(0.7, 2.0, 1.2));

    vec3 N = normalize(vNormal);
    vec3 L = normalize(lightDir);
    float diff = max(dot(N, L), 0.2);

    FragColor = vec4(vColor * diff, 1.0);
}
