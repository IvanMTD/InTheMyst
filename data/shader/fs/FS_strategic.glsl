#version 430 core

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

in VS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
} fs_in;

uniform sampler2D blendMap;

void main() {
    fragment_color = texture(blendMap,fs_in.mapTextureCoord);
    select_color = vec4(0.0f,0.0f,0.0f,0.0f);
    bright_color = vec4(0.0f,0.0f,0.0f,0.0f);
}
