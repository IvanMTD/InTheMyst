#version 330 core

#define R 131073
#define G 131074
#define B 131075
#define A 131076

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;


in vec2 textureCoord;
flat in int isBoard;

uniform sampler2D image;
uniform int group;
uniform float id;
uniform int onTarget;
uniform int water;

vec4 targetHighlight(vec4 rgba);

void main(){
    // обробатываем текстуру
    vec4 rgba = texture(image,textureCoord);
    if(rgba.a < 1.0f && isBoard == 1){
        discard;
    }
    // Расчитываем цвет фрагмента
    if(onTarget == 1){
        fragment_color = targetHighlight(rgba);
    }else{
        if(water == 1){
            fragment_color = vec4(rgba.r - 0.5f,rgba.g - 0.1f,rgba.b + 0.4f,rgba.a * 2.0f);
        }else{
            fragment_color = rgba;
        }
    }

    // Расчитываем выбраный цвет
    if(group == R){
        select_color = vec4(id, 0.0f, 0.0f, rgba.a);
    }else if(group == G){
        select_color = vec4(0.0f, id, 0.0f, 1.0f);
    }else if(group == B){
        select_color = vec4(0.0f, 0.0f, id, 1.0f);
    }else if(group == A){
        select_color = vec4(0.0f, 0.0f, 0.0f, id);
    }

    // проверка фрагмента на превышение по яркости заданного порога
    // если ярче - вывести в отдельный буфер, хранящий яркие участки
    float brightness = dot(fragment_color.rgb, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0){
        bright_color = vec4(fragment_color.rgb, rgba.a);
    }
    else{
        bright_color = vec4(0.0f, 0.0f, 0.0f, rgba.a);
    }
}

vec4 targetHighlight(vec4 rgba){
    vec4 result = vec4(vec3(rgba.rgb * 2.0f), rgba.a);
    return result;
}
