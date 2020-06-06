package ru.phoenix.game.content.stage.strategy;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.model.Vertex;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.*;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.stone.*;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.BigTree;
import ru.phoenix.game.content.object.passive.Bush;
import ru.phoenix.game.content.object.passive.Tree;
import ru.phoenix.game.content.object.passive.Weed;
import ru.phoenix.game.logic.lighting.DirectLight;
import ru.phoenix.game.logic.lighting.Light;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class StrategicScreen {
    //shaders
    private Shader mapShader;
    private Shader shader3D;
    private Shader spriteShader;

    //meshes
    private Mesh mesh;

    private List<Block> stones;
    private Block smallGreenStone;
    private Block mediumGreenStone;
    private Block bigGreenStone;
    private Block smallGraySotne;
    private Block mediumGrayStone;
    private Block bigGrayStone;
    private Block smallWhiteStone;
    private Block mediumWhiteStone;
    private Block bigWhiteStone;

    private List<Object> objects;
    private Object tree1;
    private Object tree2;
    private Object tree3;
    private Object tree4;
    private Object tree5;
    private Object tree6;
    private Object bigTree1;
    private Object bigTree2;
    private Object bush1;
    private Object bush2;
    private Object bush3;
    private Object bush4;
    private Object bush5;
    private Object bush6;
    private Object weed1;
    private Object weed2;
    private Object weed3;
    private Object weed4;
    private Object weed5;

    private Projection projection;

    //textures
    private List<List<Vector3f>> heighMap;
    private List<List<Vector3f>> blendMap;
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

    // controls
    private long seed;
    private int xOffset;
    private float accuracy;
    private float lastTime;
    private float biom;
    private float biomColor;
    private float camPos;
    private Vector3f startPos;
    private float tempHeight;
    private boolean control;

    // list controls
    private List<Vector3f> tempSmallGreenPositions;
    private List<Vector3f> tempMediumGreenPositions;
    private List<Vector3f> tempBigGreenPositions;
    private List<Vector3f> tempSmallGrayPositions;
    private List<Vector3f> tempMediumGrayPositions;
    private List<Vector3f> tempBigGrayPositions;
    private List<Vector3f> tempSmallWhitePositions;
    private List<Vector3f> tempMediumWhitePositions;
    private List<Vector3f> tempBigWhitePositions;

    private List<Vector3f> tempTree1Position;
    private List<Vector3f> tempTree2Position;
    private List<Vector3f> tempTree3Position;
    private List<Vector3f> tempTree4Position;
    private List<Vector3f> tempTree5Position;
    private List<Vector3f> tempTree6Position;
    private List<Vector3f> tempBigTree1Position;
    private List<Vector3f> tempBigTree2Position;
    private List<Vector3f> tempBush1Prosition;
    private List<Vector3f> tempBush2Prosition;
    private List<Vector3f> tempBush3Prosition;
    private List<Vector3f> tempBush4Prosition;
    private List<Vector3f> tempBush5Prosition;
    private List<Vector3f> tempBush6Prosition;
    private List<Vector3f> tempWeed1Position;
    private List<Vector3f> tempWeed2Position;
    private List<Vector3f> tempWeed3Position;
    private List<Vector3f> tempWeed4Position;
    private List<Vector3f> tempWeed5Position;

    private List<Light> directLights;

    public StrategicScreen() {
        // shaders
        mapShader = new Shader();
        shader3D = new Shader();
        spriteShader = new Shader();

        // meshes
        smallGreenStone = new SmallStone();
        smallGreenStone.setSimple(true);
        mediumGreenStone = new MediumStone();
        mediumGreenStone.setSimple(true);
        bigGreenStone = new BigStone();
        bigGreenStone.setSimple(true);
        smallGraySotne = new SmallStoneDirt();
        smallGraySotne.setSimple(true);
        mediumGrayStone = new MediumStoneDirt();
        mediumGrayStone.setSimple(true);
        bigGrayStone = new BigStoneDirt();
        bigGrayStone.setSimple(true);
        smallWhiteStone = new SmallStoneSnow();
        smallWhiteStone.setSimple(true);
        mediumWhiteStone = new MediumStoneSnow();
        mediumWhiteStone.setSimple(true);
        bigWhiteStone = new BigStoneSnow();
        bigWhiteStone.setSimple(true);
        stones = new ArrayList<>(Arrays.asList(smallGreenStone,smallGraySotne,smallWhiteStone,mediumGreenStone,mediumGrayStone,mediumWhiteStone,bigGreenStone,bigGrayStone,bigWhiteStone));

        tree1 = new Tree(0);
        tree2 = new Tree(1);
        tree3 = new Tree(2);
        tree4 = new Tree(3);
        tree5 = new Tree(4);
        tree6 = new Tree(5);
        bigTree1 = new BigTree(0);
        bigTree2 = new BigTree(1);
        bush1 = new Bush(0);
        bush2 = new Bush(1);
        bush3 = new Bush(2);
        bush4 = new Bush(3);
        bush5 = new Bush(4);
        bush6 = new Bush(5);
        weed1 = new Weed(0);
        weed2 = new Weed(1);
        weed3 = new Weed(2);
        weed4 = new Weed(3);
        weed5 = new Weed(4);

        objects = new ArrayList<>(Arrays.asList(tree1,tree2,tree3,tree4,tree5,bigTree1,bigTree2,bush1,bush2,bush3,bush4,bush5,weed1,weed2,weed3,weed4,weed5));

        projection = new Projection();

        //textures
        blendMap = new ArrayList<>();
        heighMap = new ArrayList<>();

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

        //controls
        seed = (long)(1 + Math.random() * 10000000000L);
        xOffset = 0;
        lastTime = (float)glfwGetTime();
        accuracy = 20.0f + (float)Math.random() * 30.0f;
        control = true;
        tempHeight = 0.0f;
        camPos = 0.0f;
        //list controls
        tempSmallGreenPositions = new ArrayList<>();
        tempMediumGreenPositions = new ArrayList<>();
        tempBigGreenPositions = new ArrayList<>();
        tempSmallGrayPositions = new ArrayList<>();
        tempMediumGrayPositions = new ArrayList<>();
        tempBigGrayPositions = new ArrayList<>();
        tempSmallWhitePositions = new ArrayList<>();
        tempMediumWhitePositions = new ArrayList<>();
        tempBigWhitePositions = new ArrayList<>();

        tempTree1Position = new ArrayList<>();
        tempTree2Position = new ArrayList<>();
        tempTree3Position = new ArrayList<>();
        tempTree4Position = new ArrayList<>();
        tempTree5Position = new ArrayList<>();
        tempTree6Position = new ArrayList<>();
        tempBigTree1Position = new ArrayList<>();
        tempBigTree2Position = new ArrayList<>();
        tempBush1Prosition = new ArrayList<>();
        tempBush2Prosition = new ArrayList<>();
        tempBush3Prosition = new ArrayList<>();
        tempBush4Prosition = new ArrayList<>();
        tempBush5Prosition = new ArrayList<>();
        tempBush6Prosition = new ArrayList<>();
        tempWeed1Position = new ArrayList<>();
        tempWeed2Position = new ArrayList<>();
        tempWeed3Position = new ArrayList<>();
        tempWeed4Position = new ArrayList<>();
        tempWeed5Position = new ArrayList<>();

        directLights = new ArrayList<>();
    }

    public void init(float biom){

        startPos = Camera.getInstance().getPos();

        mapShader.createVertexShader("VS_strategic.glsl");
        mapShader.createGeometryShader("GS_strategic.glsl");
        mapShader.createFragmentShader("FS_strategic.glsl");
        mapShader.createProgram();

        shader3D.createVertexShader("VS_game_object.glsl");
        shader3D.createGeometryShader("GS_game_object.glsl");
        shader3D.createFragmentShader("FS_game_object.glsl");
        shader3D.createProgram();

        spriteShader.createVertexShader("VS_sprite.glsl");
        spriteShader.createGeometryShader("GS_sprite.glsl");
        spriteShader.createFragmentShader("FS_sprite.glsl");
        spriteShader.createProgram();

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
        this.biom = biom;
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

        initLight();
    }

    private void initLight(){
        float hyp = (float)Math.sqrt(200 * 200 + 200 * 200);
        float x = (float)200 / 2.0f + ((float)Math.sin(Math.toRadians(-45.0f)) * (float)200 / 2.0f);
        float y = hyp;
        float z = (float)200 / 2.0f + ((float)Math.cos(Math.toRadians(-45.0f)) * (float)200 / 2.0f);
        Light directLight = new DirectLight(
                new Vector3f(x,y,z), // position
                new Vector3f(0.2f,0.2f,0.2f), // ambient
                new Vector3f(1.0f,1.0f,1.0f), // diffuse
                new Vector3f(1.0f,1.0f,1.0f), // specular
                200,
                200,
                200
        );
        directLights.add(directLight);
    }

    public void update(float biom) {
        biomColor = biom;
        /*float currentTime = (float) glfwGetTime();
        float diff = currentTime - lastTime;
        if (diff > 0.0166f) { // 0.0166f // 0.033333f*/
        float size = 0.02f;
        camPos += size;

        if (camPos >= 1.0f) {
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
            updateBlendMap(xOffset, this.biom);
            //small
            updateMatrix(tempSmallGreenPositions,smallGreenStone);
            updateMatrix(tempSmallGrayPositions,smallGraySotne);
            updateMatrix(tempSmallWhitePositions,smallWhiteStone);
            //medium
            updateMatrix(tempMediumGreenPositions,mediumGreenStone);
            updateMatrix(tempMediumGrayPositions,mediumGrayStone);
            updateMatrix(tempMediumWhitePositions,mediumWhiteStone);
            //big
            updateMatrix(tempBigGreenPositions,bigGreenStone);
            updateMatrix(tempBigGrayPositions,bigGrayStone);
            updateMatrix(tempBigWhitePositions,bigWhiteStone);

            updateMatrix(tempTree1Position,tree1);
            updateMatrix(tempTree2Position,tree2);
            updateMatrix(tempTree3Position,tree3);
            updateMatrix(tempTree4Position,tree4);
            updateMatrix(tempTree5Position,tree5);
            updateMatrix(tempTree6Position,tree6);

            updateMatrix(tempBigTree1Position,bigTree1);
            updateMatrix(tempBigTree2Position,bigTree2);

            updateMatrix(tempBush1Prosition,bush1);
            updateMatrix(tempBush2Prosition,bush2);
            updateMatrix(tempBush3Prosition,bush3);
            updateMatrix(tempBush4Prosition,bush4);
            updateMatrix(tempBush5Prosition,bush5);
            updateMatrix(tempBush6Prosition,bush6);

            updateMatrix(tempWeed1Position,weed1);
            updateMatrix(tempWeed2Position,weed2);
            updateMatrix(tempWeed3Position,weed3);
            updateMatrix(tempWeed4Position,weed4);
            updateMatrix(tempWeed5Position,weed5);
        } else {
            Camera.getInstance().setPos(Camera.getInstance().getPos().add(new Vector3f(size, 0.0f, 0.0f)));
            Camera.getInstance().updateViewMatrix();
        }
        lastTime = (float) glfwGetTime();
    }

    public void draw(){
        mapShader.useProgram();

        mapShader.setUniformBlock("matrices",0);
        mapShader.setUniform("model_m",projection.getModelMatrix());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, blendTexture.getTextureID());
        mapShader.setUniform("blendMap", 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, heightTexture.getTextureID());
        mapShader.setUniform("heightMap", 1);

        glActiveTexture(GL_TEXTURE10);
        glBindTexture(GL_TEXTURE_2D, desertLow.getTextureID());
        mapShader.setUniform("material.desert_low", 10);

        glActiveTexture(GL_TEXTURE11);
        glBindTexture(GL_TEXTURE_2D, desertSide.getTextureID());
        mapShader.setUniform("material.desert_side", 11);

        glActiveTexture(GL_TEXTURE12);
        glBindTexture(GL_TEXTURE_2D, desertUp.getTextureID());
        mapShader.setUniform("material.desert_up", 12);

        glActiveTexture(GL_TEXTURE13);
        glBindTexture(GL_TEXTURE_2D, steppeLow.getTextureID());
        mapShader.setUniform("material.steppe_low", 13);

        glActiveTexture(GL_TEXTURE14);
        glBindTexture(GL_TEXTURE_2D, steppeSide.getTextureID());
        mapShader.setUniform("material.steppe_side", 14);

        glActiveTexture(GL_TEXTURE15);
        glBindTexture(GL_TEXTURE_2D, steppeUp.getTextureID());
        mapShader.setUniform("material.steppe_up", 15);

        glActiveTexture(GL_TEXTURE16);
        glBindTexture(GL_TEXTURE_2D, plainLow.getTextureID());
        mapShader.setUniform("material.plain_low", 16);

        glActiveTexture(GL_TEXTURE17);
        glBindTexture(GL_TEXTURE_2D, plainSide.getTextureID());
        mapShader.setUniform("material.plain_side", 17);

        glActiveTexture(GL_TEXTURE18);
        glBindTexture(GL_TEXTURE_2D, plainUp.getTextureID());
        mapShader.setUniform("material.plain_up", 18);

        glActiveTexture(GL_TEXTURE19);
        glBindTexture(GL_TEXTURE_2D, forestLow.getTextureID());
        mapShader.setUniform("material.forest_low", 19);

        glActiveTexture(GL_TEXTURE20);
        glBindTexture(GL_TEXTURE_2D, forestSide.getTextureID());
        mapShader.setUniform("material.forest_side", 20);

        glActiveTexture(GL_TEXTURE21);
        glBindTexture(GL_TEXTURE_2D, forestUp.getTextureID());
        mapShader.setUniform("material.forest_up", 21);

        glActiveTexture(GL_TEXTURE22);
        glBindTexture(GL_TEXTURE_2D, mountainsLow.getTextureID());
        mapShader.setUniform("material.mountains_low", 22);

        glActiveTexture(GL_TEXTURE23);
        glBindTexture(GL_TEXTURE_2D, mountainsSide.getTextureID());
        mapShader.setUniform("material.mountains_side", 23);

        glActiveTexture(GL_TEXTURE24);
        glBindTexture(GL_TEXTURE_2D, mountainsUp.getTextureID());
        mapShader.setUniform("material.mountains_up", 24);

        glActiveTexture(GL_TEXTURE25);
        glBindTexture(GL_TEXTURE_2D, road.getTextureID());
        mapShader.setUniform("material.road", 25);

        mesh.draw();

        shader3D.useProgram();
        for(Block stone : stones){
            stone.draw(shader3D);
        }

        spriteShader.useProgram();
        for(Object object : objects){
            object.draw(spriteShader);
        }
    }

    private void createBlendMap(float biom){

        List<Matrix4f> sm1 = new ArrayList<>();
        List<Matrix4f> mm1 = new ArrayList<>();
        List<Matrix4f> bm1 = new ArrayList<>();

        List<Matrix4f> sm2 = new ArrayList<>();
        List<Matrix4f> mm2 = new ArrayList<>();
        List<Matrix4f> bm2 = new ArrayList<>();

        List<Matrix4f> sm3 = new ArrayList<>();
        List<Matrix4f> mm3 = new ArrayList<>();
        List<Matrix4f> bm3 = new ArrayList<>();

        List<Matrix4f> t1 = new ArrayList<>();
        List<Matrix4f> t2 = new ArrayList<>();
        List<Matrix4f> t3 = new ArrayList<>();
        List<Matrix4f> t4 = new ArrayList<>();
        List<Matrix4f> t5 = new ArrayList<>();
        List<Matrix4f> t6 = new ArrayList<>();

        List<Matrix4f> bt1 = new ArrayList<>();
        List<Matrix4f> bt2 = new ArrayList<>();

        List<Matrix4f> b1 = new ArrayList<>();
        List<Matrix4f> b2 = new ArrayList<>();
        List<Matrix4f> b3 = new ArrayList<>();
        List<Matrix4f> b4 = new ArrayList<>();
        List<Matrix4f> b5 = new ArrayList<>();
        List<Matrix4f> b6 = new ArrayList<>();

        List<Matrix4f> w1 = new ArrayList<>();
        List<Matrix4f> w2 = new ArrayList<>();
        List<Matrix4f> w3 = new ArrayList<>();
        List<Matrix4f> w4 = new ArrayList<>();
        List<Matrix4f> w5 = new ArrayList<>();


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

                int pice = ((arrayBlendMap[0].length - 1) / 2);
                int special = (arrayBlendMap[0].length - 1) - pice;
                float proportion = 1.0f;
                if(z > special){
                    int num = z - special;
                    proportion = 1.0f - (float)num / (float)pice;
                }

                result = result * proportion;

                float cap = arrayBlendMap[0].length - (10.0f + (float)Math.random() * 2.0f);

                if(z > cap){
                    result = 0.01f;
                    arrayBlendMap[x][z] = new Vector3f(result, 0.0f, 0.0f);
                }else {
                    arrayBlendMap[x][z] = new Vector3f(result, result, result);
                }

                Vector2f info = getInfo(biom);
                float currentHeight = result * info.getX();
                arrayHeightMap[x][z] = new Vector3f(biomColor / 45.0f,currentHeight / 20.0f,info.getY());

                if(x != 0 && z != 0 && z < cap) {
                    int d = Math.round(arrayHeightMap[x-1][z-1].getY() * 20.0f);
                    int c = Math.round(arrayHeightMap[x][z-1].getY() * 20.0f);
                    int b = Math.round(arrayHeightMap[x-1][z].getY() * 20.0f);
                    int a = Math.round(arrayHeightMap[x][z].getY() * 20.0f);
                    if(a == b && a == c && a == d) {
                        Vector3f position = new Vector3f(x - 0.5f, a, z - 0.5f);
                        if (Math.random() * 500.0f <= 1.0f) { // для камня
                            int coin = (int) Math.round(Math.random() * 2.0f);
                            if (coin == 0) {
                                tempSmallGreenPositions.add(position);
                            } else if (coin == 1) {
                                tempMediumGreenPositions.add(position);
                            } else {
                                tempBigGreenPositions.add(position);
                            }
                        }else if(Math.random() * 500.0f <= 1.0f){ // для дерева
                            int coin = (int) Math.round(Math.random() * 2.0f);
                            if (coin == 0) {
                                tempTree1Position.add(position);
                            } else if (coin == 1) {
                                tempTree2Position.add(position);
                            } else {
                                tempTree3Position.add(position);
                            }
                        }else if(Math.random() * 1500.0f <= 1.0f){
                            tempBigTree1Position.add(position);
                        }else if(Math.random() * 500.0f <= 1.0f){
                            int coin = (int)Math.round(Math.random() * 2.0f);
                            if(coin == 0){
                                tempBush1Prosition.add(position);
                            }else if(coin == 1){
                                tempBush2Prosition.add(position);
                            }else{
                                tempBush3Prosition.add(position);
                            }
                        }else if(Math.random() * 100.0f <= 1.0f){
                            int coin = (int)Math.round(Math.random() * 1.0f);
                            if(coin == 0){
                                tempWeed1Position.add(position);
                            }else{
                                tempWeed2Position.add(position);
                            }
                        }
                    }
                }

                line1.add(arrayBlendMap[x][z]);
                line2.add(arrayHeightMap[x][z]);
            }
            blendMap.add(line1);
            heighMap.add(line2);
        }

        checkSum(tempSmallGreenPositions,sm1,50);
        checkSum(tempSmallGrayPositions,sm2,50);
        checkSum(tempSmallWhitePositions,sm3,50);

        checkSum(tempMediumGreenPositions,mm1,50);
        checkSum(tempMediumGrayPositions,mm2,50);
        checkSum(tempMediumWhitePositions,mm3,50);

        checkSum(tempBigGreenPositions,bm1,50);
        checkSum(tempBigGrayPositions,bm2,50);
        checkSum(tempBigWhitePositions,bm3,50);

        Texture texture = new Texture2D();
        texture.setup(arrayBlendMap,GL_RGB,GL_CLAMP_TO_EDGE);
        blendTexture = texture;

        texture = new Texture2D();
        texture.setup(arrayHeightMap,GL_RGB,GL_CLAMP_TO_EDGE);
        heightTexture = texture;

        Matrix4f[] sm1_1 = new Matrix4f[sm1.size()];
        Matrix4f[] sm2_2 = new Matrix4f[sm2.size()];
        Matrix4f[] sm3_3 = new Matrix4f[sm3.size()];
        for(int i=0; i<sm1.size(); i++){
            sm1_1[i] = sm1.get(i);
            sm2_2[i] = sm2.get(i);
            sm3_3[i] = sm3.get(i);
        }
        smallGreenStone.setInstanceMatrix(sm1_1);
        smallGraySotne.setInstanceMatrix(sm2_2);
        smallWhiteStone.setInstanceMatrix(sm3_3);

        Matrix4f[] mm1_1 = new Matrix4f[mm1.size()];
        Matrix4f[] mm2_2 = new Matrix4f[mm2.size()];
        Matrix4f[] mm3_3 = new Matrix4f[mm3.size()];
        for(int i=0; i<mm1.size(); i++){
            mm1_1[i] = mm1.get(i);
            mm2_2[i] = mm2.get(i);
            mm3_3[i] = mm3.get(i);
        }
        mediumGreenStone.setInstanceMatrix(mm1_1);
        mediumGrayStone.setInstanceMatrix(mm2_2);
        mediumWhiteStone.setInstanceMatrix(mm3_3);

        Matrix4f[] bm1_1 = new Matrix4f[bm1.size()];
        Matrix4f[] bm2_2 = new Matrix4f[bm2.size()];
        Matrix4f[] bm3_3 = new Matrix4f[bm3.size()];
        for(int i=0; i<bm1.size(); i++){
            bm1_1[i] = bm1.get(i);
            bm2_2[i] = bm2.get(i);
            bm3_3[i] = bm3.get(i);
        }
        bigGreenStone.setInstanceMatrix(bm1_1);
        bigGrayStone.setInstanceMatrix(bm2_2);
        bigWhiteStone.setInstanceMatrix(bm3_3);

        int size = 100;
        checkSum(tempTree1Position,t1,size);
        checkSum(tempTree2Position,t2,size);
        checkSum(tempTree3Position,t3,size);
        checkSum(tempTree4Position,t4,size);
        checkSum(tempTree5Position,t5,size);
        checkSum(tempTree6Position,t6,size);
        checkSum(tempBigTree1Position,bt1,size);
        checkSum(tempBigTree2Position,bt2,size);
        checkSum(tempBush1Prosition,b1,size);
        checkSum(tempBush2Prosition,b2,size);
        checkSum(tempBush3Prosition,b3,size);
        checkSum(tempBush4Prosition,b4,size);
        checkSum(tempBush5Prosition,b5,size);
        checkSum(tempBush6Prosition,b6,size);

        Matrix4f[] t1_1 = new Matrix4f[t1.size()];
        Matrix4f[] t2_2 = new Matrix4f[t2.size()];
        Matrix4f[] t3_3 = new Matrix4f[t3.size()];
        Matrix4f[] t4_4 = new Matrix4f[t4.size()];
        Matrix4f[] t5_5 = new Matrix4f[t5.size()];
        Matrix4f[] t6_6 = new Matrix4f[t6.size()];
        Matrix4f[] bt1_1 = new Matrix4f[bt1.size()];
        Matrix4f[] bt2_2 = new Matrix4f[bt2.size()];
        Matrix4f[] b1_1 = new Matrix4f[b1.size()];
        Matrix4f[] b2_2 = new Matrix4f[b2.size()];
        Matrix4f[] b3_3 = new Matrix4f[b3.size()];
        Matrix4f[] b4_4 = new Matrix4f[b4.size()];
        Matrix4f[] b5_5 = new Matrix4f[b5.size()];
        Matrix4f[] b6_6 = new Matrix4f[b6.size()];

        for(int i=0; i<size; i++){
            t1_1[i] = t1.get(i);
            t2_2[i] = t2.get(i);
            t3_3[i] = t3.get(i);
            t4_4[i] = t4.get(i);
            t5_5[i] = t5.get(i);
            t6_6[i] = t6.get(i);
            bt1_1[i] = bt1.get(i);
            bt2_2[i] = bt2.get(i);
            b1_1[i] = b1.get(i);
            b2_2[i] = b2.get(i);
            b3_3[i] = b3.get(i);
            b4_4[i] = b4.get(i);
            b5_5[i] = b5.get(i);
            b6_6[i] = b6.get(i);
        }

        tree1.init(t1_1);
        tree2.init(t2_2);
        tree3.init(t3_3);
        tree4.init(t4_4);
        tree5.init(t5_5);
        tree6.init(t6_6);
        bigTree1.init(bt1_1);
        bigTree2.init(bt2_2);
        bush1.init(b1_1);
        bush2.init(b2_2);
        bush3.init(b3_3);
        bush4.init(b4_4);
        bush5.init(b5_5);
        bush6.init(b6_6);

        size = 500;
        checkSum(tempWeed1Position,w1,size);
        checkSum(tempWeed2Position,w2,size);
        checkSum(tempWeed3Position,w3,size);
        checkSum(tempWeed4Position,w4,size);
        checkSum(tempWeed5Position,w5,size);

        Matrix4f[] w1_1 = new Matrix4f[w1.size()];
        Matrix4f[] w2_2 = new Matrix4f[w2.size()];
        Matrix4f[] w3_3 = new Matrix4f[w3.size()];
        Matrix4f[] w4_4 = new Matrix4f[w4.size()];
        Matrix4f[] w5_5 = new Matrix4f[w5.size()];

        for(int i=0; i<size; i++){
            w1_1[i] = w1.get(i);
            w2_2[i] = w2.get(i);
            w3_3[i] = w3.get(i);
            w4_4[i] = w4.get(i);
            w5_5[i] = w5.get(i);
        }

        weed1.init(w1_1);
        weed2.init(w2_2);
        weed3.init(w3_3);
        weed4.init(w4_4);
        weed5.init(w5_5);
    }

    private void updateBlendMap(int offset, float biom){
        Perlin2D perlin = new Perlin2D(seed);
        blendMap.remove(0);
        heighMap.remove(0);

        int pice = ((blendMap.get(0).size() - 1) / 2);
        int special = (blendMap.get(0).size() - 1) - pice;
        int cap = (int)(blendMap.get(0).size() - (10.0f + Math.random() * 2.0f));

        List<Vector3f> line1 = new ArrayList<>();
        List<Vector3f> line2 = new ArrayList<>();
        for(int z=0; z<blendMap.get(0).size(); z++){
            float value = perlin.getNoise((199.0f + offset)/(accuracy * 2.0f),z/(accuracy * 2.0f),8,0.5f);
            int n = (int)(value * 255 + 128) & 255;
            float result = ((float)n / 255.0f);

            float proportion = 1.0f;
            if(z > special){
                int num = z - special;
                proportion = 1.0f - (float)num / (float)pice;
            }

            result = result * proportion;

            if(z > blendMap.get(0).size() - (10.0f + Math.random() * 2.0f)){
                result = 0.01f;
                line1.add(new Vector3f(result, 0.0f, 0.0f));
            }else {
                line1.add(new Vector3f(result, result, result));
            }

            Vector2f info = getInfo(biom);
            line2.add(new Vector3f(biomColor / 45.0f,(result * info.getX()) / 20.0f,info.getY()));
        }

        blendMap.add(line1);
        heighMap.add(line2);

        Vector3f[][] arrayBlendMap = new Vector3f[200][200];
        Vector3f[][] arrayHeightMap = new Vector3f[200][200];
        for(int x = 0; x < arrayBlendMap.length; x++){
            for(int z = 0; z < arrayBlendMap[0].length; z++){
                arrayBlendMap[x][z] = blendMap.get(x).get(z);
                arrayHeightMap[x][z] = heighMap.get(x).get(z);
                if(x == arrayBlendMap.length - 1){
                    if(z != 0 && z < cap) {
                        int d = Math.round(arrayHeightMap[x-1][z-1].getY() * 20.0f);
                        int c = Math.round(arrayHeightMap[x][z-1].getY() * 20.0f);
                        int b = Math.round(arrayHeightMap[x-1][z].getY() * 20.0f);
                        int a = Math.round(arrayHeightMap[x][z].getY() * 20.0f);
                        if(a == b && a == c && a == d) {
                            Vector3f position = new Vector3f(x - 0.5f, a, z - 0.5f);
                            if(biomColor <= 10.0f){
                                if(Math.random() * 500.0f <= 1.0f){
                                    int coin = (int) Math.round(Math.random() * 2.0f);
                                    if (coin == 0) {
                                        tempSmallGrayPositions.add(position);
                                    } else if (coin == 1) {
                                        tempMediumGrayPositions.add(position);
                                    } else {
                                        tempBigGrayPositions.add(position);
                                    }
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    tempTree6Position.add(position);
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    tempBush6Prosition.add(position);
                                }else if(Math.random() * 1500.0f < 1.0f){
                                    tempWeed3Position.add(position);
                                }
                            }else if(biomColor <= 20.0f){
                                if(Math.random() * 800.0f <= 1.0f){
                                    int coin = (int) Math.round(Math.random() * 2.0f);
                                    if (coin == 0) {
                                        tempSmallGreenPositions.add(position);
                                    } else if (coin == 1) {
                                        tempMediumGreenPositions.add(position);
                                    } else {
                                        tempBigGreenPositions.add(position);
                                    }
                                }else if(Math.random() * 1000.0f <= 1.0f){
                                    tempTree1Position.add(position);
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    tempBigTree1Position.add(position);
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 1.0f);
                                    if(coin == 0){
                                        tempBush1Prosition.add(position);
                                    }else{
                                        tempBush3Prosition.add(position);
                                    }
                                }else if(Math.random() * 200.0f < 1.0f){
                                    tempWeed3Position.add(position);
                                }
                            }else if(biomColor <= 30.0f){
                                if(Math.random() * 500.0f <= 1.0f){
                                    int coin = (int) Math.round(Math.random() * 2.0f);
                                    if (coin == 0) {
                                        tempSmallGreenPositions.add(position);
                                    } else if (coin == 1) {
                                        tempMediumGreenPositions.add(position);
                                    } else {
                                        tempBigGreenPositions.add(position);
                                    }
                                }else if(Math.random() * 500.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 2.0f);
                                    if(coin == 0){
                                        tempTree1Position.add(position);
                                    }else if(coin == 1){
                                        tempTree2Position.add(position);
                                    }else if(coin == 2){
                                        tempTree3Position.add(position);
                                    }
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    tempBigTree1Position.add(position);
                                }else if(Math.random() * 1000.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 2.0f);
                                    if(coin == 0){
                                        tempBush1Prosition.add(position);
                                    }else if(coin == 1){
                                        tempBush2Prosition.add(position);
                                    }else{
                                        tempBush3Prosition.add(position);
                                    }
                                }else if(Math.random() * 100.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 1.0f);
                                    if(coin == 0){
                                        tempWeed1Position.add(position);
                                    }else{
                                        tempWeed2Position.add(position);
                                    }
                                }
                            }else if(biomColor <= 40.0f){
                                if(Math.random() * 900.0f <= 1.0f){
                                    int coin = (int) Math.round(Math.random() * 2.0f);
                                    if (coin == 0) {
                                        tempSmallGreenPositions.add(position);
                                    } else if (coin == 1) {
                                        tempMediumGreenPositions.add(position);
                                    } else {
                                        tempBigGreenPositions.add(position);
                                    }
                                }else if(Math.random() * 100.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 3.0f);
                                    if(coin == 0){
                                        tempTree2Position.add(position);
                                    }else if(coin == 1){
                                        tempTree3Position.add(position);
                                    }else if(coin == 2){
                                        tempTree4Position.add(position);
                                    }else if(coin == 3){
                                        tempTree5Position.add(position);
                                    }
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 2.0f);
                                    if(coin == 0) {
                                        tempBigTree1Position.add(position);
                                    }else{
                                        tempBigTree2Position.add(position);
                                    }
                                }else if(Math.random() * 200.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 2.0f);
                                    if(coin == 0){
                                        tempBush2Prosition.add(position);
                                    }else if(coin == 1){
                                        tempBush4Prosition.add(position);
                                    }else{
                                        tempBush5Prosition.add(position);
                                    }
                                }else if(Math.random() * 100.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 1.0f);
                                    if(coin == 0){
                                        tempWeed4Position.add(position);
                                    }else{
                                        tempWeed5Position.add(position);
                                    }
                                }
                            }else{
                                if(Math.random() * 300.0f <= 1.0f){
                                    int coin = (int) Math.round(Math.random() * 2.0f);
                                    if (coin == 0) {
                                        tempSmallWhitePositions.add(position);
                                    } else if (coin == 1) {
                                        tempMediumWhitePositions.add(position);
                                    } else {
                                        tempBigWhitePositions.add(position);
                                    }
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 3.0f);
                                    if(coin == 0){
                                        tempTree2Position.add(position);
                                    }else if(coin == 1){
                                        tempTree3Position.add(position);
                                    }else if(coin == 2){
                                        tempTree4Position.add(position);
                                    }else if(coin == 3){
                                        tempTree5Position.add(position);
                                    }
                                }else if(Math.random() * 1500.0f <= 1.0f){
                                    tempBigTree2Position.add(position);
                                }else if(Math.random() * 1000.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 2.0f);
                                    if(coin == 0){
                                        tempBush2Prosition.add(position);
                                    }else if(coin == 1){
                                        tempBush4Prosition.add(position);
                                    }else{
                                        tempBush5Prosition.add(position);
                                    }
                                }else if(Math.random() * 1000.0f <= 1.0f){
                                    int coin = (int)Math.round(Math.random() * 1.0f);
                                    if(coin == 0){
                                        tempWeed4Position.add(position);
                                    }else{
                                        tempWeed5Position.add(position);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        checkPositions(tempSmallGreenPositions,50);
        checkPositions(tempSmallGrayPositions,50);
        checkPositions(tempSmallWhitePositions,50);

        checkPositions(tempMediumGreenPositions,50);
        checkPositions(tempMediumGrayPositions,50);
        checkPositions(tempMediumWhitePositions,50);

        checkPositions(tempBigGreenPositions,50);
        checkPositions(tempBigGrayPositions,50);
        checkPositions(tempBigWhitePositions,50);

        checkPositions(tempTree1Position,100);
        checkPositions(tempTree2Position,100);
        checkPositions(tempTree3Position,100);
        checkPositions(tempTree4Position,100);
        checkPositions(tempTree5Position,100);
        checkPositions(tempTree6Position,100);

        checkPositions(tempBigTree1Position,100);
        checkPositions(tempBigTree2Position,100);

        checkPositions(tempBush1Prosition,100);
        checkPositions(tempBush2Prosition,100);
        checkPositions(tempBush3Prosition,100);
        checkPositions(tempBush4Prosition,100);
        checkPositions(tempBush5Prosition,100);
        checkPositions(tempBush6Prosition,100);

        checkPositions(tempWeed1Position,500);
        checkPositions(tempWeed2Position,500);
        checkPositions(tempWeed3Position,500);
        checkPositions(tempWeed4Position,500);
        checkPositions(tempWeed5Position,500);

        Texture texture = new Texture2D();
        texture.setup(arrayBlendMap,GL_RGB,GL_CLAMP_TO_EDGE);
        blendTexture = texture;

        texture = new Texture2D();
        texture.setup(arrayHeightMap,GL_RGB,GL_CLAMP_TO_EDGE);
        heightTexture = texture;
    }

    private Vector2f getInfo(float biom){
        Vector2f vector;

        if(biom <= 7.5f){
            vector = getInfo(biom,5.0f,2.0f);
        }else if(biom <= 12.5f){
            vector = getInfo(biom,10.0f,3.0f);
        }else if(biom <= 17.5f){
            vector = getInfo(biom,15.0f,2.0f);
        }else if(biom <= 22.5f){
            vector = getInfo(biom,20.0f,3.0f);
        }else if(biom <= 27.5f){
            vector = getInfo(biom,25.0f,4.0f);
        }else if(biom <= 32.5f){
            vector = getInfo(biom,30.0f,5.0f);
        }else if(biom <= 37.5f){
            vector = getInfo(biom,35.0f,4.0f);
        }else if(biom <= 42.5f){
            vector = getInfo(biom,40.0f,10.0f);
        }else{
            vector = getInfo(biom,45.0f,20.0f);
        }

        return vector;
    }

    private Vector2f getInfo(float biom, float correction, float biomHeight){

        float percent;
        if(biom < correction){
            percent = 1.0f - (Math.abs(biom - correction) / 5.0f);
            control = true;
        }else if(biom > correction){
            percent = Math.abs(biom - correction) / 5.0f;
            control = false;
        }else{
            if(control){
                percent = 1.0f;
            }else{
                percent = 0.0f;
            }
        }

        float h;
        if(tempHeight < biomHeight){
            h = tempHeight + 0.005f;
            if(h >= biomHeight){
                h = biomHeight;
            }
        }else if(tempHeight > biomHeight){
            h = tempHeight - 0.005f;
            if(h <= biomHeight){
                h = biomHeight;
            }
        }else{
            h = biomHeight;
        }
        tempHeight = h;

        return new Vector2f(h,percent);
    }

    public float getCurrentBiom(){
        return Math.round(heighMap.get(heighMap.size() / 2).get(0).getX() * 45.0f);
    }

    private void updateMatrix(List<Vector3f> positions, Block stone){
        Matrix4f[] matrices = new Matrix4f[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            Vector3f pos = new Vector3f(positions.get(i));
            pos = new Vector3f(pos.getX() - 1, pos.getY(), pos.getZ());
            positions.set(i, pos);
            Projection projection = new Projection();
            projection.setTranslation(pos);
            matrices[i] = projection.getModelMatrix();
        }
        stone.updateInstanceMatrix(matrices);
    }

    private void updateMatrix(List<Vector3f> positions, Object object){
        Matrix4f[] matrices = new Matrix4f[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            Vector3f pos = new Vector3f(positions.get(i));
            pos = new Vector3f(pos.getX() - 1, pos.getY(), pos.getZ());
            positions.set(i, pos);
            Projection projection = new Projection();
            projection.setTranslation(pos);
            matrices[i] = projection.getModelMatrix();
        }
        object.updateInstanceMatrix(matrices);
    }

    private void checkSum(List<Vector3f> positions, List<Matrix4f> matrices, int size){
        if(positions.size() < size){
            int diff = size - positions.size();
            for(int i=0; i<diff; i++){
                positions.add(0,new Vector3f());
            }
        }else{
            checkPositions(positions, size);
        }

        for(Vector3f position : positions){
            Projection projection = new Projection();
            projection.setTranslation(position);
            matrices.add(projection.getModelMatrix());
        }
    }

    private void checkPositions(List<Vector3f> positions, int size){
        if(positions.size() > size){
            int diff = positions.size() - size;
            for(int i=0; i<diff; i++) {
                positions.remove(0);
            }
        }
    }
}
