#version 430 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
    float biom;
    float percent;
} gs_in[];

out GS_OUT{
    vec3 normal;
    vec2 cellTextureCoord;
    vec2 mapTextureCoord;
    float biom;
    float percent;
} gs_out;

void firstTriangle();
void secondTriangle();

void main(){
    vec3 v1 = gl_in[0].gl_Position.xyz;
    vec3 v2 = gl_in[1].gl_Position.xyz;

    vec3 config = v1 - v2;
    if(config.x != 0.0f && config.z != 0.0f){
        secondTriangle();
    }else{
        firstTriangle();
    }
    EndPrimitive();
}

void firstTriangle(){
    // треугольник 0,1,2
    vec3 vertex0 = gl_in[0].gl_Position.xyz;
    vec3 vertex1 = gl_in[1].gl_Position.xyz;
    vec3 vertex2 = gl_in[2].gl_Position.xyz;

    if(vertex0.y == vertex1.y && vertex0.y == vertex2.y){ // плоскость
        for(int i=0; i<3; i++){
            gl_Position = gl_in[i].gl_Position;
            gs_out.normal = gs_in[i].normal;
            gs_out.cellTextureCoord = gs_in[i].cellTextureCoord;
            gs_out.mapTextureCoord = gs_in[i].mapTextureCoord;
            gs_out.biom = gs_in[i].biom;
            gs_out.percent = gs_in[i].percent;
            EmitVertex();
        }
    }else if(vertex0.y > vertex1.y && vertex0.y > vertex2.y){ // X (НЕ ТРОГАТЬ!)
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y < vertex1.y && vertex0.y < vertex2.y){ // -X (НЕ ТРОГАТЬ!)
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(-1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(-1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(-1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(0.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y > vertex1.y && vertex0.y == vertex2.y){ // наклон в сторону -XZ -1,0,1
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = normalize(vec3(1.0f,0.0f,-1.0f));
        gs_out.cellTextureCoord = vec2(0.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = normalize(vec3(1.0f,0.0f,-1.0f));
        gs_out.cellTextureCoord = vec2(0.5f,0.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = normalize(vec3(1.0f,0.0f,-1.0f));
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y < vertex1.y && vertex0.y == vertex2.y){ // наклон в сторону X-Z 1,0,-1
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = normalize(vec3(-1.0f,0.0f,1.0f));
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = normalize(vec3(-1.0f,0.0f,1.0f));
        gs_out.cellTextureCoord = vec2(0.5f,1.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = normalize(vec3(-1.0f,0.0f,1.0f));
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y == vertex1.y && vertex0.y < vertex2.y){ // Z- (НЕ ТРОГАТЬ!)
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,-1.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,-1.0f);
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,-1.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y == vertex1.y && vertex0.y > vertex2.y){ // Z+ (НЕ ТРОГАТЬ!)
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,1.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,1.0f);
        gs_out.cellTextureCoord = vec2(0.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,1.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else{
        for(int i=0; i<3; i++){
            gl_Position = gl_in[i].gl_Position;
            gs_out.normal = gs_in[i].normal;
            gs_out.cellTextureCoord = gs_in[i].cellTextureCoord;
            gs_out.mapTextureCoord = gs_in[i].mapTextureCoord;
            gs_out.biom = gs_in[i].biom;
            gs_out.percent = gs_in[i].percent;
            EmitVertex();
        }
    }
}

void secondTriangle(){
    // треугольник 0,2,3
    vec3 vertex0 = gl_in[0].gl_Position.xyz;
    vec3 vertex2 = gl_in[1].gl_Position.xyz;
    vec3 vertex3 = gl_in[2].gl_Position.xyz;

    if(vertex0.y == vertex2.y && vertex0.y == vertex3.y){ // плоскость
        for(int i=0; i<3; i++){
            gl_Position = gl_in[i].gl_Position;
            gs_out.normal = gs_in[i].normal;
            gs_out.cellTextureCoord = gs_in[i].cellTextureCoord;
            gs_out.mapTextureCoord = gs_in[i].mapTextureCoord;
            gs_out.biom = gs_in[i].biom;
            gs_out.percent = gs_in[i].percent;
            EmitVertex();
        }
    }else if(vertex0.y > vertex2.y && vertex0.y > vertex3.y){ // Z
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,1.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,1.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,1.0f);
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y < vertex2.y && vertex0.y < vertex3.y){ // -Z
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,-1.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,-1.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(0.0f,0.0f,-1.0f);
        gs_out.cellTextureCoord = vec2(0.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y > vertex2.y && vertex0.y == vertex3.y){ // X (НЕ ТРОГАТЬ!)
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(0.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y < vertex2.y && vertex0.y == vertex3.y){ // -X (НЕ ТРОГАТЬ)
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = vec3(-1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = vec3(-1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = vec3(-1.0f,0.0f,0.0f);
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y == vertex2.y && vertex0.y > vertex3.y){ // наклон в сторону X -Z 1,0,-1
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = normalize(vec3(-1.0f,0.0f,1.0f));
        gs_out.cellTextureCoord = vec2(1.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = normalize(vec3(-1.0f,0.0f,1.0f));
        gs_out.cellTextureCoord = vec2(0.0f,1.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
                                                  gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = normalize(vec3(-1.0f,0.0f,1.0f));
        gs_out.cellTextureCoord = vec2(0.5f,0.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else if(vertex0.y == vertex2.y && vertex0.y < vertex3.y){ // наклон в сторону -X Z -1,0,1
        gl_Position = gl_in[0].gl_Position;
        gs_out.normal = normalize(vec3(1.0f,0.0f,-1.0f));
        gs_out.cellTextureCoord = vec2(0.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[0].mapTextureCoord;
        gs_out.biom = gs_in[0].biom;
        gs_out.percent = gs_in[0].percent;
        EmitVertex();

        gl_Position = gl_in[1].gl_Position;
        gs_out.normal = normalize(vec3(1.0f,0.0f,-1.0f));
        gs_out.cellTextureCoord = vec2(1.0f,0.0f);
        gs_out.mapTextureCoord = gs_in[1].mapTextureCoord;
        gs_out.biom = gs_in[1].biom;
        gs_out.percent = gs_in[1].percent;
        EmitVertex();

        gl_Position = gl_in[2].gl_Position;
        gs_out.normal = normalize(vec3(1.0f,0.0f,-1.0f));
        gs_out.cellTextureCoord = vec2(0.5f,1.0f);
        gs_out.mapTextureCoord = gs_in[2].mapTextureCoord;
        gs_out.biom = gs_in[2].biom;
        gs_out.percent = gs_in[2].percent;
        EmitVertex();
    }else{
        for(int i=0; i<3; i++){
            gl_Position = gl_in[i].gl_Position;
            gs_out.normal = gs_in[i].normal;
            gs_out.cellTextureCoord = gs_in[i].cellTextureCoord;
            gs_out.mapTextureCoord = gs_in[i].mapTextureCoord;
            gs_out.biom = gs_in[i].biom;
            gs_out.percent = gs_in[i].percent;
            EmitVertex();
        }
    }
}
