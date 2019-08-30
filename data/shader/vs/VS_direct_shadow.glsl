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

// контролеры
uniform int instance;
uniform int animated;

// доп данные
uniform mat4 model_m;
uniform mat4 lightSpaceMatrix;
uniform mat4 joints_m[MAX_JOINTS];

void main(){
    if(instance == 1){
        if(animated == 1){
            mat4 boneTransform;

            boneTransform  = joints_m[l_bone_id[0]] * l_bone_w[0];
            boneTransform += joints_m[l_bone_id[1]] * l_bone_w[1];
            boneTransform += joints_m[l_bone_id[2]] * l_bone_w[2];
            boneTransform += joints_m[l_bone_id[3]] * l_bone_w[3];

            vec4 newPos = boneTransform * vec4(l_pos,1.0f);

            gl_Position = lightSpaceMatrix * l_instance_m * newPos;
        }else{
            gl_Position = lightSpaceMatrix * l_instance_m * vec4(l_pos, 1.0);
        }
    }else{
        if(animated == 1){
            mat4 boneTransform;

            boneTransform  = joints_m[l_bone_id[0]] * l_bone_w[0];
            boneTransform += joints_m[l_bone_id[1]] * l_bone_w[1];
            boneTransform += joints_m[l_bone_id[2]] * l_bone_w[2];
            boneTransform += joints_m[l_bone_id[3]] * l_bone_w[3];

            vec4 newPos = boneTransform * vec4(l_pos,1.0f);

            gl_Position = lightSpaceMatrix * model_m * newPos;
        }else{
            gl_Position = lightSpaceMatrix * model_m * vec4(l_pos, 1.0);
        }
    }
}
