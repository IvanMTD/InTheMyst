#version 430 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

uniform int battlefield;
uniform vec3 localPoint;
uniform int radius;

in VS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoords;
    vec3 TangentViewPos;
    vec3 TangentFragPos;
    vec3 ViewPos;
    vec4 FragPosLightSpace;
    mat3 TBN;
    vec4 localPos;
    float visibility;
} gs_in[];

out GS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoords;
    vec3 TangentViewPos;
    vec3 TangentFragPos;
    vec3 ViewPos;
    vec4 FragPosLightSpace;
    mat3 TBN;
    vec4 localPos;
    float visibility;
} gs_out;

out flat int useShading;
out flat int useBorder;

void main() {
    if(battlefield == 1){
        float size = 0.5f;
        int r = radius - 1;
        float minX = localPoint.x - r - size;
        float maxX = localPoint.x + r + size;
        float minZ = localPoint.z - r - size;
        float maxZ = localPoint.z + r + size;

        float minW = localPoint.x - radius - size;
        float maxW = localPoint.x + radius + size;
        float minH = localPoint.z - radius - size;
        float maxH = localPoint.z + radius + size;

        int count = 0;
        for(int i=0; i<3; i++){
            if((minX <= gs_in[i].localPos.x && gs_in[i].localPos.x <= maxX) && (minZ <= gs_in[i].localPos.z && gs_in[i].localPos.z <= maxZ)){
                count++;
            }
        }
        if(count == 3){
            useBorder = 0;
        }else{
            useBorder = 1;
        }

        count = 0;
        for(int i=0; i<3; i++){
            if((minW <= gs_in[i].localPos.x && gs_in[i].localPos.x <= maxW) && (minH <= gs_in[i].localPos.z && gs_in[i].localPos.z <= maxH)){
                count++;
            }
        }
        if(count == 3){
            useShading = 0;
        }else{
            useShading = 1;
        }
    }else{
        useShading = 0;
        useBorder = 0;
    }

    gl_Position = gl_in[0].gl_Position;
    gs_out.FragPos = gs_in[0].FragPos;
    gs_out.Normal = gs_in[0].Normal;
    gs_out.TexCoords = gs_in[0].TexCoords;
    gs_out.TangentViewPos = gs_in[0].TangentViewPos;
    gs_out.TangentFragPos = gs_in[0].TangentFragPos;
    gs_out.ViewPos = gs_in[0].ViewPos;
    gs_out.FragPosLightSpace = gs_in[0].FragPosLightSpace;
    gs_out.TBN = gs_in[0].TBN;
    gs_out.localPos = gs_in[0].localPos;
    gs_out.visibility = gs_in[0].visibility;
    EmitVertex();

    gl_Position = gl_in[1].gl_Position;
    gs_out.FragPos = gs_in[1].FragPos;
    gs_out.Normal = gs_in[1].Normal;
    gs_out.TexCoords = gs_in[1].TexCoords;
    gs_out.TangentViewPos = gs_in[1].TangentViewPos;
    gs_out.TangentFragPos = gs_in[1].TangentFragPos;
    gs_out.ViewPos = gs_in[1].ViewPos;
    gs_out.FragPosLightSpace = gs_in[1].FragPosLightSpace;
    gs_out.TBN = gs_in[1].TBN;
    gs_out.localPos = gs_in[1].localPos;
    gs_out.visibility = gs_in[1].visibility;
    EmitVertex();

    gl_Position = gl_in[2].gl_Position;
    gs_out.FragPos = gs_in[2].FragPos;
    gs_out.Normal = gs_in[2].Normal;
    gs_out.TexCoords = gs_in[2].TexCoords;
    gs_out.TangentViewPos = gs_in[2].TangentViewPos;
    gs_out.TangentFragPos = gs_in[2].TangentFragPos;
    gs_out.ViewPos = gs_in[2].ViewPos;
    gs_out.FragPosLightSpace = gs_in[2].FragPosLightSpace;
    gs_out.TBN = gs_in[2].TBN;
    gs_out.localPos = gs_in[2].localPos;
    gs_out.visibility = gs_in[2].visibility;
    EmitVertex();

    EndPrimitive();
}
