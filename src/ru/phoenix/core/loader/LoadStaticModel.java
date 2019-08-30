package ru.phoenix.core.loader;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import ru.phoenix.core.loader.model.Material;
import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.model.Vertex;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class LoadStaticModel {
    private static String directory;
    private static boolean noTexture;

    public static ArrayList<Mesh> LoadModel(String path, boolean noTexture){
        setNoTexture(noTexture);
        System.out.println("\n" + path);
        directory = path.substring(0,path.lastIndexOf("/")) + "/";
        AIScene aiScene = aiImportFile(path,aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_CalcTangentSpace | aiProcess_GenSmoothNormals | aiProcess_LimitBoneWeights | aiProcess_JoinIdenticalVertices | aiProcess_FixInfacingNormals);
        checkScene(aiScene);
        return setupMeshes(aiScene);
    }

    private static ArrayList<Mesh> setupMeshes(AIScene aiScene) {

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer materialsBuffer = aiScene.mMaterials();
        ArrayList<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++){
            AIMaterial aiMaterial = AIMaterial.create(materialsBuffer.get(i));
            processMaterial(aiMaterial, materials);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer meshesBuffer = aiScene.mMeshes();
        Mesh[] meshArray = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++){
            AIMesh aiMesh = AIMesh.create(meshesBuffer.get(i));
            Mesh mesh = processMesh(aiMesh, materials);
            meshArray[i] = mesh;
        }

        return new ArrayList<>(Arrays.asList(meshArray));
    }

    private static void processMaterial(AIMaterial aiMaterial, ArrayList<Material> materials){
        Material ambientMap = new Material();
        Material diffuseMap = new Material();
        Material specularMap = new Material();
        Material displaceMap = new Material();
        Material normalMap = new Material();
        Texture ambient = new Texture2D();
        Texture diffuse = new Texture2D();
        Texture specular = new Texture2D();
        Texture displace = new Texture2D();
        Texture normal = new Texture2D();
        AIString path = AIString.calloc();
        String temp;

        if(!isNoTexture()) {
            aiGetMaterialTexture(aiMaterial, aiTextureType_AMBIENT, 0, path, (IntBuffer) null, null, null, null, null, null);
            String textPath = path.dataString();
            temp = textPath;
            if (textPath.length() > 0) {
                ambient.setup(null, directory + textPath, GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
                ambientMap.setType("ambientMap");
                ambientMap.setPath(directory + textPath);
                ambientMap.setId(ambient.getTextureID());
                System.out.println(textPath + " | " + ambientMap.getType());
            }else{
                ambientMap.setType(null);
            }

            aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
            textPath = path.dataString();
            temp = textPath;
            if (textPath.length() > 0) {
                diffuse.setup(null, directory + textPath, GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
                diffuseMap.setType("diffuseMap");
                diffuseMap.setPath(directory + textPath);
                diffuseMap.setId(diffuse.getTextureID());
                System.out.println(textPath + " | " + diffuseMap.getType());
            }

            aiGetMaterialTexture(aiMaterial, aiTextureType_SPECULAR, 0, path, (IntBuffer) null, null, null, null, null, null);
            textPath = path.dataString();
            temp = textPath;
            if (textPath.length() > 0) {
                specular.setup(null, directory + textPath, GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
                specularMap.setType("specularMap");
                specularMap.setPath(directory + textPath);
                specularMap.setId(specular.getTextureID());
                System.out.println(textPath + " | " + specularMap.getType());
            }else{
                specularMap.setType(null);
            }

            aiGetMaterialTexture(aiMaterial, aiTextureType_DISPLACEMENT, 0, path, (IntBuffer) null, null, null, null, null, null);
            textPath = path.dataString();
            if (textPath.length() > 0 && !textPath.equals(temp)) {
                displace.setup(null, directory + textPath, GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
                displaceMap.setType("displaceMap");
                displaceMap.setPath(directory + textPath);
                displaceMap.setId(displace.getTextureID());
                System.out.println(textPath + " | " + displaceMap.getType());
            } else {
                displaceMap.setType(null);
            }

            aiGetMaterialTexture(aiMaterial, aiTextureType_HEIGHT, 0, path, (IntBuffer) null, null, null, null, null, null);
            textPath = path.dataString();
            if (textPath.length() > 0 && !textPath.equals(temp)) {
                normal.setup(null, directory + textPath, GL_RGBA, GL_CLAMP_TO_EDGE);
                normalMap.setType("normalMap");
                normalMap.setPath(directory + textPath);
                normalMap.setId(normal.getTextureID());
                System.out.println(textPath + " | " + normalMap.getType());
            } else {
                normalMap.setType(null);
            }

            AIColor4D color = AIColor4D.create();
            Vector3f cDiffuse = new Vector3f();
            int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
            if (result == 0) {
                cDiffuse = new Vector3f(color.r(), color.g(), color.b());
                diffuseMap.setMaterial(cDiffuse);
            }
        }

        materials.add(ambientMap);
        materials.add(diffuseMap);
        materials.add(specularMap);
        materials.add(displaceMap);
        materials.add(normalMap);
    }

    private static Mesh processMesh(AIMesh aiMesh, ArrayList<Material> materials){
        ArrayList<Vertex> vertex = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        processVertices(aiMesh, vertex);
        processNormals(aiMesh, vertex);
        processTextCoords(aiMesh, vertex);
        processTangents(aiMesh, vertex);
        processBitangents(aiMesh, vertex);
        processIndices(aiMesh, indices);

        ArrayList<Material> material = new ArrayList<>();
        int materialIdx = aiMesh.mMaterialIndex();

        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material.add(materials.get(materialIdx * 5));
            material.add(materials.get(materialIdx * 5 + 1));
            material.add(materials.get(materialIdx * 5 + 2));
            material.add(materials.get(materialIdx * 5 + 3));
            material.add(materials.get(materialIdx * 5 + 4));
        } else {
            material.add(new Material());
        }

        return new Mesh(vertex,indices,material);
    }

    private static void processVertices(AIMesh aiMesh, ArrayList<Vertex> vertex){
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            Vertex newVertex = new Vertex();
            newVertex.setPosition(new Vector3f(aiVertex.x(),aiVertex.y(),aiVertex.z()));
            vertex.add(newVertex);
        }
    }

    private static void processNormals(AIMesh aiMesh, ArrayList<Vertex> vertex) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        int index = 0;
        if (aiNormals != null) {
            while (aiNormals.remaining() > 0) {
                AIVector3D aiNormal = aiNormals.get();
                vertex.get(index++).setNormal(new Vector3f(aiNormal.x(),aiNormal.y(),aiNormal.z()));
            }
        }
    }

    private static void processTextCoords(AIMesh aiMesh, ArrayList<Vertex> vertex) {
        AIVector3D.Buffer aiTexCoords = aiMesh.mTextureCoords(0);
        int index = 0;
        if (aiTexCoords != null) {
            while (aiTexCoords.remaining() > 0) {
                AIVector3D aiTexCoord = aiTexCoords.get();
                vertex.get(index++).setTexCoords(new Vector2f(aiTexCoord.x(), aiTexCoord.y()));
            }
        }
    }

    private static void processTangents(AIMesh aiMesh, ArrayList<Vertex> vertex){
        AIVector3D.Buffer aiTangents = aiMesh.mTangents();
        int index = 0;
        if (aiTangents != null) {
            while (aiTangents.remaining() > 0) {
                AIVector3D aiTangent = aiTangents.get();
                vertex.get(index++).setTangents(new Vector3f(aiTangent.x(),aiTangent.y(),aiTangent.z()));
            }
        }
    }

    private static void processBitangents(AIMesh aiMesh, ArrayList<Vertex> vertex){
        AIVector3D.Buffer aiBitangents = aiMesh.mBitangents();
        int index = 0;
        if (aiBitangents != null) {
            while (aiBitangents.remaining() > 0) {
                AIVector3D aiBitangent = aiBitangents.get();
                vertex.get(index++).setBitangnets(new Vector3f(aiBitangent.x(),aiBitangent.y(),aiBitangent.z()));
            }
        }
    }

    private static void processIndices(AIMesh aiMesh, ArrayList<Integer> indices){
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();

        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get();
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }

    private static void checkScene(AIScene aiScene){
        if (aiScene == null) {
            System.err.println("Error loading model");
            System.exit(1);
        }else {
            System.out.println("Loading is ok.");
            System.out.println("Textures: " + aiScene.mNumTextures());
            System.out.println("Materials: " + aiScene.mNumMaterials());
            System.out.println("Meshes: " + aiScene.mNumMeshes());
            System.out.println("Cameras: " + aiScene.mNumCameras());
            System.out.println("Animations: " + aiScene.mNumAnimations());
            System.out.println("Lights: " + aiScene.mNumLights());
        }
    }

    public static boolean isNoTexture() {
        return noTexture;
    }

    public static void setNoTexture(boolean noTexture) {
        LoadStaticModel.noTexture = noTexture;
    }
}

