package com.maddev.shaders.base;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL45;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Arrays;

public abstract class AbstractShader {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public AbstractShader(String vertexFile, String fragmentFile) throws IOException {
        vertexShaderID = loadShader(vertexFile, GL45.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL45.GL_FRAGMENT_SHADER);
        programID = GL45.glCreateProgram();
        GL45.glAttachShader(programID, vertexShaderID);
        GL45.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL45.glLinkProgram(programID);
        GL45.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniform) {
        return GL45.glGetUniformLocation(programID, uniform);
    }

    public void start() {
        GL45.glUseProgram(programID);
    }

    public void stop() {
        GL45.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL45.glDetachShader(programID, vertexShaderID);
        GL45.glDetachShader(programID, fragmentShaderID);
        GL45.glDeleteShader(vertexShaderID);
        GL45.glDeleteShader(fragmentShaderID);
        GL45.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL45.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        GL45.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL45.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int location, boolean value) {
        loadFloat(location, value ? 1 : 0);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.get(matrixBuffer);
        GL45.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private static int loadShader(String fileName, int type) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("shaders/" + fileName);
        if(is == null) {
            throw new IOException("Failed to load " + fileName + ". Input stream was null");
        }
        String source = new String(is.readAllBytes());
        int shaderID = GL45.glCreateShader(type);
        GL45.glShaderSource(shaderID, source);
        GL45.glCompileShader(shaderID);
        if (GL45.glGetShaderi(shaderID, GL45.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL45.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
        }
        return shaderID;
    }

}
