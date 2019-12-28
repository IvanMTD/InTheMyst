#version 430 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

uniform int battlefield;
uniform vec3 localPoint;
uniform int radius;

in VS_OUT{
    vec2 textureCoord;
    flat int isBoard;
    flat int isGrid;
    vec4 localPos;
}gs_in[];

out vec2 textureCoord;
out flat int isBoard;
out flat int isGrid;
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
        textureCoord = gs_in[0].textureCoord;
        isBoard = gs_in[0].isBoard;
        isGrid = gs_in[0].isGrid;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        textureCoord = gs_in[1].textureCoord;
        isBoard = gs_in[1].isBoard;
        isGrid = gs_in[1].isGrid;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        textureCoord = gs_in[2].textureCoord;
        isBoard = gs_in[2].isBoard;
        isGrid = gs_in[2].isGrid;
        EmitVertex();

        EndPrimitive();
    }else{
        useShading = 0;

        gl_Position = gl_in[0].gl_Position;
        textureCoord = gs_in[0].textureCoord;
        isBoard = gs_in[0].isBoard;
        isGrid = gs_in[0].isGrid;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        textureCoord = gs_in[1].textureCoord;
        isBoard = gs_in[1].isBoard;
        isGrid = gs_in[1].isGrid;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        textureCoord = gs_in[2].textureCoord;
        isBoard = gs_in[2].isBoard;
        isGrid = gs_in[2].isGrid;
        EmitVertex();

        EndPrimitive();
    }
}
