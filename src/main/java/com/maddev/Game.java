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
import java.util.ArrayList;
import java.util.List;

public class Game extends GameWindow {

    private final ObjLoader loader;
    private Renderer renderer;
    private List<Entity> entities;

    private StaticShader shader;
    private Camera camera;

    public Game() {
        loader = new ObjLoader();
        camera = new Camera();
    }

    @Override
    void onLoad() {

        ModelLoader modelLoader = new ModelLoader();
        RawModel rawModel = loader.load("character.obj", modelLoader);
        int texture = modelLoader.loadTexture("textures/Character Texture.png");
        var textured = new TexturedModel(rawModel, new ModelTexture(texture));
        entities = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            entities.add(new Entity(textured, new Vector3f(i, 0, i), 0, 0, 0, 0.2f));
        }

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
        for (Entity entity : entities) {
            renderer.render(entity, shader);
        }
        shader.stop();
    }
}
