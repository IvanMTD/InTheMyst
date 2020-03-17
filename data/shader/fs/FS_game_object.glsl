#version 430 core

#define R 131073
#define G 131074
#define B 131075
#define A 131076

//const vec4 skyColor = vec4(0.2f,0.4f,0.5f,1.0f);
const vec4 skyColor = vec4(0.0f,0.0f,0.0f,1.0f);
//const vec4 skyColor = vec4(0.5f,0.5f,0.5f,1.0f);

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;
layout (location = 2) out vec4 bright_color;

// Входящий Блок
in GS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoords;
    vec2 mapTexCoords;
    vec3 ViewPos;
    vec4 FragPosLightSpace;
    float face;
}fs_in;

// Структуры и Юниформы
struct Material{
    sampler2D ambientMap;
    sampler2D diffuseMap;
    sampler2D specularMap;
    sampler2D normalMap;
    sampler2D displaceMap;

    sampler2D desert_low;
    sampler2D desert_up;

    sampler2D steppe_low;
    sampler2D steppe_up;

    sampler2D plain_low;
    sampler2D plain_up;

    sampler2D forest_low;
    sampler2D forest_up;

    sampler2D mountains_low;
    sampler2D mountains_up;

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

in flat int useShading;
in flat int useBorder;

// target highlight
uniform int instance;
uniform int w;
uniform int h;
uniform float currentHeight;
uniform int onTarget;
uniform float radiance;
uniform float shininess;
uniform Material material;
uniform DirectLight directLight;
uniform sampler2D heightMap;
uniform sampler2D shadowMap;
uniform sampler2D map;
uniform int group;
uniform float id;

//vec2 textureCoordinate;

// Функции
vec4 targetHighlight();
vec3 getDirectLight(DirectLight, vec3, vec3, vec2);
float shadowCalculation(vec4, vec3, vec3);
int getInt(float);
vec3 getDiffuseMap(vec2);

void main() {
    vec4 color = texture(map,fs_in.mapTexCoords);
    //vec4 color = vec4(1.0f,1.0f,1.0f,1.0f);

    vec3 viewDirection = normalize(fs_in.ViewPos - fs_in.FragPos);
    vec3 normal = fs_in.Normal;

    if(onTarget == 1){
        fragment_color = targetHighlight();
    }else{
        if(useShading == 0 && useBorder == 0){
            vec3 result = getDirectLight(directLight, normal, viewDirection,fs_in.TexCoords);
            //float alpha = texture(material.diffuseMap,fs_in.TexCoords).a;
            fragment_color = vec4(result, 1.0f);
            /*if(color.r > 0.0f){
                fragment_color = mix(skyColor,fragment_color, color.r);
            }else{
                fragment_color = skyColor;
            }*/
        }else{
            if(useBorder == 1){
                vec3 result = getDirectLight(directLight, normal, viewDirection, fs_in.TexCoords);
                //float alpha = texture(material.diffuseMap,fs_in.TexCoords).a;
                float rad = radiance * radiance;
                fragment_color = vec4(result.r * rad, result.g / radiance, result.b / radiance, 1.0f);
            }
            if(useShading == 1){
                vec3 result = getDirectLight(directLight, normal, viewDirection, fs_in.TexCoords);
                //float alpha = texture(material.diffuseMap,fs_in.TexCoords).a;
                fragment_color = vec4(result / 10.0f, 1.0f);
                /*if(color.r > 0.0f){
                    fragment_color = mix(skyColor,fragment_color, color.r);
                }else{
                    fragment_color = skyColor;
                }*/
            }
        }
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

vec3 getDirectLight(DirectLight light, vec3 normal, vec3 viewDirection, vec2 tex){
    vec3 diffuseMap;// = vec3(texture(material.diffuseMap, tex));

    vec4 desert_low = texture(material.desert_low,tex);
    vec4 desert_up = texture(material.desert_up,tex);
    vec4 steppe_low = texture(material.steppe_low,tex);
    vec4 steppe_up = texture(material.steppe_up,tex);
    vec4 plain_low = texture(material.plain_low,tex);
    vec4 plain_up = texture(material.plain_up,tex);
    vec4 forest_low = texture(material.forest_low,tex);
    vec4 forest_up = texture(material.forest_up,tex);
    vec4 mountains_low = texture(material.mountains_low,tex);
    vec4 mountains_up = texture(material.mountains_up,tex);
    float normalHeight = texture(heightMap, vec2(fs_in.mapTexCoords.x, 1.0f - fs_in.mapTexCoords.y)).r;
    float height = currentHeight + ((normalHeight + 0.5f - 1.0f) * 10.0f);

    if(instance == 0){
        if(normal.y > 0.0f){
            diffuseMap = getDiffuseMap(tex);
        }else{
            if(fs_in.face == 0.0f){
                int num = getInt(tex.x * 10.0f) % 3;
                if(num == 0){
                    if(tex.y <= 0.1f){
                        diffuseMap = getDiffuseMap(tex);
                    }else{
                        if(currentHeight < 10.0f){
                            diffuseMap = vec3(desert_low);
                        }else if(currentHeight < 20.0f){
                            diffuseMap = vec3(steppe_low);
                        }else if(currentHeight < 30.0f){
                            diffuseMap = vec3(plain_low);
                        }else if(currentHeight < 40.0f){
                            diffuseMap = vec3(forest_low);
                        }else{
                            diffuseMap = vec3(mountains_low);
                        }
                    }
                }else if(num == 1){
                    if(tex.y <= 0.2f){
                        diffuseMap = getDiffuseMap(tex);
                    }else{
                        if(currentHeight < 10.0f){
                            diffuseMap = vec3(desert_low);
                        }else if(currentHeight < 20.0f){
                            diffuseMap = vec3(steppe_low);
                        }else if(currentHeight < 30.0f){
                            diffuseMap = vec3(plain_low);
                        }else if(currentHeight < 40.0f){
                            diffuseMap = vec3(forest_low);
                        }else{
                            diffuseMap = vec3(mountains_low);
                        }
                    }
                }else if(num == 2){
                    if(tex.y <= 0.15f){
                        diffuseMap = getDiffuseMap(tex);
                    }else{
                        if(currentHeight < 10.0f){
                            diffuseMap = vec3(desert_low);
                        }else if(currentHeight < 20.0f){
                            diffuseMap = vec3(steppe_low);
                        }else if(currentHeight < 30.0f){
                            diffuseMap = vec3(plain_low);
                        }else if(currentHeight < 40.0f){
                            diffuseMap = vec3(forest_low);
                        }else{
                            diffuseMap = vec3(mountains_low);
                        }
                    }
                }
            }else{
                if(currentHeight < 10.0f){
                    diffuseMap = vec3(desert_low);
                }else if(currentHeight < 20.0f){
                    diffuseMap = vec3(steppe_low);
                }else if(currentHeight < 30.0f){
                    diffuseMap = vec3(plain_low);
                }else if(currentHeight < 40.0f){
                    diffuseMap = vec3(forest_low);
                }else{
                    diffuseMap = vec3(mountains_low);
                }
            }
        }
    }else{
        diffuseMap = vec3(texture(material.diffuseMap, tex));
    }
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
    vec3 ambient  = light.ambient  * diffuseMap;
    vec3 diffuse  = light.diffuse  * diff * diffuseMap;
    vec3 specular = light.specular * spec * diffuseMap;

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
       float bias = max(0.005f * (1.0f - dot(normal, lightDir)), 0.001f); // 0.005f

       float shadow = 0.0f;
       vec2 texelSize = 0.25f / textureSize(shadowMap, 0);
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

int getInt(float num){
    int newNum = 0;
    if(num < 1.0f){
        newNum = 0;
    }else if(num < 2.0f){
        newNum = 1;
    }else if(num < 3.0f){
        newNum = 2;
    }else if(num < 4.0f){
        newNum = 3;
    }else if(num < 5.0f){
        newNum = 4;
    }else if(num < 6.0f){
        newNum = 5;
    }else if(num < 7.0f){
        newNum = 6;
    }else if(num < 8.0f){
        newNum = 7;
    }else if(num < 9.0f){
        newNum = 8;
    }else if(num < 10.0f){
        newNum = 9;
    }else{
        newNum = 10;
    }
    return newNum;
}

vec3 getDiffuseMap(vec2 tex){
    vec3 diffuseMap;

    vec4 desert_low = texture(material.desert_low,tex);
    vec4 desert_up = texture(material.desert_up,tex);
    vec4 steppe_low = texture(material.steppe_low,tex);
    vec4 steppe_up = texture(material.steppe_up,tex);
    vec4 plain_low = texture(material.plain_low,tex);
    vec4 plain_up = texture(material.plain_up,tex);
    vec4 forest_low = texture(material.forest_low,tex);
    vec4 forest_up = texture(material.forest_up,tex);
    vec4 mountains_low = texture(material.mountains_low,tex);
    vec4 mountains_up = texture(material.mountains_up,tex);
    float normalHeight = texture(heightMap, vec2(fs_in.mapTexCoords.x, 1.0f - fs_in.mapTexCoords.y)).r;
    float height = currentHeight + ((normalHeight + 0.5f - 1.0f) * 10.0f);

    // desert
    if(height < 0.3f){
        diffuseMap = vec3(desert_low);
    }else if(height < 0.5f){
        diffuseMap = vec3(desert_low.r + 0.1f,desert_low.g + 0.1f,desert_low.b);
    }else if(height < 0.75f){
        diffuseMap = vec3(desert_up - 0.1f);
    }else if(height < 1.0f){
        diffuseMap = vec3(desert_up);
    }else if(height < 3.5f){
        diffuseMap = vec3(desert_up + 0.05f);
    }else if(height < 7.0f){
        diffuseMap = vec3(desert_up + 0.1f);
    }else if(height < 8.0f){
        diffuseMap = vec3(mix(desert_low,desert_up,0.25f));
    }else if(height < 9.0f){
        diffuseMap = vec3(desert_up);
    }else if(height < 10.0f){
        diffuseMap = vec3(desert_up - 0.15f);
    }

    // steppe
    else if(height < 10.3f){
        diffuseMap = vec3(steppe_low);
    }else if(height < 10.6f){
        diffuseMap = vec3(mix(steppe_low,steppe_up,0.3f));
    }else if(height < 11.0f){
        diffuseMap = vec3(mix(steppe_low,steppe_up,0.4f));
    }else if(height < 13.0f){
        diffuseMap = vec3(mix(steppe_low,steppe_up,0.5f));
    }else if(height < 16.0f){
        diffuseMap = vec3(mix(steppe_low,steppe_up,0.6f));
    }else if(height < 20.0f){
        diffuseMap = vec3(steppe_up);
    }

    // plain
    else if(height < 20.3f){
        diffuseMap = vec3(plain_low - 0.1f);
    }else if(height < 20.8f){
        diffuseMap = vec3(plain_low);
    }else if(height < 22.5f){
        diffuseMap = vec3(plain_up - 0.1f);
    }else if(height < 28.5f){
        diffuseMap = vec3(plain_up);
    }else if(height < 30.0f){
        diffuseMap = vec3(plain_up - 0.1f);
    }

    // forest
    else if(height < 30.5f){
        diffuseMap = vec3(forest_low - 0.07f);
    }else if(height < 31.0f){
        diffuseMap = vec3(forest_low);
    }else if(height < 36.5f){
        diffuseMap = vec3(forest_up);
    }else if(height < 40.0f){
        diffuseMap = vec3(forest_up - 0.04f);
    }

    // mountains
    else if(height < 40.5f){
        diffuseMap = vec3(mountains_low);
    }else if(height < 41.0){
        diffuseMap = vec3(mountains_low + 0.05f);
    }else if(height < 43.0f){
        diffuseMap = vec3(mountains_up);
    }else{
        diffuseMap = vec3(mountains_up + 0.3f);
    }

    return diffuseMap;
}

/*vec4 blenMapColour = texture(heightMap,vec2(fs_in.mapTexCoords.x, 1.0f - fs_in.mapTexCoords.y));
float backTextureAmount = 1.0f - (blenMapColour.r + blenMapColour.g + blenMapColour.b);
vec4 backgrond = texture(material.lowDiffMap,tex) * backTextureAmount;
vec4 upperTexture = texture(material.upDiffMap,tex) * blenMapColour.r;
diffuseMap = vec3(backgrond + upperTexture);*/
