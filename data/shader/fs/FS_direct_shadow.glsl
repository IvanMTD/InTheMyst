#version 430 core

uniform sampler2D image;

in vec2 texCoord;

void main(){
    vec4 fragColor = texture(image,texCoord);
    if(fragColor.a < 1.0f){
        discard;
    }
    gl_FragDepth = gl_FragCoord.z;
}