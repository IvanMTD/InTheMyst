#version 430 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tex;

out vec2 texture_coord;
out vec2 fog_tex_coord;

void main(){
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
    texture_coord = tex;
    fog_tex_coord = vec2(tex.x * 3, tex.y * 2);
}