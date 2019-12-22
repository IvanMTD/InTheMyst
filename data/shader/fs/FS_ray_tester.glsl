#version 430

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

uniform vec3 color;

void main() {
    fragment_color = vec4(color,1.0f);
    select_color = vec4(0.0f,0.0f,0.0f,0.0f);
    bright_color = vec4(0.0f,0.0f,0.0f,0.0f);
}
