#version 330 core

out vec4 fragment_color;

in vec2 texture_coord;

uniform sampler2D main_texture;
uniform sampler2D blur_texture;
uniform float gamma;

/*void main(){
    //FragColor = texture(screenTexture, TexCoord);
    //FragColor = vec4(vec3(1.0 - texture(screenTexture, TexCoord)), 1.0); // Инверсия цвета
    FragColor = texture(screenTexture, TexCoord);
    float average = 0.2126 * FragColor.r + 0.7152 * FragColor.g + 0.0722 * FragColor.b; // Градации серого
    FragColor = vec4(average, average, average, 1.0); // Градации серого
}*/

const float offset = 1.0 / 300.0;
float exposure = 1.0f;

void main(){

    vec2 offsets[9] = vec2[](
        vec2(-offset,  offset), // top-left
        vec2( 0.0f,    offset), // top-center
        vec2( offset,  offset), // top-right
        vec2(-offset,  0.0f),   // center-left
        vec2( 0.0f,    0.0f),   // center-center
        vec2( offset,  0.0f),   // center-right
        vec2(-offset, -offset), // bottom-left
        vec2( 0.0f,   -offset), // bottom-center
        vec2( offset, -offset)  // bottom-right
    );

    float kernel_sharpness[9] = float[]( // Ядро - резкости
        -1, -1, -1,
        -1,  9, -1,
        -1, -1, -1
    );

    float kernel_blur[9] = float[]( // Ядро - размытия
        1.0 / 16, 2.0 / 16, 1.0 / 16,
        2.0 / 16, 4.0 / 16, 2.0 / 16,
        1.0 / 16, 2.0 / 16, 1.0 / 16
    );

    float kernel_inverse[9] = float[](
        1, 1, 1,
        1,-8, 1,
        1, 1, 1
    );

    float kernel_std[9] = float[](
         0,0,0,
         0,1,0,
         0,0,0
    );

    vec3 sampleTex[9];

    for(int i = 0; i < 9; i++){
        sampleTex[i] = vec3(texture(main_texture, texture_coord.st + offsets[i]));
    }

    vec3 col = vec3(0.0);

    for(int i = 0; i < 9; i++){
        col += sampleTex[i] * kernel_std[i];
    }

    // hdr
    vec3 hdrColor = col.rgb;
    // bloom
    vec3 bloomColor = texture(blur_texture,texture_coord).rgb;
    hdrColor += bloomColor;
    // reinhard tone mapping
    vec3 mapped = vec3(1.0) - exp(-hdrColor * exposure);
    // gamma correction
    mapped = pow(mapped, vec3(1.0 / gamma));
    fragment_color = vec4(mapped, 1.0);
    //fragment_color = texture(blur_texture,texture_coord);
}

