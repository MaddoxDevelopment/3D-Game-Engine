package com.maddev;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        System.out.println("Pressed: " + key);
       keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int key) {

        return keys[key];
    }

}