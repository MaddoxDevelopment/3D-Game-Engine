package com.maddev.shaders;

import com.maddev.Camera;
import com.maddev.shaders.base.AbstractShader;
import com.maddev.utils.MathUtil;
import org.joml.Matrix4f;

import java.io.IOException;

public class StaticShader extends AbstractShader {

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public StaticShader() throws IOException {
        super("vertexShader.glsl", "fragmentShader.glsl");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = getUniformLocation("transformationMatrix");
        projectionMatrixLocation = getUniformLocation("projectionMatrix");
        viewMatrixLocation = getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        loadMatrix(transformationMatrixLocation, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        loadMatrix(projectionMatrixLocation, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        loadMatrix(viewMatrixLocation, MathUtil.createViewMatrix(camera));
    }

}
