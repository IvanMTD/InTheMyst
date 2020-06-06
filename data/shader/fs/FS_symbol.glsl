#version 430 core

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;

uniform sampler2D image;
uniform vec3 color;
uniform int empty;

in vec2 texCoord;

void main() {
    vec4 rgba = texture(image,texCoord);
    if(rgba.a < 1.0f || empty == 1){
        discard;
    }
    vec3 rgb = vec3(rgba);
    vec3 mixColor = vec3(rgb.r + color.r,rgb.g + color.g,rgb.b + color.b);
    fragment_color = vec4(mixColor,1.0f);
    select_color = vec4(0.0f,0.0f,0.0f,0.0f);
}
