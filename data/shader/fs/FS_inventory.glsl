#version 430

in vec2 texture_coord;

out vec4 FragColor;

uniform sampler2D image;
uniform float gamma;
uniform int onTarget;

void main() {
    vec4 color = texture(image, texture_coord);
    
    // Применяем gamma коррекцию
    color.rgb = pow(color.rgb, vec3(1.0 / gamma));
    
    // Подсветка при наведении
    if (onTarget == 1) {
        color.rgb *= 1.2;
    }
    
    // Отбрасываем прозрачные пиксели
    if (color.a < 0.1) {
        discard;
    }
    
    FragColor = color;
}
