#version 430 core

out vec4 FragColor;

in vec3 TexCoords;

uniform samplerCube skybox;

void main(){
    vec4 color = texture(skybox, TexCoords);
    FragColor = color - 0.1f;
}
