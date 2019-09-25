package ru.phoenix.core.shader;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.util.BufferUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    private int program;

    public Shader(){
        program = glCreateProgram();
        if(program == 0){
            System.err.println("Shader creation failed");
            System.exit(1);
        }
    }

    public void createVertexShader(String shaderFileName) {
        addProgram(ShaderLoader("./data/shader/vs/" + shaderFileName), GL_VERTEX_SHADER);
    }

    public void createGeometryShader(String shaderFileName){
        addProgram(ShaderLoader("./data/shader/gs/" + shaderFileName), GL_GEOMETRY_SHADER);
    }

    public void createFragmentShader(String shaderFileName){
        addProgram(ShaderLoader("./data/shader/fs/" + shaderFileName),GL_FRAGMENT_SHADER);
    }

    public void createProgram(){
        glLinkProgram(program);
        check(program,true);
    }

    private void addProgram(String text, int type){
        int shader = glCreateShader(type);

        if(shader == 0) {
            System.err.println(this.getClass().getName() + " Shader creation failed");
            System.exit(1);
        }
        glShaderSource(shader, text);
        glCompileShader(shader);
        check(shader,false);
        glAttachShader(program,shader);
    }

    private void check(int data, boolean isProgram){
        if(isProgram){
            if(glGetProgrami(data,GL_LINK_STATUS) == 0){
                System.err.println(glGetProgramInfoLog(program,1024));
                System.exit(1);
            }
        }else {
            if (glGetShaderi(data, GL_COMPILE_STATUS) == 0) {
                System.err.println(glGetShaderInfoLog(data, 1024));
                System.exit(1);
            }
        }
    }

    public  void setUniformBlock(String name, int index){
        int uniformBlockIndex = glGetUniformBlockIndex(program, name);
        glUniformBlockBinding(program, uniformBlockIndex, index);
    }

    public void setUniform(String uniformName, int value){
        glUniform1i(glGetUniformLocation(program,uniformName),value);
    }

    public void setUniform(String uniformName, float value){
        glUniform1f(glGetUniformLocation(program,uniformName),value);
    }

    public void setUniform(String uniformName, Vector3f vector){
        glUniform3f(glGetUniformLocation(program,uniformName),vector.getX(),vector.getY(),vector.getZ());
    }

    public void setUniform(String uniformName, Matrix4f matrix){
        glUniformMatrix4fv(glGetUniformLocation(program,uniformName), true, BufferUtil.createFlippedBuffer(matrix));
    }

    public void setUniform(String uniformName, Matrix4f[] matrix){
        for(int i=0; i<matrix.length; i++){
            setUniform(uniformName + "[" + i + "]",matrix[i]);
        }
    }

    public void setUniform(String uniformName, List<Matrix4f> matrix){
        for(int i=0; i<matrix.size(); i++){
            setUniform(uniformName + "[" + i + "]",matrix.get(i));
        }
    }

    public void useProgram(){
        glUseProgram(program);
    }

    public void createNewProgram(){
        glDeleteProgram(program);
        program = glCreateProgram();
        if(program == 0){
            System.err.println("Shader creation failed");
            System.exit(1);
        }
    }

    private static String ShaderLoader(String path){

        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader;

        try {
            shaderReader = new BufferedReader(new FileReader(path));
            String line;
            while((line = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            shaderReader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }
}
