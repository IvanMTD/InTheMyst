#version 430

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

in vec2 texture_coord;

void main() {
    fragment_color = vec4(0.2f,0.4f,0.5f,1.0f);
    select_color = vec4(0.0f,0.0f,0.0f,1.0f);
    bright_color = vec4(0.0f,0.0f,0.0f,1.0f);
}