#version 430 core

const int MAX_JOINTS = 44;
const int MAX_WEIGHTS = 4;
const float gradient = 8.0f;

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
uniform sampler2D heightMap;
uniform int animated;
uniform int instance;
uniform int alternative;
uniform int w;
uniform int h;

// доп данные
uniform mat4 model_m;
uniform mat4 lightSpaceMatrix;
uniform vec3 viewPos;
uniform mat4 joints_m[MAX_JOINTS];

uniform vec4 unit0;
uniform vec4 unit1;
uniform vec4 unit2;
uniform vec4 unit3;
uniform vec4 unit4;
uniform vec4 unit5;

out VS_OUT {
    flat int alternative;
     vec3 FragPos;
     vec3 Normal;
     vec2 TexCoords;
     vec2 mapTexCoords;
     vec3 TangentViewPos;
     vec3 TangentFragPos;
     vec3 ViewPos;
     vec4 FragPosLightSpace;
     mat3 TBN;
     vec4 localPos;
     float visibility;
} vs_out;

void main() {
    float gain = 3.5f;
    vec4 initNormal = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    vec4 initPos = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    vec4 mapPos = vec4(0.0f,0.0f,0.0f,0.0f);

    if(instance == 1){
        if(animated == 1){
            int count = 0;

            for(int i = 0; i < MAX_WEIGHTS; i++){
                float weight = l_bone_w[i];
                if(weight > 0) {
                    count++;
                    int jointIndex = l_bone_id[i];
                    vec4 tmpPos = joints_m[jointIndex] * vec4(l_pos, 1.0f);
                    initPos += weight * tmpPos;

                    vec4 tmpNormal = joints_m[jointIndex] * vec4(l_norm, 0.0);
                    initNormal += weight * tmpNormal;
                }
            }

            if (count == 0){
                initPos = vec4(l_pos, 1.0f);
                initNormal = vec4(l_norm, 0.0f);
            }

            gl_Position = perspective_m * view_m * l_instance_m * initPos;
        }else{
            initPos = vec4(l_pos, 1.0f);
            vs_out.localPos = l_instance_m * initPos;

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

            vs_out.mapTexCoords = vec2(x,1.0f - y);
            vec4 lPos = l_instance_m * vec4(0.0f,0.0f,0.0f,1.0f);

            if(lPos.x < 0){
                x = 0.0f;
            }else if(lPos.x > w){
                x = 1.0f;
            }else{
                x = lPos.x / w;
            }

            if(lPos.z < 0){
                y = 0.0f;
            }else if(lPos.z > h){
                y = 1.0f;
            }else{
                y = lPos.z / h;
            }

            vec2 lt = vec2(x, 1.0f - y);


            vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
            float visbl = 0;
            for(int i=0; i<6; i++){
                if(units[i].x != -1.0f){
                    vec4 elementPos = l_instance_m * initPos;
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
            /*float offset = (texture(heightMap, lt).r * gain) + 0.5f - 1.0f;
            vec4 res = perspective_m * view_m * l_instance_m * initPos;
            gl_Position = vec4(res.x,res.y + offset,res.z,res.w);*/
            gl_Position = perspective_m * view_m * l_instance_m * initPos;
            initNormal = vec4(l_norm,0.0f);
        }

        vec3 T = normalize(vec3(l_instance_m * vec4(l_tan,    0.0f)));
        vec3 B = normalize(vec3(l_instance_m * vec4(l_bi_tan, 0.0f)));
        vec3 N = normalize(vec3(l_instance_m * initNormal));
        mat3 TBN = mat3(T, B, N);

        vs_out.FragPos = vec3(l_instance_m * initPos);
        vs_out.Normal = mat3(transpose(inverse(l_instance_m))) * vec3(initNormal);
        vs_out.TexCoords = l_tex;
        vs_out.TangentViewPos  = TBN * viewPos;
        vs_out.TangentFragPos  = TBN * vs_out.FragPos;
        vs_out.ViewPos = viewPos;
        vs_out.FragPosLightSpace = lightSpaceMatrix * vec4(vs_out.FragPos, 1.0);
        vs_out.TBN = TBN;
        vs_out.alternative = alternative;
    }else{
        if(animated == 1){
            int count = 0;

            for(int i = 0; i < MAX_WEIGHTS; i++){
                float weight = l_bone_w[i];
                if(weight > 0) {
                    count++;
                    int jointIndex = l_bone_id[i];
                    vec4 tmpPos = joints_m[jointIndex] * vec4(l_pos, 1.0f);
                    initPos += weight * tmpPos;

                    vec4 tmpNormal = joints_m[jointIndex] * vec4(l_norm, 0.0);
                    initNormal += weight * tmpNormal;
                }
            }

            if (count == 0){
                initPos = vec4(l_pos, 1.0f);
                initNormal = vec4(l_norm, 0.0f);
            }

            gl_Position = perspective_m * view_m * model_m * initPos;
        }else{
            initPos = vec4(l_pos, 1.0f);
            vs_out.localPos = model_m * initPos;

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

            vs_out.mapTexCoords = vec2(x,1.0f - y);

            vec4[6]units = {unit0,unit1,unit2,unit3,unit4,unit5};
            float visbl = 0;
            for(int i=0; i<6; i++){
                if(units[i].x != -1.0f){
                    vec4 elementPos = model_m * initPos;
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
            if(alternative == 1){
                float offset = (texture(heightMap, vs_out.mapTexCoords).r * gain) + 0.5f - 1.0f;
                vec4 res = perspective_m * view_m * model_m * initPos;
                gl_Position = vec4(res.x,res.y + offset,res.z,res.w);
            }else{
                gl_Position = perspective_m * view_m * model_m * initPos;
            }
            initNormal = vec4(l_norm,0.0f);
        }

        vec3 norm = mat3(transpose(inverse(model_m))) * vec3(initNormal);
        //vec3 norm = vec3(initNormal);

        vec3 T = normalize(vec3(model_m * vec4(l_tan,    0.0f)));
        vec3 B = normalize(vec3(model_m * vec4(l_bi_tan, 0.0f)));
        vec3 N = normalize(vec3(model_m * vec4(norm,0.0f)));
        mat3 TBN = mat3(T, B, N);

        vs_out.FragPos = vec3(model_m * initPos);
        vs_out.Normal = norm;
        vs_out.TexCoords = vec2(l_tex.x,1.0f - l_tex.y);
        vs_out.TangentViewPos  = TBN * viewPos;
        vs_out.TangentFragPos  = TBN * vs_out.FragPos;
        vs_out.ViewPos = viewPos;
        vs_out.FragPosLightSpace = lightSpaceMatrix * vec4(vs_out.FragPos, 1.0);
        vs_out.TBN = TBN;
        vs_out.alternative = alternative;
    }
}
