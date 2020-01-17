#version 430 core

const int MAX_JOINTS = 44;

layout (location = 0) in vec3 l_pos;
layout (location = 1) in vec3 l_norm;
layout (location = 2) in vec2 l_tex;
layout (location = 3) in vec3 l_tan;
layout (location = 4) in vec3 l_bi_tan;
layout (location = 5) in ivec4 l_bone_id;
layout (location = 6) in vec4 l_bone_w;
layout (location = 7) in mat4 l_instance_m;

const float gradient = 8.0f;

uniform int instance;

uniform mat4 model_m;
uniform mat4 mapCam_m;

uniform vec4 unit0;
uniform vec4 unit1;
uniform vec4 unit2;
uniform vec4 unit3;
uniform vec4 unit4;
uniform vec4 unit5;

out VS_OUT {
    vec4 localPos;
    vec2 texCoord;
    float visibility;
} vs_out;

void main() {
    if(instance == 1){
        gl_Position = mapCam_m * l_instance_m * vec4(l_pos,1.0f);
        vs_out.localPos = l_instance_m * vec4(l_pos,1.0f);
        vs_out.texCoord = vec2(l_tex.x,1.0f - l_tex.y);
        vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
        float visbl = 0;
        for(int i=0; i<6; i++){
            if(units[i].x != -1.0f){
                vec4 elementPos = l_instance_m * vec4(l_pos,1.0f);
                vec3 direct = vec3(elementPos.xyz - units[i].xyz);
                float distance = length(direct.xyz);
                float vis = exp(-pow((distance * units[i].w),gradient));
                visbl += clamp(vis,0.0f,1.0f);
            }
        }
        if(visbl > 1.0f){
            visbl = 1.0f;
        }
        vs_out.visibility = visbl;
    }else{
        gl_Position = mapCam_m * model_m * vec4(l_pos,1.0f);
        vs_out.localPos = model_m * vec4(l_pos,1.0f);
        vs_out.texCoord = vec2(l_tex.x,1.0f - l_tex.y);
        vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
        float visbl = 0;
        for(int i=0; i<6; i++){
            if(units[i].x != -1.0f){
                vec4 elementPos = model_m * vec4(l_pos,1.0f);
                vec3 direct = vec3(elementPos.xyz - units[i].xyz);
                float distance = length(direct.xyz);
                float vis = exp(-pow((distance * units[i].w),gradient));
                visbl += clamp(vis,0.0f,1.0f);
            }
        }
        if(visbl > 1.0f){
            visbl = 1.0f;
        }
        vs_out.visibility = visbl;
    }
}