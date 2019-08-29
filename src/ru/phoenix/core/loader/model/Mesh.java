package ru.phoenix.core.loader.model;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.math.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    public static final int MAX_WEIGHTS = 4;

    private VertexBufferObject vbo;

    private ArrayList<Vertex> vertices;
    private ArrayList<Integer> indices;
    private ArrayList<Material> textures;
    private List<Integer> boneIds;
    private List<Float> weights;

    private boolean animated;

    public Mesh(ArrayList<Vertex> vertices, ArrayList<Integer> indices, ArrayList<Material> textures){
        animated = false;
        this.vertices = vertices;
        this.indices = indices;
        this.textures = textures;
        this.boneIds = null;
        this.weights = null;
        setupMesh();
    }

    public Mesh(ArrayList<Vertex> vertices, ArrayList<Integer> indices, ArrayList<Material> textures, ArrayList<Integer> boneIds, ArrayList<Float> weights){
        animated = true;
        this.vertices = vertices;
        this.indices = indices;
        this.textures = textures;
        this.boneIds = boneIds;
        this.weights = weights;
        setupMesh();
    }

    private void setupMesh(){
        vbo = new MeshConfig();
        if(animated){
            vbo.allocate(getPositions(), getNormals(), getTexturesCoords(), getTangents(), getBiTangents(), getBoneId(), getWeight(), null, getIndices());
        }else {
            vbo.allocate(getPositions(), getNormals(), getTexturesCoords(), getTangents(), getBiTangents(), null, null, null, getIndices());
        }
    }

    public void setupInstance(Matrix4f[] instanceMatrix){
        vbo = new MeshConfig();
        if(animated){
            vbo.allocate(getPositions(), getNormals(), getTexturesCoords(), getTangents(), getBiTangents(), getBoneId(), getWeight(), instanceMatrix, getIndices());
        }else {
            vbo.allocate(getPositions(), getNormals(), getTexturesCoords(), getTangents(), getBiTangents(), null, null, instanceMatrix, getIndices());
        }
    }

    public void draw(){
        vbo.draw();
    }

    public ArrayList<Material> getTextures() {
        return textures;
    }

    private float[] getPositions(){
        float[] positions = new float[vertices.size() * 3];
        int index = 0;
        for (Vertex vertex : vertices) {
            positions[index++] = vertex.getPosition().getX();
            positions[index++] = vertex.getPosition().getY();
            positions[index++] = vertex.getPosition().getZ();
        }
        return positions;
    }

    private float[] getNormals(){
        float[] normals = new float[vertices.size() * 3];
        int index = 0;
        for (Vertex vertex : vertices) {
            normals[index++] = vertex.getNormal().getX();
            normals[index++] = vertex.getNormal().getY();
            normals[index++] = vertex.getNormal().getZ();
        }
        return normals;
    }

    private float[] getTexturesCoords(){
        float[] texturesCoords = new float[vertices.size() * 2];
        int index = 0;
        for (Vertex vertex : vertices) {
            texturesCoords[index++] = vertex.getTexCoords().getX();
            texturesCoords[index++] = vertex.getTexCoords().getY();
        }
        return texturesCoords;
    }

    private float[] getTangents(){
        float[] tangents = new float[vertices.size() * 3];
        int index = 0;
        for (Vertex vertex : vertices) {
            tangents[index++] = vertex.getTangents().getX();
            tangents[index++] = vertex.getTangents().getY();
            tangents[index++] = vertex.getTangents().getZ();
        }
        return tangents;
    }

    private float[] getBiTangents(){
        float[] biTangents = new float[vertices.size() * 3];
        int index = 0;
        for (Vertex vertex : vertices) {
            biTangents[index++] = vertex.getBitangnets().getX();
            biTangents[index++] = vertex.getBitangnets().getY();
            biTangents[index++] = vertex.getBitangnets().getZ();
        }
        return biTangents;
    }

    private int[] getBoneId(){
        int[] array = new int[boneIds.size()];
        for(int i=0; i<array.length; i++){
            array[i] = boneIds.get(i);
        }
        return array;
    }

    private float[] getWeight(){
        float[] array = new float[weights.size()];
        for(int i=0; i<array.length; i++){
            array[i] = weights.get(i);
        }
        return array;
    }

    private int[] getIndices(){
        int[] array = new int[indices.size()];
        for(int i=0;i<indices.size();i++){
            array[i] = indices.get(i);
        }
        return array;
    }

    public ArrayList<Vertex> getVerticesArray(){
        return vertices;
    }

    public ArrayList<Integer> getIndicesArray(){
        return indices;
    }

    public VertexBufferObject getVbo() {
        return vbo;
    }
}

