#version 430 core

#define R 131073
#define G 131074
#define B 131075
#define A 131076

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

//const vec4 skyColor = vec4(0.2f,0.4f,0.5f,1.0f);
const vec4 skyColor = vec4(0.0f,0.0f,0.0f,1.0f);
//const vec4 skyColor = vec4(0.5f,0.5f,0.5f,1.0f);

in GS_OUT{
    vec2 mapTexCoords;
    flat int indicator;
    flat int isActive;
    flat int tree;
    flat int water;
}fs_in;

in vec2 textureCoord;
in flat int isBoard;
in flat int isGrid;
in flat int useShading;
in flat int battle;
in float visibility;

uniform sampler2D image;
uniform sampler2D map;
uniform int group;
uniform float id;
// switch
uniform int onTarget;
uniform int discardReverse;
uniform float discardControl;
uniform int showAlpha;

vec4 targetHighlight(vec4 rgba);

void main(){
    vec4 color = texture(map,fs_in.mapTexCoords);
    // обробатываем текстуру
    vec4 rgba = texture(image,textureCoord);
    if(rgba.a < 1.0f && isBoard == 1){
        discard;
    }

    if(discardControl > -1.0f){
        if(discardReverse == 1){
            if(textureCoord.x < discardControl){
                discard;
            }
        }else{
            if(textureCoord.x > discardControl){
                discard;
            }
        }
    }

    // Расчитываем цвет фрагмента
    if(onTarget == 1){
        fragment_color = targetHighlight(rgba);
    }else{
        if(useShading == 1){
            if(fs_in.water == 1){
                fragment_color = vec4(rgba.r - 0.5f,rgba.g - 0.1f,rgba.b + 0.4f,rgba.a * 2.0f);
            }else{
                fragment_color = vec4(rgba.rgb / 10.0f, rgba.a);
            }
        }else{
            if(fs_in.water == 1){
                fragment_color = vec4(rgba.r - 0.5f,rgba.g - 0.1f,rgba.b + 0.4f,rgba.a * 2.0f);
            }else{
                if(battle == 1){
                    vec3 rgb = vec3(rgba);
                    float a = rgba.a / 2.0f;
                    if(fs_in.tree == 1){
                        if(showAlpha == 1){
                            fragment_color = vec4(rgb,a);
                        }else{
                            fragment_color = rgba;
                        }
                    }else{
                        fragment_color = rgba;
                    }
                }else{
                    fragment_color = rgba;
                }
            }
        }
    }

    if(color.r > 0.0f){
        fragment_color = mix(vec4(skyColor),fragment_color, color.r);
    }else{
        fragment_color = skyColor;
    }

    if(fs_in.isActive == 1){
        if(visibility == 0.0f){
            discard;
        }else {
            fragment_color = mix(vec4(0.0f, 0.0f, 0.0f, 0.0f), fragment_color, visibility);
        }
    }

    if(fs_in.indicator == 1){
        fragment_color = rgba;
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
