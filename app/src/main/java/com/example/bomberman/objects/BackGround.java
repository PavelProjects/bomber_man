package com.example.bomberman.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BackGround extends GameObject {
    private Paint linePaint;

    public BackGround(int width, int height, int cellSize) {
        super(null, 0, 0, width, height, cellSize);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        for(int i = 0; i <= height/cellSize; i++){
            canvas.drawLine(0, i*cellSize, width, i*cellSize, linePaint);
        }
        for(int i = 0; i <= width/cellSize; i++){
            canvas.drawLine(i*cellSize, 0, i*cellSize, height, linePaint);
        }
    }
}
