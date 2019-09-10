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

out vec2 texCoord;

// контролеры
uniform int instance;
uniform int animated;
uniform int board;

// доп данные
uniform mat4 model_m;
uniform mat4 lightSpaceMatrix;
uniform mat4 directLightViewMatrix;
uniform mat4 joints_m[MAX_JOINTS];
uniform float xOffset;
uniform float yOffset;
uniform float zOffset;

void main(){

    vec3 sunRight_worldspace = {directLightViewMatrix[0][0], directLightViewMatrix[1][0], directLightViewMatrix[2][0]};
    vec3 sunUp_worldspace = {directLightViewMatrix[0][1], directLightViewMatrix[1][1], directLightViewMatrix[2][1]};

    texCoord = l_tex;

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
            if(board == 1){
                vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
                vec3 particleCenter_wordspace = {l_instance_m[0][2],l_instance_m[1][2],l_instance_m[2][2]};
                vec3 particleUp_wordspace = {l_instance_m[0][1],l_instance_m[1][1],l_instance_m[2][1]};
                vec3 result = particleCenter_wordspace + (sunRight_worldspace * l_pos.x * billboardSize.x + sunUp_worldspace * l_pos.y  * billboardSize.y);
                result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);
                gl_Position = lightSpaceMatrix * l_instance_m * vec4(result,1.0f);
            }else{
                gl_Position = lightSpaceMatrix * l_instance_m * vec4(l_pos, 1.0);
            }
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
            if(board == 1){
                vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
                vec3 particleCenter_wordspace = {model_m[0][2],model_m[1][2],model_m[2][2]};
                vec3 particleUp_wordspace = {model_m[0][1],model_m[1][1],model_m[2][1]};
                vec3 result = particleCenter_wordspace + (sunRight_worldspace * l_pos.x * billboardSize.x + sunUp_worldspace * l_pos.y  * billboardSize.y);
                result = vec3(result.x + xOffset,result.y + 0.1f + yOffset,result.z - 1.0f + zOffset);
                gl_Position = lightSpaceMatrix * model_m * vec4(result,1.0f);
            }else{
                gl_Position = lightSpaceMatrix * model_m * vec4(l_pos, 1.0);
            }
        }
    }
}
