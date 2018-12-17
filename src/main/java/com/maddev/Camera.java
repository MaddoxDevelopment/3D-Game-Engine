package com.maddev;

import org.joml.Vector3f;

import java.awt.event.KeyEvent;

public class Camera {

    private Vector3f position = new Vector3f();
    private float pitch;
    private float yaw;
    private float roll;

    public void move() {
        if(Keyboard.isKeyDown(KeyEvent.VK_W)) {
            position.z -= 0.02f;
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_D)) {
            position.x += 0.02f;
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_A)) {
            position.x -= 0.02f;
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_S)) {
            position.z += 0.02f;
        }
        if(Keyboard.isKeyDown(265)) {
           pitch -= 1;
        }
        if(Keyboard.isKeyDown(264)) {
            pitch += 1;
        }
        if(Keyboard.isKeyDown(263)) {
            System.out.println("here");
            yaw -= 1;
        }
        if(Keyboard.isKeyDown(262)) {
            System.out.println("here");
            yaw += 1;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
