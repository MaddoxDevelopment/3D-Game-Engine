package com.maddev;


import com.maddev.models.Entity;
import com.maddev.shaders.StaticShader;
import com.maddev.utils.MathUtil;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader) {
        GL45.glEnable(GL45.GL_CULL_FACE);
        GL45.glCullFace(GL45.GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Entity entity, StaticShader shader) {

        GL45.glBindVertexArray(entity.getModel().getRawModel().getVaoID());
        GL45.glEnableVertexAttribArray(0);
        GL45.glEnableVertexAttribArray(1);

        Matrix4f transformation = MathUtil.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotY(), entity.getScale());
        shader.loadTransformationMatrix(transformation);


        GL45.glActiveTexture(GL45.GL_TEXTURE0);
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureId());
        GL45.glDrawElements(GL45.GL_TRIANGLES, entity.getModel().getRawModel().getVertexCount(), GL45.GL_UNSIGNED_INT, 0);
        GL45.glDisableVertexAttribArray(0);
        GL45.glEnableVertexAttribArray(1);
        GL45.glBindVertexArray(0);
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) GameWindow.getWidth() / (float) GameWindow.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
}
