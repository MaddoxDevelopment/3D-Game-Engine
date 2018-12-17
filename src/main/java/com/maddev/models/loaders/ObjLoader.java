package com.maddev.models.loaders;

import com.maddev.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textures = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();

    private float[] texturesArray;
    private float[] normalsArray;
    private float[] verticesArray;
    private int[] indicesArray;


    public RawModel load(String fileName, ModelLoader loader) {
        var classloader = Thread.currentThread().getContextClassLoader();
        var is = classloader.getResourceAsStream("models/" + fileName);
        var lines = new BufferedReader(new InputStreamReader(is)).lines();
        lines.forEach(s -> {
            System.out.println(s);
           if(s.startsWith("v ")) {
               onVertexPosition(s);
           }
           else if(s.startsWith("vn ")) {
               onNormal(s);
           }
           else if(s.startsWith("vt ")) {
                onTexture(s);
           }
           else if(s.startsWith("f ")) {
                if(texturesArray == null) {
                    texturesArray = new float[vertices.size() * 2];
                }
                if(normalsArray == null) {
                    normalsArray = new float[vertices.size() * 3];
                }
                onF(s);
           }
        });
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, texturesArray, indicesArray);
    }

    private void onNormal(String line) {
        normals.add(getVector3FromLine(line));
    }

    private void onVertexPosition(String line) {
        vertices.add(getVector3FromLine(line));
    }

    private void onTexture(String line) {
        textures.add(getVector2FromLine(line));
    }

    private void onF(String line) {
        var split = line.split(" ");
        var vertex1 = split[1].split("/");
        var vertex2 = split[2].split("/");
        var vertex3 = split[3].split("/");

        processVertex(vertex1);
        processVertex(vertex2);
        processVertex(vertex3);

    }

    private void processVertex(String[] vertexes) {
        int currentVertex = Integer.parseInt(vertexes[0]) - 1;
        indices.add(currentVertex);
        int currentTexturePointer = Integer.parseInt(vertexes[1]) -1;
        var currentTexture = textures.get(currentTexturePointer);
        texturesArray[currentVertex * 2] = currentTexture.x;
        texturesArray[currentVertex * 2 + 1] = 1 - currentTexture.y;
        var currentNormalPointer = Integer.parseInt(vertexes[2]) - 1;
        var currentNormal = normals.get(currentNormalPointer);
        normalsArray[currentVertex * 3] = currentNormal.x;
        normalsArray[currentVertex * 3 + 1] = currentNormal.y;
        normalsArray[currentVertex * 3 + 2] = currentNormal.z;
    }

    private Vector3f getVector3FromLine(String line) {
        var split = line.split(" ");
        var x = Float.parseFloat(split[1]);
        var y = Float.parseFloat(split[2]);
        var z = Float.parseFloat(split[3]);
        return new Vector3f(x, y, z);
    }

    private Vector2f getVector2FromLine(String line) {
        var split = line.split(" ");
        var x = Float.parseFloat(split[1]);
        var y = Float.parseFloat(split[2]);
        return new Vector2f(x, y);
    }


}
