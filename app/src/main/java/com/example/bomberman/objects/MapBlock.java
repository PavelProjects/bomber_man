package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MapBlock extends GameObject {
    private boolean bonus;
    private boolean passable;
    Paint p1 = new Paint();
    Paint p2 = new Paint();

    public MapBlock(Bitmap image, int row, int col, int cellSize, boolean bonus, boolean passable) {
        super(image, 1, 1, col*cellSize, row*cellSize, cellSize);
        this.bonus = bonus;
        this.passable = passable;
        p1.setColor(Color.BLACK);
        p2.setColor(Color.WHITE);
    }

    public void draw(Canvas canvas){
        //canvas.drawBitmap(image, x, y, null);
        if(passable)
            canvas.drawRect(x, y, x+cellSize, y+cellSize, p2);
        else
            canvas.drawRect(x, y, x+cellSize, y+cellSize, p1);
    }

    public boolean isPassable() {
        return passable;
    }
}
