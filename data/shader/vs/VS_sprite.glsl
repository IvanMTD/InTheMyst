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
uniform int turn;

// доп данные
uniform mat4 model_m;
uniform float xOffset;
uniform float yOffset;
uniform float zOffset;

out vec2 textureCoord;
flat out int isBoard;

void main() {
    vec3 cameraRight_worldspace = vec3(view_m[0].x, view_m[1].x, view_m[2].x);
    vec3 cameraUp_worldspace = vec3(view_m[0].y, view_m[1].y, view_m[2].y);
    vec3 cameraCenter_worldspace = vec3(view_m[0].z, view_m[1].z, view_m[2].z);

    isBoard = board;

    if(instance == 1){
        if(board == 1){
            vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
            vec3 particleCenter_wordspace = vec3(l_instance_m[0].z,l_instance_m[1].z,l_instance_m[2].z);
            vec3 particleUp_wordspace = vec3(l_instance_m[0].y,l_instance_m[1].y,l_instance_m[2].y);
            vec3 result = particleCenter_wordspace + (cameraRight_worldspace * l_pos.x * billboardSize.x + particleUp_wordspace * l_pos.y  * billboardSize.y);
            result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);

            if(turn == 1){
                result.x = -result.x;
                result.z = -result.z;
            }

            gl_Position = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
        }else{
            vec3 result = vec3(l_pos.x + xOffset,l_pos.y + yOffset, l_pos.z + zOffset);
            gl_Position = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
        }
    }else{
        if(board == 1){
            vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
            vec3 particleCenter_wordspace = vec3(model_m[0].z,model_m[1].z,model_m[2].z);
            vec3 particleUp_wordspace = vec3(model_m[0].y,model_m[1].y,model_m[2].y);
            vec3 result = particleCenter_wordspace + (cameraRight_worldspace * l_pos.x * billboardSize.x + particleUp_wordspace * l_pos.y  * billboardSize.y);
            result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);

            if(turn == 1){
                result.x = -result.x;
                result.z = -result.z;
            }

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
