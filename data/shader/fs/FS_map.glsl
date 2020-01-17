#version 430 core

const vec4 skyColor = vec4(0.2f,0.4f,0.5f,1.0f);

out vec4 fragment_color;

// Входящий Блок
in VS_OUT {
    vec4 localPos;
    vec2 texCoord;
    float visibility;
} fs_in;

uniform int w;
uniform int h;
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

int getCorrectNum(float,int);

void main() {
    float wc = w + 1;
    float hc = h + 1;
    float x = getCorrectNum(fs_in.localPos.x + 0.5f, w + 1) / wc;
    float y = getCorrectNum(fs_in.localPos.z + 0.5f, h + 1) / hc;
    vec2 tex = vec2(x,1.0f - y);
    vec4 color = texture(map,tex);
    if(start == 1){
        if(color.r == skyColor.r && color.g == skyColor.g && color.b == skyColor.b){
            fragment_color = texture(material.diffuseMap,fs_in.texCoord);
            fragment_color = mix(vec4(skyColor),fragment_color, fs_in.visibility);
        }else{
            fragment_color = texture(material.diffuseMap,fs_in.texCoord);
            fragment_color = mix(vec4(vec4(fragment_color.rgb / 2.0f,fragment_color.a)),fragment_color, fs_in.visibility);
        }
        //fragment_color = color;
    }else{
        fragment_color = texture(material.diffuseMap,fs_in.texCoord);
        fragment_color = mix(vec4(skyColor),fragment_color, fs_in.visibility);
    }
}

int getCorrectNum(float num, int size){
    int n = 0;
    for(int i=0; i<=size; i++){
        if(i-0.5f < num && num < i+0.5f){
            n = i;
            break;
        }
    }
    return n;
}