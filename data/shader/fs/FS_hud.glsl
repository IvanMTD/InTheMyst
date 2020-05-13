#version 430 core

#define R 131073
#define G 131074
#define B 131075
#define A 131076

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

in vec2 texture_coord;

// attenuation control
uniform int tune;
uniform float gamma;
// target highlight
uniform int onTarget;
// texture
uniform sampler2D image;
uniform int group;
uniform float id;
// control
uniform int discardControl;

vec4 attenuation();
vec4 targetHighlight();

void main() {
    if(tune == 1){
        fragment_color = attenuation();
    }else if(onTarget == 1){
        fragment_color = targetHighlight();
    }else{
        fragment_color = texture(image, texture_coord);
    }

    if(discardControl == 1){
        if(fragment_color.a < 1.0f){
            discard;
        }
    }

    vec4 rgba = texture(image,texture_coord);
    if(group == R){
        select_color = vec4(id, 0.0f, 0.0f, 1.0f);
    }else if(group == G){
        select_color = vec4(0.0f, id, 0.0f, 1.0f);
    }else if(group == B){
        select_color = vec4(0.0f, 0.0f, id, rgba.a);
    }else if(group == A){
        select_color = vec4(0.0f, 0.0f, 0.0f, id);
    }

    bright_color = vec4(0.0f,0.0f,0.0f,1.0f);
}

vec4 attenuation(){
    vec4 correct_image = texture(image, texture_coord);
    float alpha = correct_image.a;
    vec3 result = correct_image.rgb;
    result = result * (pow(gamma,4));
    return vec4(result,alpha);
}

vec4 targetHighlight(){
    vec4 img = texture(image, texture_coord);
    vec4 result = vec4(vec3(img.rgb * 2.0f), img.a);
    return result;
}
