#version 430 core

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

in GS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
    float biom;
    float percent;
} fs_in;

// Структуры и Юниформы
struct Material{
    sampler2D desert_low;
    sampler2D desert_side;
    sampler2D desert_up;

    sampler2D steppe_low;
    sampler2D steppe_side;
    sampler2D steppe_up;

    sampler2D plain_low;
    sampler2D plain_side;
    sampler2D plain_up;

    sampler2D forest_low;
    sampler2D forest_side;
    sampler2D forest_up;

    sampler2D mountains_low;
    sampler2D mountains_side;
    sampler2D mountains_up;

    sampler2D road;
};

uniform sampler2D blendMap;
uniform sampler2D heightMap;
uniform Material material;

void main() {

    vec2 tex = vec2(fs_in.cellTextureCoord.x,1.0f - fs_in.cellTextureCoord.y);

    vec4 blend = texture(blendMap,fs_in.mapTextureCoord);
    vec4 height = texture(heightMap,fs_in.mapTextureCoord);

    vec4 desert_low = texture(material.desert_low,tex);
    vec4 desert_side = texture(material.desert_side,tex);
    vec4 desert_up = texture(material.desert_up,tex);

    vec4 steppe_low = texture(material.steppe_low,tex);
    vec4 steppe_side = texture(material.steppe_side,tex);
    vec4 steppe_up = texture(material.steppe_up,tex);

    vec4 plain_low = texture(material.plain_low,tex);
    vec4 plain_side = texture(material.plain_side,tex);
    vec4 plain_up = texture(material.plain_up,tex);

    vec4 forest_low = texture(material.forest_low,tex);
    vec4 forest_side = texture(material.forest_side,tex);
    vec4 forest_up = texture(material.forest_up,tex);

    vec4 mountains_low = texture(material.mountains_low,tex);
    vec4 mountains_side = texture(material.mountains_side,tex);
    vec4 mountains_up = texture(material.mountains_up,tex);

    vec4 road = texture(material.road,tex);

    vec4 low;
    vec4 up;

    float b = round(height.r * 45.0f);
    float percent = height.b;

    if(b <= 5.0f){ // ПУСТЫНЯ
        if(fs_in.normal.y <= 0.0f){
            if(desert_side.a == 1.0f){
                fragment_color = desert_side;
            }else{
                fragment_color = desert_low;
            }
        }else{
            fragment_color = desert_up;
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 10.0f){ // ПЕРЕХОД ПУСТЫНИ В СТЕПЬ
        if(fs_in.normal.y <= 0.0f){
            vec4 side = mix(desert_side,steppe_side,percent);
            if(side.a == 1.0f){
                fragment_color = side;
            }else{
                fragment_color = mix(desert_low,steppe_low,percent);
            }
        }else{
            fragment_color = mix(desert_up,steppe_up,percent);
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 15.0f){ // СТЕПЬ
        if(fs_in.normal.y <= 0.0f){
            if(steppe_side.a == 1.0f){
                fragment_color = steppe_side;
            }else{
                fragment_color = steppe_low;
            }
        }else{
            fragment_color = steppe_up;
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 20.0f){ // ПЕРЕХОД СТЕПИ В РАВНИНУ
        if(fs_in.normal.y <= 0.0f){
            vec4 side = mix(steppe_side,plain_side,percent);
            if(side.a == 1.0f){
                fragment_color = side;
            }else{
                fragment_color = mix(steppe_low,plain_low,percent);
            }
        }else{
            fragment_color = mix(steppe_up,plain_up,percent);
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 25.0f){ // РАВНИНА
        if(fs_in.normal.y <= 0.0f){
            if(plain_side.a == 1.0f){
                fragment_color = plain_side;
            }else{
                fragment_color = plain_low;
            }
        }else{
            fragment_color = plain_up;
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 30.0f){ // ПЕРЕХОД РАВНИНЫ В ЛЕС
        if(fs_in.normal.y <= 0.0f){
            vec4 side = mix(plain_side,forest_side,percent);
            if(side.a == 1.0f){
                fragment_color = side;
            }else{
                fragment_color = mix(plain_low,forest_low,percent);
            }
        }else{
            fragment_color = mix(plain_up,forest_up,percent);
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 35.0f){ // ЛЕС
        if(fs_in.normal.y <= 0.0f){
            if(forest_side.a == 1.0f){
                fragment_color = forest_side;
            }else{
                fragment_color = forest_low;
            }
        }else{
            fragment_color = forest_up;
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else if(b <= 40.0f){ // ПЕРЕХОД ЛЕСА В ГОРЫ
        if(fs_in.normal.y <= 0.0f){
            vec4 side = mix(forest_side,mountains_side,percent);
            if(side.a == 1.0f){
                fragment_color = side;
            }else{
                fragment_color = mix(forest_low,mountains_low,percent);
            }
        }else{
            fragment_color = mix(forest_up,mountains_up,percent);
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }else{
        if(fs_in.normal.y <= 0.0f){
            if(mountains_side.a == 1.0f){
                fragment_color = mountains_side;
            }else{
                fragment_color = mountains_low;
            }
        }else{
            fragment_color = mountains_up;
        }
        if(blend.r != blend.g){
            fragment_color = mix(road,fragment_color,0.3f);
        }
    }

    //fragment_color = vec4(fs_in.normal,1.0f);

    select_color = vec4(0.0f,0.0f,0.0f,0.0f);
    bright_color = vec4(0.0f,0.0f,0.0f,0.0f);
}
