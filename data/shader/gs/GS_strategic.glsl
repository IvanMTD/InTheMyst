#version 430 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 6) out;

in VS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
} gs_in[];

out GS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
    vec3 color;
} gs_out;

float getAlignmentHeight(vec4);
void totalAlignment(int,vec4,float,vec3);

void main(){
    float referenceHeight = getAlignmentHeight(gl_in[0].gl_Position);
    for(int i=0; i<3; i++){
        totalAlignment(i,gl_in[i].gl_Position,referenceHeight, vec3(0.0f,0.5f,0.0f));
    }

    totalAlignment(2,gl_in[2].gl_Position,referenceHeight + 1.0f, vec3(0.0f,0.3f,0.3f));
    totalAlignment(0,gl_in[0].gl_Position,referenceHeight + 1.0f, vec3(0.4f,0.0f,0.1f));
    totalAlignment(1,gl_in[1].gl_Position,referenceHeight + 1.0f, vec3(0.4f,0.0f,0.1f));

    EndPrimitive();
}

float getAlignmentHeight(vec4 position){
    float f = floor(position.y);
    float h = position.y - f;
    if(h < 0.5f){
        h = f;
    }else if(h > 0.5f){
        h = f + 1.0f;
    }else{
        h = round(position.y);
    }
    return h;
}

void totalAlignment(int index, vec4 position, float height, vec3 color){
    gs_out.normal = gs_in[index].normal;
    gs_out.cellTextureCoord = gs_in[index].cellTextureCoord;
    gs_out.mapTextureCoord = gs_in[index].mapTextureCoord;
    gs_out.color = color;
    gl_Position = vec4(position.x,height,position.z,position.w);
    EmitVertex();
}
