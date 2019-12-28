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
} gs_out;

out flat int useShading;

void main() {
    if(battlefield == 1){
        float size = 0.5f;
        int r = radius - 1;
        float minX = localPoint.x - r - size;
        float maxX = localPoint.x + r + size;
        float minZ = localPoint.z - r - size;
        float maxZ = localPoint.z + r + size;

        int count = 0;
        for(int i=0; i<3; i++){
            if((minX <= gs_in[i].localPos.x && gs_in[i].localPos.x <= maxX) && (minZ <= gs_in[i].localPos.z && gs_in[i].localPos.z <= maxZ)){
                count++;
            }
        }

        if(count == 3){
            useShading = 0;
        }else{
            useShading = 1;
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
        EmitVertex();

        EndPrimitive();
    }else{
        useShading = 0;

        gl_Position = gl_in[0].gl_Position;
        gs_out.FragPos = gs_in[0].FragPos;
        gs_out.Normal = gs_in[0].Normal;
        gs_out.TexCoords = gs_in[0].TexCoords;
        gs_out.TangentViewPos = gs_in[0].TangentViewPos;
        gs_out.TangentFragPos = gs_in[0].TangentFragPos;
        gs_out.ViewPos = gs_in[0].ViewPos;
        gs_out.FragPosLightSpace = gs_in[0].FragPosLightSpace;
        gs_out.TBN = gs_in[0].TBN;
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
        EmitVertex();

        EndPrimitive();
    }
}
