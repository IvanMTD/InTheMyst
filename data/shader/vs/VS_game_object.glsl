#version 430 core

layout (location = 0) in vec3 l_pos;
layout (location = 1) in vec3 l_norm;
layout (location = 2) in vec2 l_tex;
layout (location = 3) in vec3 l_tan;
layout (location = 4) in vec3 l_bi_tan;
layout (location = 5) in ivec4 l_bone_id;
layout (location = 6) in vec4 l_bone_w;
layout (location = 7) in mat4 l_instance_m;

layout (row_major, std140) uniform matrices{
    mat4 perspective_m;
    mat4 ortho_m;
    mat4 view_m;
};

// контролеры
uniform int instance;

// доп данные
uniform mat4 model_m;
uniform mat4 lightSpaceMatrix;
uniform vec3 viewPos;
uniform int w;
uniform int h;

out VS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoords;
    vec2 mapTexCoords;
    vec3 ViewPos;
    vec4 FragPosLightSpace;
    vec4 localPos;
} vs_out;

vec2 getMapTexCoord();

void main() {
    mat4 projection;
    vec2 texCoords;
    if(instance == 1){
        projection = l_instance_m;
        texCoords = l_tex;
    }else{
        projection = model_m;
        texCoords = vec2(l_tex.x,1.0f - l_tex.y);
    }
    gl_Position = perspective_m * view_m * projection * vec4(l_pos, 1.0f);
    vs_out.localPos = projection * vec4(l_pos, 1.0f);
    vs_out.mapTexCoords = getMapTexCoord();
    vs_out.FragPos = vec3(projection * vec4(l_pos, 1.0f));
    vs_out.Normal = l_norm;
    vs_out.TexCoords = texCoords;
    vs_out.ViewPos = viewPos;
    vs_out.FragPosLightSpace = lightSpaceMatrix * vec4(vs_out.FragPos, 1.0);
}

vec2 getMapTexCoord(){
    float x = 0.0f;
    if(vs_out.localPos.x < 0){
        x = 0.0f;
    }else if(vs_out.localPos.x > w){
        x = 1.0f;
    }else{
        x = vs_out.localPos.x / w;
    }

    float y = 0.0f;
    if(vs_out.localPos.z < 0){
        y = 0.0f;
    }else if(vs_out.localPos.z > h){
        y = 1.0f;
    }else{
        y = vs_out.localPos.z / h;
    }
    return vec2(x, 1.0f - y);
}
