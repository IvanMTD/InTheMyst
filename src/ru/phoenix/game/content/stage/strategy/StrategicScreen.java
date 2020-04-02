package ru.phoenix.game.content.stage.strategy;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
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

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class StrategicScreen {

    private Shader shader;
    private Mesh mesh;

    List<List<Vector3f>> heighMap;
    List<List<Vector3f>> blendMap;
    private Texture blendTexture;
    private Texture heightTexture;

    private Texture desertLow;
    private Texture desertSide;
    private Texture desertUp;

    private Texture steppeLow;
    private Texture steppeSide;
    private Texture steppeUp;

    private Texture plainLow;
    private Texture plainSide;
    private Texture plainUp;

    private Texture forestLow;
    private Texture forestSide;
    private Texture forestUp;

    private Texture mountainsLow;
    private Texture mountainsSide;
    private Texture mountainsUp;

    private Texture road;

    private long seed;
    private float xOffset;
    private float accuracy;

    private float lastTime;

    private float biom;
    private float biomColor;

    private float camPos;
    private Vector3f startPos;

    public StrategicScreen() {
        shader = new Shader();
        blendMap = new ArrayList<>();
        heighMap = new ArrayList<>();

        seed = (long)(1 + Math.random() * 10000000000L);
        xOffset = 0.0f;
        lastTime = (float)glfwGetTime();
        accuracy = 20.0f + (float)Math.random() * 30.0f;

        desertLow = new Texture2D();
        desertSide = new Texture2D();
        desertUp = new Texture2D();

        steppeLow = new Texture2D();
        steppeSide = new Texture2D();
        steppeUp = new Texture2D();

        plainLow = new Texture2D();
        plainSide = new Texture2D();
        plainUp = new Texture2D();

        forestLow = new Texture2D();
        forestSide = new Texture2D();
        forestUp = new Texture2D();

        mountainsLow = new Texture2D();
        mountainsSide = new Texture2D();
        mountainsUp = new Texture2D();

        road = new Texture2D();

        camPos = 0.0f;
    }

    public void init(float biom){

        startPos = Camera.getInstance().getPos();

        shader.createVertexShader("VS_strategic.glsl");
        shader.createGeometryShader("GS_strategic.glsl");
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

        biomColor = biom;
        createBlendMap(biom);

        desertLow.setup(null,"./data/content/texture/atlas/graund/desert_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        desertSide.setup(null,"./data/content/texture/atlas/graund/desert_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        desertUp.setup(null,"./data/content/texture/atlas/graund/desert_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        steppeLow.setup(null,"./data/content/texture/atlas/graund/steppe_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        steppeSide.setup(null,"./data/content/texture/atlas/graund/steppe_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        steppeUp.setup(null,"./data/content/texture/atlas/graund/steppe_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        plainLow.setup(null,"./data/content/texture/atlas/graund/plain_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        plainSide.setup(null,"./data/content/texture/atlas/graund/plain_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        plainUp.setup(null,"./data/content/texture/atlas/graund/plain_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        forestLow.setup(null,"./data/content/texture/atlas/graund/forest_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        forestSide.setup(null,"./data/content/texture/atlas/graund/forest_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        forestUp.setup(null,"./data/content/texture/atlas/graund/forest_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        mountainsLow.setup(null,"./data/content/texture/atlas/graund/mountains_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        mountainsSide.setup(null,"./data/content/texture/atlas/graund/mountains_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        mountainsUp.setup(null,"./data/content/texture/atlas/graund/mountains_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        road.setup(null,"./data/content/texture/atlas/graund/road.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    public void update(float biom){
        biomColor = biom;
        if(!Input.getInstance().isPressed(GLFW_KEY_E)) {
            float currentTime = (float) glfwGetTime();
            float diff = currentTime - lastTime;
            if (diff > 0.0166f) { // 0.0166f // 0.033333f
                float size = 0.1f;
                camPos += size;
                if (camPos > 1.0f) {
                    camPos = 0.0f;
                    Camera.getInstance().setPos(startPos);
                    Camera.getInstance().updateViewMatrix();

                    if (this.biom != biom) {
                        if (this.biom < biom) {
                            this.biom += 0.1f;
                            if (this.biom >= biom) {
                                this.biom = biom;
                            }
                        } else if (this.biom > biom) {
                            this.biom -= 0.1f;
                            if (this.biom <= biom) {
                                this.biom = biom;
                            }
                        }
                    }

                    xOffset++;
                    updateBlendMap(xOffset,this.biom);
                } else {
                    Camera.getInstance().setPos(Camera.getInstance().getPos().add(new Vector3f(size, 0.0f, 0.0f)));
                    Camera.getInstance().updateViewMatrix();
                }
                lastTime = (float) glfwGetTime();
            }
        }
    }

    public void draw(){
        shader.useProgram();

        shader.setUniformBlock("matrices",0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, blendTexture.getTextureID());
        shader.setUniform("blendMap", 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, heightTexture.getTextureID());
        shader.setUniform("heightMap", 1);

        glActiveTexture(GL_TEXTURE10);
        glBindTexture(GL_TEXTURE_2D, desertLow.getTextureID());
        shader.setUniform("material.desert_low", 10);

        glActiveTexture(GL_TEXTURE11);
        glBindTexture(GL_TEXTURE_2D, desertSide.getTextureID());
        shader.setUniform("material.desert_side", 11);

        glActiveTexture(GL_TEXTURE12);
        glBindTexture(GL_TEXTURE_2D, desertUp.getTextureID());
        shader.setUniform("material.desert_up", 12);

        glActiveTexture(GL_TEXTURE13);
        glBindTexture(GL_TEXTURE_2D, steppeLow.getTextureID());
        shader.setUniform("material.steppe_low", 13);

        glActiveTexture(GL_TEXTURE14);
        glBindTexture(GL_TEXTURE_2D, steppeSide.getTextureID());
        shader.setUniform("material.steppe_side", 14);

        glActiveTexture(GL_TEXTURE15);
        glBindTexture(GL_TEXTURE_2D, steppeUp.getTextureID());
        shader.setUniform("material.steppe_up", 15);

        glActiveTexture(GL_TEXTURE16);
        glBindTexture(GL_TEXTURE_2D, plainLow.getTextureID());
        shader.setUniform("material.plain_low", 16);

        glActiveTexture(GL_TEXTURE17);
        glBindTexture(GL_TEXTURE_2D, plainSide.getTextureID());
        shader.setUniform("material.plain_side", 17);

        glActiveTexture(GL_TEXTURE18);
        glBindTexture(GL_TEXTURE_2D, plainUp.getTextureID());
        shader.setUniform("material.plain_up", 18);

        glActiveTexture(GL_TEXTURE19);
        glBindTexture(GL_TEXTURE_2D, forestLow.getTextureID());
        shader.setUniform("material.forest_low", 19);

        glActiveTexture(GL_TEXTURE20);
        glBindTexture(GL_TEXTURE_2D, forestSide.getTextureID());
        shader.setUniform("material.forest_side", 20);

        glActiveTexture(GL_TEXTURE21);
        glBindTexture(GL_TEXTURE_2D, forestUp.getTextureID());
        shader.setUniform("material.forest_up", 21);

        glActiveTexture(GL_TEXTURE22);
        glBindTexture(GL_TEXTURE_2D, mountainsLow.getTextureID());
        shader.setUniform("material.mountains_low", 22);

        glActiveTexture(GL_TEXTURE23);
        glBindTexture(GL_TEXTURE_2D, mountainsSide.getTextureID());
        shader.setUniform("material.mountains_side", 23);

        glActiveTexture(GL_TEXTURE24);
        glBindTexture(GL_TEXTURE_2D, mountainsUp.getTextureID());
        shader.setUniform("material.mountains_up", 24);

        glActiveTexture(GL_TEXTURE25);
        glBindTexture(GL_TEXTURE_2D, road.getTextureID());
        shader.setUniform("material.road", 25);

        mesh.draw();
    }

    private void createBlendMap(float biom){
        Vector3f[][] arrayBlendMap = new Vector3f[200][200];
        Vector3f[][] arrayHeightMap = new Vector3f[200][200];
        Perlin2D perlin = new Perlin2D(seed);
        for(int x=0; x<arrayBlendMap.length; x++){
            List<Vector3f> line1 = new ArrayList<>();
            List<Vector3f> line2 = new ArrayList<>();
            for(int z=0; z<arrayBlendMap[0].length; z++){
                float value = perlin.getNoise(x/(accuracy * 2.0f),z/(accuracy * 2.0f),8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);

                int pice = ((arrayBlendMap[0].length - 1) / 3);
                int special = (arrayBlendMap[0].length - 1) - pice;
                float proportion = 1.0f;
                if(z > special){
                    int num = z - special;
                    proportion = 1.0f - (float)num / (float)pice;
                }

                result = result * proportion;

                if(z > arrayBlendMap[0].length - (10.0f + Math.random() * 6.0f)){
                    result += 0.01f;
                    arrayBlendMap[x][z] = new Vector3f(result, 0.0f, 0.0f);
                }else {
                    arrayBlendMap[x][z] = new Vector3f(result, result, result);
                }

                Vector2f info = getInfo(biom);
                arrayHeightMap[x][z] = new Vector3f(biomColor / 45.0f,(result * info.getX()) / 30.0f,info.getY());

                line1.add(arrayBlendMap[x][z]);
                line2.add(arrayHeightMap[x][z]);
            }
            blendMap.add(line1);
            heighMap.add(line2);
        }

        Texture texture = new Texture2D();
        texture.setup(arrayBlendMap,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        blendTexture = texture;

        texture = new Texture2D();
        texture.setup(arrayHeightMap,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        heightTexture = texture;
    }

    private void updateBlendMap(float offset, float biom){
        Perlin2D perlin = new Perlin2D(seed);
        blendMap.remove(0);
        heighMap.remove(0);

        List<Vector3f> line1 = new ArrayList<>();
        List<Vector3f> line2 = new ArrayList<>();
        for(int z=0; z<blendMap.get(0).size(); z++){
            float value = perlin.getNoise(((float)199 + offset)/(accuracy * 2.0f),z/(accuracy * 2.0f),8,0.5f);
            int n = (int)(value * 255 + 128) & 255;
            float result = ((float)n / 255.0f);

            int pice = ((blendMap.get(0).size() - 1) / 3);
            int special = (blendMap.get(0).size() - 1) - pice;
            float proportion = 1.0f;
            if(z > special){
                int num = z - special;
                proportion = 1.0f - (float)num / (float)pice;
            }

            result = result * proportion;

            if(z > blendMap.get(0).size() - (10.0f + Math.random() * 6.0f)){
                result += 0.01f;
                line1.add(new Vector3f(result, 0.0f, 0.0f));
            }else {
                line1.add(new Vector3f(result, result, result));
            }

            Vector2f info = getInfo(biom);
            line2.add(new Vector3f(biomColor / 45.0f,(result * info.getX()) / 30.0f,info.getY()));
        }

        blendMap.add(line1);
        heighMap.add(line2);

        Vector3f[][] arrayBlendMap = new Vector3f[200][200];
        Vector3f[][] arrayHeightMap = new Vector3f[200][200];
        for(int x = 0; x < arrayBlendMap.length; x++){
            for(int z = 0; z < arrayBlendMap[0].length; z++){
                arrayBlendMap[x][z] = blendMap.get(x).get(z);
                arrayHeightMap[x][z] = heighMap.get(x).get(z);
            }
        }

        Texture texture = new Texture2D();
        texture.setup(arrayBlendMap,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        blendTexture = texture;

        texture = new Texture2D();
        texture.setup(arrayHeightMap,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        heightTexture = texture;
    }

    private Vector2f getInfo(float biom){
        float h;
        float percent;

        if(biom <= 5.0f){
            percent = 1.0f;
            h = 8.0f;
        }else if(biom <= 10.0f){
            percent = (biom - 5.0f) / 5.0f;
            h = 8.0f + (4.0f * percent);
        }else if(biom <= 15.0f){
            percent = (biom - 10.0f) / 5.0f;
            h = 12.0f + (4.0f * percent);
        }else if(biom <= 20.0f){
            percent = (biom - 15.0f) / 5.0f;
            h = 16.0f + (4.0f * percent);
        }else if(biom <= 25.0f){
            percent = (biom - 20.0f) / 5.0f;
            h = 20.0f - (8.0f * percent);
        }else if(biom <= 30.0f){
            percent = (biom - 25.0f) / 5.0f;
            h = 12.0f + (4.0f * percent);
        }else if(biom <= 35.0f){
            percent = (biom - 30.0f) / 5.0f;
            h = 16.0f - (6.0f * percent);
        }else if(biom <= 40.0f){
            percent = (biom - 35.0f) / 5.0f;
            h = 10.0f + (10.0f * percent);
        }else{
            percent = (biom - 40.0f) / 5.0f;
            h = 20.0f + (10.0f * percent);
        }

        return new Vector2f(h,percent);
    }
}
