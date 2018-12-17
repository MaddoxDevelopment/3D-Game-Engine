package com.maddev;

import com.maddev.models.Entity;
import com.maddev.models.RawModel;
import com.maddev.models.TexturedModel;
import com.maddev.models.loaders.ModelLoader;
import com.maddev.models.loaders.ObjLoader;
import com.maddev.shaders.StaticShader;
import com.maddev.texture.ModelTexture;
import org.joml.Vector3f;

import java.io.IOException;

public class Game extends GameWindow {

    private final ObjLoader loader;
    private Renderer renderer;
    private Entity entity;
    private StaticShader shader;
    private Camera camera;

    public Game() {
        loader = new ObjLoader();
        camera = new Camera();
    }

    @Override
    void onLoad() {

        ModelLoader modelLoader = new ModelLoader();
        RawModel rawModel = loader.load("stall.obj", modelLoader);
        int texture = modelLoader.loadTexture("textures/stallTexture.png");
        var textured = new TexturedModel(rawModel, new ModelTexture(texture));
        entity = new Entity(textured, new Vector3f(0, 0, -1), 0, 0, 0, 1);


        try {
            shader = new StaticShader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderer = new Renderer(shader);

    }

    @Override
    void onStop() {
        shader.cleanUp();
    }

    @Override
    void onLoop() {
        camera.move();
        shader.start();
        shader.loadViewMatrix(camera);
        renderer.render(entity, shader);
        shader.stop();
    }
}
