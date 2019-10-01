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
uniform vec3 sunRay;
uniform float xOffset;
uniform float yOffset;
uniform float zOffset;

void main(){

    vec3 sunRight_worldspace = vec3(directLightViewMatrix[0].x, directLightViewMatrix[1].x, directLightViewMatrix[2].x);
    vec3 sunUp_worldspace = vec3(directLightViewMatrix[0].y, directLightViewMatrix[1].y, directLightViewMatrix[2].y);

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
                vec3 particleCenter_wordspace = vec3(l_instance_m[0].z,l_instance_m[1].z,l_instance_m[2].z);
                vec3 particleUp_wordspace = vec3(l_instance_m[0].y,l_instance_m[1].y,l_instance_m[2].y);
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
                vec3 particleCenter_wordspace = vec3(model_m[0].z,model_m[1].z,model_m[2].z);
                vec3 particleUp_wordspace = vec3(model_m[0].y,model_m[1].y,model_m[2].y);
                vec3 result = particleCenter_wordspace + (sunRight_worldspace * l_pos.x * billboardSize.x + sunUp_worldspace * l_pos.y  * billboardSize.y);
                result = vec3(result.x + xOffset,result.y + 1.0f + yOffset,result.z - 1.0f + zOffset);

                vec3 inverseRay = (sunRay * 0.65f) * -1.0f;
                vec3 somePos = result + inverseRay;
                result = vec3(somePos.x,result.y,somePos.z);

                gl_Position = lightSpaceMatrix * model_m * vec4(result,1.0f);
            }else{
                gl_Position = lightSpaceMatrix * model_m * vec4(l_pos, 1.0);
            }
        }
    }
}
