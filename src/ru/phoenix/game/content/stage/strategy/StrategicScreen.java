package ru.phoenix.game.content.stage.strategy;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.model.Vertex;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class StrategicScreen {
    private Shader shader;
    private Mesh mesh;

    private Texture blendTexture;

    private long seed;
    private float xOffset;
    float accuracy;

    private float lastTime;

    public StrategicScreen() {
        shader = new Shader();
        seed = (long)(1 + Math.random() * 10000000000L);
        xOffset = 0.0f;
        lastTime = (float)glfwGetTime();
        accuracy = 20.0f + (float)Math.random() * 30.0f;
    }

    public void init(){

        shader.createVertexShader("VS_strategic.glsl");
        shader.createFragmentShader("FS_strategic.glsl");
        shader.createProgram();

        List<Vertex> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int index = 0;
        for(int x=0; x<200; x++){
            for(int z=0; z<200; z++){
                Vertex v1 = new Vertex();
                Vertex v2 = new Vertex();
                Vertex v3 = new Vertex();
                Vertex v4 = new Vertex();

                v1.setPosition(new Vector3f(x,0.0f,z));
                v2.setPosition(new Vector3f(x + 1.0f,0.0f,z));
                v3.setPosition(new Vector3f(x + 1.0f,0.0f,z + 1.0f));
                v4.setPosition(new Vector3f(x,0.0f,z + 1.0f));

                v1.setNormal(new Vector3f(0.0f,1.0f,0.0f));
                v2.setNormal(new Vector3f(0.0f,1.0f,0.0f));
                v3.setNormal(new Vector3f(0.0f,1.0f,0.0f));
                v4.setNormal(new Vector3f(0.0f,1.0f,0.0f));

                v1.setTexCoords(new Vector2f(0.0f,1.0f));
                v2.setTexCoords(new Vector2f(1.0f,1.0f));
                v3.setTexCoords(new Vector2f(1.0f,0.0f));
                v4.setTexCoords(new Vector2f(0.0f,0.0f));

                float w = (float)x;
                float h = (float)z;
                float t = 200.0f;

                v1.setTangents(new Vector3f(w/t,h/t,0.0f));
                v2.setTangents(new Vector3f((w + 1.0f)/t,h/t,0.0f));
                v3.setTangents(new Vector3f((w + 1.0f)/t,(h + 1.0f)/t,0.0f));
                v4.setTangents(new Vector3f(w/t,(h + 1.0f)/t,0.0f));

                vertices.add(v1);
                vertices.add(v2);
                vertices.add(v3);
                vertices.add(v4);

                indices.add(index++);
                indices.add(index++);
                indices.add(index);
                index -= 2;
                indices.add(index);
                index += 2;
                indices.add(index++);
                indices.add(index++);
            }
        }
        mesh = new Mesh(new ArrayList<>(vertices),new ArrayList<>(indices),null);

        blendTexture = createBlendMap(xOffset);
    }

    public void update(){
        float currentTime = (float)glfwGetTime();
        float diff = currentTime - lastTime;
        if(diff > 0.0166f) {
            xOffset += 0.1f;
            blendTexture = createBlendMap(xOffset);
            lastTime = (float)glfwGetTime();
        }
    }

    public void draw(){
        shader.useProgram();

        shader.setUniformBlock("matrices",0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, blendTexture.getTextureID());
        shader.setUniform("blendMap", 0);

        mesh.draw();
    }

    private Texture createBlendMap(float offset){
        Vector3f[][] heiMap = new Vector3f[100][100];
        Perlin2D perlin = new Perlin2D(seed);
        for(int x=0; x<heiMap.length; x++){
            for(int z=0; z<heiMap[0].length; z++){
                float value = perlin.getNoise(((float)x + offset)/accuracy,z/accuracy,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                heiMap[x][z] = new Vector3f(result, result, result);
            }
        }

        Texture texture = new Texture2D();
        texture.setup(heiMap,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        return texture;
    }
}
