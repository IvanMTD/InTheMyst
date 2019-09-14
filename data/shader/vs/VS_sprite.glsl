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
uniform int board;
uniform int isActive;

// доп данные
uniform mat4 model_m;
uniform float xOffset;
uniform float yOffset;
uniform float zOffset;

out vec2 textureCoord;

void main() {
    vec3 cameraRight_worldspace = {view_m[0][0], view_m[1][0], view_m[2][0]};
    vec3 cameraUp_worldspace = {view_m[0][1], view_m[1][1], view_m[2][1]};
    vec3 cameraCenter_worldspace = {view_m[0][2], view_m[1][2], view_m[2][2]};

    if(instance == 1){
        if(board == 1){
            vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
            vec3 particleCenter_wordspace = {l_instance_m[0][2],l_instance_m[1][2],l_instance_m[2][2]};
            vec3 particleUp_wordspace = {l_instance_m[0][1],l_instance_m[1][1],l_instance_m[2][1]};
            vec3 result = particleCenter_wordspace + (cameraRight_worldspace * l_pos.x * billboardSize.x + particleUp_wordspace * l_pos.y  * billboardSize.y);
            result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);
            gl_Position = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
        }else{
            vec3 result = vec3(l_pos.x + xOffset,l_pos.y + yOffset, l_pos.z + zOffset);
            gl_Position = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
        }
    }else{
        if(board == 1){
            vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
            vec3 particleCenter_wordspace = {model_m[0][2],model_m[1][2],model_m[2][2]};
            vec3 particleUp_wordspace = {model_m[0][1],model_m[1][1],model_m[2][1]};
            vec3 result = particleCenter_wordspace + (cameraRight_worldspace * l_pos.x * billboardSize.x + particleUp_wordspace * l_pos.y  * billboardSize.y);
            result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);

            if(isActive == 1){
                vec3 somePos = cameraCenter_worldspace + (cameraCenter_worldspace - result) * -1.02f;
                result = vec3(somePos.x,result.y,somePos.z);
            }

            gl_Position = perspective_m * view_m * model_m * vec4(result,1.0f);
        }else{
            vec3 result = vec3(l_pos.x + xOffset,l_pos.y + yOffset, l_pos.z + zOffset);
            gl_Position = perspective_m * view_m * model_m * vec4(result,1.0f);
        }
    }
    textureCoord = l_tex;
}
