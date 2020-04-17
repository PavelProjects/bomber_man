package com.example.bomberman.objects;

import android.graphics.Bitmap;

public class GameObject {
    protected Bitmap image;

    protected final int height, width;

    protected final int rows, columns;

    protected int x;
    protected int y;

    public GameObject(Bitmap image, int rows, int columns, int x, int y){
        this.image = image;
        this.x = x;
        this.y = y;
        this.rows = rows;
        this.columns = columns;
        this.height = image.getHeight() / rows;
        this.width = image.getWidth() / columns;
    }

    protected Bitmap createSubImageAt(int row, int col)  {
        Bitmap subImage = Bitmap.createBitmap(image, col* width, row* height ,width,height);
        return subImage;
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
