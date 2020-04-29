package com.example.bomberman.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.objects.MapBlock;
import com.example.bomberman.surfaces.EditorSurface;


public class MapEditor {
    private MapBlock[][] blocks;
    private EditorSurface surface;
    private int rows, columns;
    private int cellSize = 64;
    private Paint linePaint = new Paint();
    private Paint textFont = new Paint();
    private String text = "";

    public MapEditor(EditorSurface surface, int width, int height) {
        Log.d("Editor", "Loading");
        this.surface = surface;
        this.rows = height / cellSize + 1;
        this.columns = width / cellSize + 1;

        blocks = new MapBlock[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                blocks[i][j] = new MapBlock(null, j * cellSize, i * cellSize, cellSize, 2);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
        textFont.setTextSize(cellSize);
        textFont.setColor(Color.RED);
        Log.d("Editor", "Loaded");
    }

    public void pressEvent(int x, int y){
        int pressRow = y/cellSize;
        int pressCol = x/cellSize;
        int type = blocks[pressRow][pressCol].getType();
        int bonus = blocks[pressRow][pressCol].getTypeBonus();
        if(type == MapBlock.TYPE_BONUS){
            if(bonus < MapBlock.BONUS_SPEED){
                bonus++;
            }else{
                type++;
                bonus = -1;
            }
        }else if (type < MapBlock.TYPE_BACKGROUND){
            type++;
        }else{
            type = MapBlock.TYPE_SOLID;
            bonus = -1;
        }
        blocks[pressRow][pressCol].setType(type, bonus);
        text = "t : " + type + " b : " + bonus;
    }

    public void parsToJSON(){

    }

    public void draw(Canvas canvas){
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                blocks[i][j].draw(canvas);
        for (int i = 0; i <= rows; i++) {
            canvas.drawLine(0, i * cellSize, columns * cellSize, i * cellSize, linePaint);
        }
        for (int i = 0; i <= columns; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, rows * cellSize, linePaint);
        }
        canvas.drawText(text, columns/2 * cellSize - text.length(), cellSize, textFont);
    }
}
