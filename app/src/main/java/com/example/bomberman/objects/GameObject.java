package com.example.bomberman.objects;

import android.graphics.Bitmap;

public class GameObject {
    protected Bitmap image;

    protected final int height;
    protected final int width;

    protected int x;
    protected int y;

    public GameObject(Bitmap image, int x, int y){
        this.image = image;
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
