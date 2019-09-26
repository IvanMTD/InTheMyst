#version 430 core

out vec4 frag_color;

in vec2 texture_coord;

uniform sampler2D image;

uniform int horizontal;
uniform float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

void main(){
    // получаем размер одного текселя
    vec2 tex_offset = 1.0 / textureSize(image, 0);
    // вклад текущего фрагмента
    vec3 result = texture(image, texture_coord).rgb * weight[0];
    if(horizontal == 1){
        for(int i = 1; i < 5; i++){
            result += texture(image, texture_coord + vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
            result += texture(image, texture_coord - vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
        }
    }
    else{
        for(int i = 1; i < 5; i++){
            result += texture(image, texture_coord + vec2(0.0, tex_offset.y * i)).rgb * weight[i];
            result += texture(image, texture_coord - vec2(0.0, tex_offset.y * i)).rgb * weight[i];
        }
    }

    frag_color = vec4(result, 1.0);
}