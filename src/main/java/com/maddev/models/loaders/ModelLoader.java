package com.maddev.models.loaders;

import com.maddev.models.RawModel;
import com.maddev.texture.TextureLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * Handles the loading of geometry data into VAOs. It also keeps track of all
 * the created VAOs and VBOs so that they can all be deleted when the game
 * closes.
 *
 * @author Karl
 *
 */
public class ModelLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, float[] textureCoordinates, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoordinates);

        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public int loadTexture(String fileName)
    {
        var loader = new TextureLoader(fileName);
        int textureId = loader.createTexture();
        textures.add(textureId);
        return textureId;
    }

    public void cleanUp() {
        for (int vao : vaos) {
            GL45.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL45.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL45.glDeleteTextures(texture);
        }
    }

    private int createVAO() {
        int vaoID = GL45.glGenVertexArrays();
        vaos.add(vaoID);
        GL45.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL45.glGenBuffers();
        vbos.add(vboID);
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL45.glBufferData(GL45.GL_ARRAY_BUFFER, buffer, GL45.GL_STATIC_DRAW);
        GL45.glVertexAttribPointer(attributeNumber, coordinateSize, GL45.GL_FLOAT, false, 0, 0);
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL45.glGenBuffers();
        vbos.add(vboId);
        GL45.glBindBuffer(GL45.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL45.glBufferData(GL45.GL_ELEMENT_ARRAY_BUFFER, buffer, GL45.GL_STATIC_DRAW);
    }


    private void unbindVAO() {
        GL45.glBindVertexArray(0);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}