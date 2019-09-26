#version 430 core

#define R 131073
#define G 131074
#define B 131075
#define A 131076

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

// Входящий Блок
in VS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoords;
    vec3 TangentViewPos;
    vec3 TangentFragPos;
    vec3 ViewPos;
    vec4 FragPosLightSpace;
    mat3 TBN;
}fs_in;

// Структуры и Юниформы
struct Material{
    sampler2D ambientMap;
    sampler2D diffuseMap;
    sampler2D specularMap;
    sampler2D normalMap;
    sampler2D displaceMap;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct DirectLight{
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

// target highlight
uniform int onTarget;
uniform float shininess;
uniform Material material;
uniform DirectLight directLight;
uniform sampler2D shadowMap;
uniform int group;
uniform float id;

//vec2 textureCoordinate;

// Функции
vec4 targetHighlight();
vec3 getDirectLight(DirectLight, vec3, vec3);
float shadowCalculation(vec4, vec3, vec3);

void main() {

    vec3 viewDirection = normalize(fs_in.ViewPos - fs_in.FragPos);
    //vec3 viewDirection = normalize(fs_in.TangentViewPos - fs_in.TangentFragPos);
    vec3 normal = fs_in.Normal;
    //vec3 normal = texture(material.normalMap,fs_in.TexCoords).rgb;
    //normal = normalize(normal * 2.0 - 1.0);
    //normal = normalize(fs_in.TBN * normal);

    if(onTarget == 1){
        fragment_color = targetHighlight();
    }else{
        vec3 result = getDirectLight(directLight, normal, viewDirection);
        float alpha = texture(material.diffuseMap,fs_in.TexCoords).a;
        fragment_color = vec4(result, alpha);
    }

    vec4 rgba = texture(material.diffuseMap,fs_in.TexCoords);
    if(group == R){
        select_color = vec4(id, 0.0f, 0.0f, 1.0f);
    }else if(group == G){
        select_color = vec4(0.0f, id, 0.0f, 1.0f);
    }else if(group == B){
        select_color = vec4(0.0f, 0.0f, id, rgba.a);
    }else if(group == A){
        select_color = vec4(0.0f, 0.0f, 0.0f, id);
    }

    // проверка фрагмента на превышение по яркости заданного порога
    // если ярче - вывести в отдельный буфер, хранящий яркие участки
    float brightness = dot(fragment_color.rgb, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0){
        bright_color = vec4(fragment_color.rgb, 1.0);
    }
    else{
        bright_color = vec4(0.0, 0.0, 0.0, 1.0);
    }
}

vec4 targetHighlight(){
    vec4 img = texture(material.diffuseMap, fs_in.TexCoords);
    vec4 result = vec4(vec3(img.rgb * 2.0f), img.a);
    return result;
}

vec3 getDirectLight(DirectLight light, vec3 normal, vec3 viewDirection){
    //vec3 lightDir = normalize(light.position - fs_in.TangentFragPos);
    vec3 lightDir = normalize(light.position);
    // диффузное освещение
    float diff = max(dot(normal, lightDir), 0.0);
    // освещение зеркальных бликов
    //vec3 halfwayDir = normalize(lightDir + viewDirection);
    //float spec = pow(max(dot(normal, halfwayDir), 0.0f), shininess);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(normal, reflectDir), 0.0), shininess);
    // комбинируем результаты
    vec3 ambient  = light.ambient  * vec3(texture(material.diffuseMap, fs_in.TexCoords));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(material.diffuseMap, fs_in.TexCoords));
    vec3 specular = light.specular * spec * vec3(texture(material.diffuseMap, fs_in.TexCoords));

    float shadow = shadowCalculation(fs_in.FragPosLightSpace, lightDir, normal);
    vec3 lighting = ambient + (1.0 - shadow) * (diffuse + specular);

    return lighting;
}

float shadowCalculation(vec4 fragPosLightSpace, vec3 lightDir, vec3 normal){
       // perform perspective divide
       vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
       // transform to [0,1] range
       projCoords = projCoords * 0.5 + 0.5;
       // get depth of current fragment from light's perspective
       float currentDepth = projCoords.z;
       // check whether current frag pos is in shadow
       float bias = max(0.005f * (1.0f - dot(normal, lightDir)), 0.001f);

       float shadow = 0.0f;
       vec2 texelSize = 1.0f / textureSize(shadowMap, 0);
       int value = 5;
       for(int x = -value; x <= value; ++x){
           for(int y = -value; y <= value; ++y){
               float pcfDepth = texture(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
               shadow += currentDepth - bias > pcfDepth ? 1.0f : 0.0f;
           }
       }

       shadow /= 125.0f;

       if(projCoords.z > 1.0f){
            shadow = 0.0f;
       }

       return shadow;
}