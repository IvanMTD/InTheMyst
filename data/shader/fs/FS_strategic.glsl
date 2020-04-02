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
uniform Material material;

void main() {

    vec2 tex = vec2(fs_in.cellTextureCoord.x,1.0f - fs_in.cellTextureCoord.y);

    vec4 blend = texture(blendMap,fs_in.mapTextureCoord);

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

    if(fs_in.biom < 0.02f){
        low = desert_low;
        up = desert_up;
        if(fs_in.normal.y == 0.0f){
            if(desert_side.a != 0.0f){
                up = desert_side;
            }else{
                up = desert_low;
            }
        }
    }else if(fs_in.biom < 0.04f){
        low = steppe_low;
        up = mix(desert_up,steppe_up,blend.r + 0.2f);
        if(fs_in.normal.y == 0.0f){
            if(desert_side.a != 0.0f){
                up = mix(desert_side,steppe_side,blend.r + 0.2f);
            }else{
                up = low;
            }
        }
    }else if(fs_in.biom < 0.1f){
        low = steppe_low;
        up = steppe_up;
        if(fs_in.normal.y == 0.0f){
            if(steppe_side.a != 0.0f){
                up = steppe_side;
            }else{
                up = steppe_low;
            }
        }
    }else if(fs_in.biom < 0.2f){
        low = plain_low;
        up = mix(steppe_up,plain_up,blend.r + 0.2f);
        if(fs_in.normal.y == 0.0f){
            if(steppe_side.a != 0.0f){
                up = mix(steppe_side,plain_side,blend.r + 0.2f);
            }else{
                up = low;
            }
        }
    }else if(fs_in.biom < 0.35f){
        low = plain_low;
        up = plain_up;
        if(fs_in.normal.y == 0.0f){
            if(plain_side.a != 0.0f){
                up = plain_side;
            }else{
                up = plain_low;
            }
        }
    }else if(fs_in.biom < 0.5f){
        low = forest_low;
        up = mix(plain_up,forest_up,blend.r + 0.2f);
        if(fs_in.normal.y == 0.0f){
            if(plain_side.a != 0.0f){
                up = mix(plain_side, forest_side, blend.r + 0.2f);
            }else{
                up = low;
            }
        }
    }else if(fs_in.biom < 0.6f){
        low = forest_low;
        up = forest_up;
        if(fs_in.normal.y == 0.0f){
            if(forest_side.a != 0.0f){
                up = forest_side;
            }else{
                up = low;
            }
        }
    }else if(fs_in.biom < 0.95f){
        low = mountains_low;
        up = mix(forest_up,mountains_up,blend.r + 0.2f);
        if(fs_in.normal.y == 0.0f){
            if(forest_side.a != 0.0f){
                up = mix(forest_side, mountains_side, blend.r + 0.2f);
            }else{
                up = low;
            }
        }
    }else{
        low = mountains_low;
        up = mountains_up;
        if(fs_in.normal.y == 0.0f){
            if(mountains_side.a != 0.0f){
                up = mountains_side;
            }else{
                up = low;
            }
        }
    }

    fragment_color = mix(low, up, blend.r + 0.2f);
    if(blend.r != blend.g){
        fragment_color = mix(road,fragment_color,0.3f);
    }

    //fragment_color = vec4(fs_in.biom,0.0f,0.0f,1.0f);

    select_color = vec4(0.0f,0.0f,0.0f,0.0f);
    bright_color = vec4(0.0f,0.0f,0.0f,0.0f);
}
