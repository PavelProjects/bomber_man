package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.bomberman.map.LevelMap;

public class GameObject{
    protected Bitmap image;
    protected final int imageHeight, imageWidth;
    protected final int rows, columns;
    protected int cellSize;

    protected int x;
    protected int y;

    public GameObject(Bitmap image, int rows, int columns, int x, int y, int cellSize){
        this.image = image;
        this.x = x;

        this.y = y;
        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;
        if(image != null) {
            this.imageHeight = image.getHeight() / rows;
            this.imageWidth = image.getWidth() / columns;
        }else{
            imageWidth = 0;
            imageHeight = 0;
        }
    }

    protected Bitmap createSubImageAt(int row, int col)  {
        if(image != null) {
            Bitmap subImage = Bitmap.createBitmap(image, col * imageWidth, row * imageHeight, imageWidth, imageHeight);
            return subImage;
        }
        return null;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public void update(LevelMap lvl){}
    public void draw(Canvas canvas){}
}
