package ru.phoenix.core.shader;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.math.Vector4f;
import ru.phoenix.core.util.BufferUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    
    /** Исключение при ошибке компиляции/линковки шейдера */
    public static class ShaderCompilationException extends RuntimeException {
        public ShaderCompilationException(String message) {
            super(message);
        }
    }

    private int program;
    private Map<String, Integer> uniformLocationCache;

    public Shader(){
        program = glCreateProgram();
        if(program == 0){
            throw new ShaderCompilationException("Shader creation failed");
        }
        uniformLocationCache = new HashMap<>();
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
        // Clear cache after linking in case of re-linking
        uniformLocationCache.clear();
    }

    private void addProgram(String text, int type){
        int shader = glCreateShader(type);

        if(shader == 0) {
            throw new ShaderCompilationException(this.getClass().getName() + " Shader creation failed");
        }
        glShaderSource(shader, text);
        glCompileShader(shader);
        check(shader,false);
        glAttachShader(program,shader);
    }

    private void check(int data, boolean isProgram){
        if(isProgram){
            if(glGetProgrami(data,GL_LINK_STATUS) == 0){
                String log = glGetProgramInfoLog(program,1024);
                throw new ShaderCompilationException("Shader program link failed: " + log);
            }
        }else {
            if (glGetShaderi(data, GL_COMPILE_STATUS) == 0) {
                String log = glGetShaderInfoLog(data, 1024);
                throw new ShaderCompilationException("Shader compile failed: " + log);
            }
        }
    }

    public  void setUniformBlock(String name, int index){
        int uniformBlockIndex = glGetUniformBlockIndex(program, name);
        glUniformBlockBinding(program, uniformBlockIndex, index);
    }

    private int getUniformLocation(String uniformName) {
        return uniformLocationCache.computeIfAbsent(uniformName, name -> glGetUniformLocation(program, name));
    }

    public void setUniform(String uniformName, int value){
        glUniform1i(getUniformLocation(uniformName), value);
    }

    public void setUniform(String uniformName, float value){
        glUniform1f(getUniformLocation(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f vector){
        glUniform3f(getUniformLocation(uniformName), vector.getX(), vector.getY(), vector.getZ());
    }

    public void setUniform(String uniformName, Vector4f vector){
        glUniform4f(getUniformLocation(uniformName), vector.getX(), vector.getY(), vector.getZ(), vector.getW());
    }

    public void setUniform(String uniformName, Matrix4f matrix){
        glUniformMatrix4fv(getUniformLocation(uniformName), true, BufferUtil.createFlippedBuffer(matrix));
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
            throw new ShaderCompilationException("Shader creation failed");
        }
    }

    private static String ShaderLoader(String path){

        StringBuilder shaderSource = new StringBuilder();

        try (BufferedReader shaderReader = new BufferedReader(new FileReader(path))) {
            String line;
            while((line = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        }
        catch(Exception e) {
            throw new ShaderCompilationException("Failed to load shader from " + path + ": " + e.getMessage());
        }

        return shaderSource.toString();
    }
}
