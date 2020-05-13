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

out VS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
    float biom;
    float percent;
} vs_out;

uniform sampler2D heightMap;
uniform mat4 model_m;

void main() {
    vs_out.normal = l_norm;
    vs_out.cellTextureCoord = l_tex;
    vs_out.mapTextureCoord = vec2(l_tan.x,l_tan.y);
    vec4 map = texture(heightMap,vs_out.mapTextureCoord);
    vec3 pos = vec3(l_pos.x, round(map.y * 20.0f), l_pos.z);
    gl_Position = perspective_m * view_m * model_m * vec4(pos, 1.0f);
    vs_out.biom = round(map.x * 45.0f);
    vs_out.percent = map.z;
}
