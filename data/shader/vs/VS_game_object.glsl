#version 430 core

const int MAX_JOINTS = 44;
const int MAX_WEIGHTS = 4;

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
uniform int animated;
uniform int instance;


// доп данные
uniform mat4 model_m;
uniform mat4 lightSpaceMatrix;
uniform vec3 viewPos;
uniform mat4 joints_m[MAX_JOINTS];

out VS_OUT {
     vec3 FragPos;
     vec3 Normal;
     vec2 TexCoords;
     vec3 TangentViewPos;
     vec3 TangentFragPos;
     vec3 ViewPos;
     vec4 FragPosLightSpace;
     mat3 TBN;
} vs_out;

void main() {

    vec4 initNormal = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    vec4 initPos = vec4(0.0f, 0.0f, 0.0f, 0.0f);

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
            gl_Position = perspective_m * view_m * model_m * initPos;
            initNormal = vec4(l_norm,0.0f);
        }

        vec3 T = normalize(vec3(model_m * vec4(l_tan,    0.0f)));
        vec3 B = normalize(vec3(model_m * vec4(l_bi_tan, 0.0f)));
        vec3 N = normalize(vec3(model_m * initNormal));
        mat3 TBN = mat3(T, B, N);

        vs_out.FragPos = vec3(model_m * initPos);
        vs_out.Normal = mat3(transpose(inverse(model_m))) * vec3(initNormal);
        vs_out.TexCoords = l_tex;
        vs_out.TangentViewPos  = TBN * viewPos;
        vs_out.TangentFragPos  = TBN * vs_out.FragPos;
        vs_out.ViewPos = viewPos;
        vs_out.FragPosLightSpace = lightSpaceMatrix * vec4(vs_out.FragPos, 1.0);
        vs_out.TBN = TBN;
    }
}
