#version 430 core

layout (location = 0) in vec3 l_pos;
layout (location = 1) in vec3 l_norm;
layout (location = 2) in vec2 l_tex;
layout (location = 3) in vec3 l_tan;
layout (location = 4) in vec3 l_bi_tan;
layout (location = 5) in ivec4 l_bone_id;
layout (location = 6) in vec4 l_bone_w;
layout (location = 7) in mat4 l_instance_m;

const float gradient = 8.0f;

layout (row_major, std140) uniform matrices{
    mat4 perspective_m;
    mat4 ortho_m;
    mat4 view_m;
};

// контролеры
uniform sampler2D heightMap;
uniform int indicator;
uniform int instance;
uniform int board;
uniform int grid;
uniform int isActive;
uniform int water;
uniform int tree;
uniform int bigTree;
uniform int turn;
uniform int w;
uniform int h;
// доп данные
uniform mat4 model_m;
uniform vec3 viewDirect;
uniform float xOffset;
uniform float yOffset;
uniform float zOffset;
// units
uniform vec4 unit0;
uniform vec4 unit1;
uniform vec4 unit2;
uniform vec4 unit3;
uniform vec4 unit4;
uniform vec4 unit5;

out VS_OUT{
    vec4 localPos;
    vec2 textureCoord;
    vec2 mapTexCoords;
    flat int indicator;
    flat int isBoard;
    flat int isGrid;
    flat int isActive;
    flat int tree;
    flat int water;
    float visibility;
}vs_out;

void main() {
    float gain = 3.5f;
    vec3 cameraRight_worldspace = vec3(view_m[0][0], view_m[1][0], view_m[2][0]);
    vec3 cameraUp_worldspace = vec3(view_m[0][1], view_m[1][1], view_m[2][1]);
    vec3 cameraCenter_worldspace = vec3(view_m[0][2], view_m[1][2], view_m[2][2]);

    vs_out.isBoard = board;
    vs_out.isGrid = grid;
    vs_out.isActive = isActive;
    vs_out.tree = tree;
    vs_out.water = water;
    vs_out.indicator = indicator;

    if(instance == 1){
        if(board == 1){
            vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
            vec3 particleCenter_wordspace = vec3(l_instance_m[0][2],l_instance_m[1][2],l_instance_m[2][2]);
            vec3 particleUp_wordspace = vec3(l_instance_m[0][1],l_instance_m[1][1],l_instance_m[2][1]);
            vec3 result = particleCenter_wordspace + (cameraRight_worldspace * l_pos.x * billboardSize.x + particleUp_wordspace * l_pos.y  * billboardSize.y);
            result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);

            if(turn == 1){
                result.x = -result.x;
                result.z = -result.z;
            }

            vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
            float visbl = 0;
            for(int i=0; i<6; i++){
                if(units[i].x != -1.0f){
                    vec4 elementPos = l_instance_m * vec4(result,1.0f);
                    vec3 direct = vec3(elementPos.xyz - units[i].xyz);
                    float distance = length(direct.xyz);
                    float vis = exp(-pow((distance * units[i].w),gradient));
                    visbl += clamp(vis,0.0f,1.0f);
                }
            }
            if(visbl > 1.0f){
                visbl = 1.0f;
            }
            /*vec4 elementPos = model_m * initPos;
            vec3 direct = vec3(elementPos.xyz - masterPoint);
            float distance = length(direct.xyz);
            float vis = exp(-pow((distance * density),gradient));*/
            vs_out.visibility = visbl;
            vs_out.localPos = l_instance_m * vec4(0.0f,0.0f,0.0f,1.0f);
            vec4 pointPos = l_instance_m * vec4(result,1.0f);
            float x = 0.0f;
            if(pointPos.x < 0){
                x = 0.0f;
            }else if(pointPos.x > w){
                x = 1.0f;
            }else{
                x = pointPos.x / w;
            }
            float y = 0.0f;
            if(pointPos.z < 0){
                y = 0.0f;
            }else if(pointPos.z > h){
                y = 1.0f;
            }else{
                y = pointPos.z / h;
            }
            vs_out.mapTexCoords = vec2(x,1.0f - y);
            /*float offset = (texture(heightMap, vs_out.mapTexCoords).r * gain) + 0.5f - 1.0f;
            vec4 res = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
            gl_Position = vec4(res.x,res.y + offset,res.z,res.w);*/
            gl_Position = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
        }else{
            vec3 result = vec3(l_pos.x + xOffset,l_pos.y + yOffset, l_pos.z + zOffset);

            vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
            float visbl = 0;
            for(int i=0; i<6; i++){
                if(units[i].x != -1.0f){
                    vec4 elementPos = l_instance_m * vec4(result,1.0f);
                    vec3 direct = vec3(elementPos.xyz - units[i].xyz);
                    float distance = length(direct.xyz);
                    float vis = exp(-pow((distance * units[i].w),gradient));
                    visbl += clamp(vis,0.0f,1.0f);
                }
            }
            if(visbl > 1.0f){
                visbl = 1.0f;
            }
            /*vec4 elementPos = model_m * initPos;
            vec3 direct = vec3(elementPos.xyz - masterPoint);
            float distance = length(direct.xyz);
            float vis = exp(-pow((distance * density),gradient));*/
            vs_out.visibility = visbl;
            vs_out.localPos = l_instance_m * vec4(0.0f,0.0f,0.0f,1.0f);
            vec4 pointPos = l_instance_m * vec4(l_pos,1.0f);
            float x = 0.0f;
            if(pointPos.x < 0){
                x = 0.0f;
            }else if(pointPos.x > w){
                x = 1.0f;
            }else{
                x = pointPos.x / w;
            }
            float y = 0.0f;
            if(pointPos.z < 0){
                y = 0.0f;
            }else if(pointPos.z > h){
                y = 1.0f;
            }else{
                y = pointPos.z / h;
            }
            vs_out.mapTexCoords = vec2(x,1.0f - y);
            gl_Position = perspective_m * view_m * l_instance_m * vec4(result,1.0f);
        }
    }else{
        if(board == 1){
            vec3 billboardSize = vec3(1.0f,1.0f,1.0f);
            vec3 particleCenter_wordspace = vec3(model_m[0][2],model_m[1][2],model_m[2][2]);
            vec3 particleUp_wordspace = vec3(model_m[0][1],model_m[1][1],model_m[2][1]);
            vec3 result = particleCenter_wordspace + (cameraRight_worldspace * l_pos.x * billboardSize.x + particleUp_wordspace * l_pos.y  * billboardSize.y);
            result = vec3(result.x + xOffset,result.y + yOffset,result.z - 1.0f + zOffset);

            if(turn == 1){
                result.x = -result.x;
                result.z = -result.z;
            }

            if(isActive == 1){
                /*vec3 somePos = cameraCenter_worldspace + (cameraCenter_worldspace - result) * -1.02f;
                result = vec3(somePos.x,result.y,somePos.z);*/
                vec3 direction = vec3(viewDirect.x,0.0f,viewDirect.z);
                result = result + (direction / 50);
            }

            if(bigTree == 1){
                vec3 direction = vec3(viewDirect.x,0.0f,viewDirect.z) * -1.0f;
                result = result + (direction / 2);
            }

            vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
            float visbl = 0;
            for(int i=0; i<6; i++){
                if(units[i].x != -1.0f){
                    vec4 elementPos = model_m * vec4(result,1.0f);
                    vec3 direct = vec3(elementPos.xyz - units[i].xyz);
                    float distance = length(direct.xyz);
                    float vis = exp(-pow((distance * units[i].w),gradient));
                    visbl += clamp(vis,0.0f,1.0f);
                }
            }
            if(visbl > 1.0f){
                visbl = 1.0f;
            }
            /*vec4 elementPos = model_m * initPos;
            vec3 direct = vec3(elementPos.xyz - masterPoint);
            float distance = length(direct.xyz);
            float vis = exp(-pow((distance * density),gradient));*/
            vs_out.visibility = visbl;
            vs_out.localPos = model_m * vec4(0.0f,0.0f,0.0f,1.0f);
            vec4 pointPos = model_m * vec4(result,1.0f);
            float x = 0.0f;
            if(pointPos.x < 0){
                x = 0.0f;
            }else if(pointPos.x > w){
                x = 1.0f;
            }else{
                x = pointPos.x / w;
            }
            float y = 0.0f;
            if(pointPos.z < 0){
                y = 0.0f;
            }else if(pointPos.z > h){
                y = 1.0f;
            }else{
                y = pointPos.z / h;
            }
            vs_out.mapTexCoords = vec2(x,1.0f - y);
            /*if(isActive == 1){
                float wc = w + 1;
                float hc = h + 1;
                float x = getCorrectNum(vs_out.localPos.x + 0.5f, w + 1) / wc;
                float y = getCorrectNum(vs_out.localPos.z + 0.5f, h + 1) / hc;
                vec2 tc = vec2(x,1.0f - y);
                float offset = (texture(heightMap, tc).r * gain) + 0.5f - 1.0f;
                vec4 res = perspective_m * view_m * model_m * vec4(result,1.0f);
                gl_Position = vec4(res.x,res.y + offset,res.z,res.w);
            }else{
                float offset = (texture(heightMap, vs_out.mapTexCoords).r * gain) + 0.5f - 1.0f;
                vec4 res = perspective_m * view_m * model_m * vec4(result,1.0f);
                if(indicator == 1 || tree == 1 || bigTree == 1){
                    offset = 0.0f;
                }
                gl_Position = vec4(res.x,res.y + offset,res.z,res.w);
            }*/
            gl_Position = perspective_m * view_m * model_m * vec4(result,1.0f);
        }else{
            vec3 result = vec3(l_pos.x + xOffset,l_pos.y + yOffset, l_pos.z + zOffset);

            vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
            float visbl = 0;
            for(int i=0; i<6; i++){
                if(units[i].x != -1.0f){
                    vec4 elementPos = model_m * vec4(result,1.0f);
                    vec3 direct = vec3(elementPos.xyz - units[i].xyz);
                    float distance = length(direct.xyz);
                    float vis = exp(-pow((distance * units[i].w),gradient));
                    visbl += clamp(vis,0.0f,1.0f);
                }
            }
            if(visbl > 1.0f){
                visbl = 1.0f;
            }
            /*vec4 elementPos = model_m * initPos;
            vec3 direct = vec3(elementPos.xyz - masterPoint);
            float distance = length(direct.xyz);
            float vis = exp(-pow((distance * density),gradient));*/
            vs_out.visibility = visbl;
            vs_out.localPos = model_m * vec4(0.0f,0.0f,0.0f,1.0f);
            vec4 pointPos = model_m * vec4(l_pos,1.0f);
            float x = 0.0f;
            if(pointPos.x < 0){
                x = 0.0f;
            }else if(pointPos.x > w){
                x = 1.0f;
            }else{
                x = pointPos.x / w;
            }
            float y = 0.0f;
            if(pointPos.z < 0){
                y = 0.0f;
            }else if(pointPos.z > h){
                y = 1.0f;
            }else{
                y = pointPos.z / h;
            }
            vs_out.mapTexCoords = vec2(x,1.0f - y);
            /*if(water == 1){
                vec4 res = perspective_m * view_m * model_m * vec4(result,1.0f);
                gl_Position = vec4(res.x,res.y + 0.1f,res.z,res.w);
            }else if(indicator == 1){
                float wc = w + 1;
                float hc = h + 1;
                float x = getCorrectNum(vs_out.localPos.x + 0.5f, w + 1) / wc;
                float y = getCorrectNum(vs_out.localPos.z + 0.5f, h + 1) / hc;
                vec2 tc = vec2(x,1.0f - y);
                float offset = (texture(heightMap, tc).r * gain) + 0.5f - 1.0f;
                vec4 res = perspective_m * view_m * model_m * vec4(result,1.0f);
                gl_Position = vec4(res.x,res.y + (offset + 0.2f),res.z,res.w);
            }else{
                float offset = (texture(heightMap, vs_out.mapTexCoords).r * gain) + 0.5f - 1.0f;
                vec4 res = perspective_m * view_m * model_m * vec4(result,1.0f);
                gl_Position = vec4(res.x,res.y + offset,res.z,res.w);
            }*/
            gl_Position = perspective_m * view_m * model_m * vec4(result,1.0f);
        }
    }
    vs_out.textureCoord = l_tex;
}
