#version 430 core

//const vec4 skyColor = vec4(0.2f,0.4f,0.5f,1.0f);
const vec4 skyColor = vec4(0.0f,0.0f,0.0f,1.0f);

const vec4 FOG = vec4(0.0f,0.0f,0.0f,1.0f);
const vec4 MASK = vec4(1.0f,0.0f,0.0f,1.0f);

out vec4 fragment_color;

// Входящий Блок
in VS_OUT {
    vec2 texCoord;
    vec2 mapTexCoord;
    float visibility;
} fs_in;

uniform sampler2D map;
uniform int start;

// Структуры и Юниформы
struct Material{
    sampler2D ambientMap;
    sampler2D diffuseMap;
    sampler2D specularMap;
    sampler2D normalMap;
    sampler2D displaceMap;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Material material;

void main() {
    if(start == 1){
        vec4 pm = texture(map,fs_in.mapTexCoord);
        vec4 nm = mix(FOG,MASK, fs_in.visibility);
        if(nm.r < 1.0f){
            if(nm.r > pm.r){
                fragment_color = nm;
            }else{
                fragment_color = pm;
            }
        }else{
            fragment_color = pm;
        }
    }else{
        fragment_color = mix(FOG,MASK, fs_in.visibility);
    }
}