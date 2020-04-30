package com.example.bomberman.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.objects.MapBlock;
import com.example.bomberman.surfaces.EditorSurface;
import com.example.bomberman.surfaces.GameSurface;

import java.io.OutputStreamWriter;

public class MapEditor {
    private MapBlock[][] blocks;
    private EditorSurface surface;
    private int rows, columns, width, height;
    private int cellSize = 64;
    private Paint linePaint = new Paint();
    private Paint textFont = new Paint();
    private Paint butt = new Paint();
    private String text = "";

    public MapEditor(EditorSurface surface, int width, int height) {
        Log.d("Editor", "Loading");
        this.surface = surface;
        this.width = width;
        this.height = height;
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
        butt.setColor(Color.YELLOW);
        Log.d("Editor", "Loaded :: " + rows + " " + columns);
    }

    public void pressEvent(int x, int y) {
        if (x >= width - 4 * cellSize && y >= height - 4 * cellSize) {
            parsToJSON();
        }

        int pressRow = y / cellSize;
        int pressCol = x / cellSize;

        int type = blocks[pressRow][pressCol].getType();
        int bonus = blocks[pressRow][pressCol].getTypeBonus();
        if (type == MapBlock.TYPE_BONUS) {
            if (bonus < MapBlock.BONUS_SPEED) {
                bonus++;
            } else {
                type++;
                bonus = -1;
            }
        } else if (type < MapBlock.TYPE_BACKGROUND) {
            type++;
        } else {
            type = MapBlock.TYPE_SOLID;
            bonus = -1;
        }
        blocks[pressRow][pressCol].setType(type, bonus);
        text = "t : " + type + " b : " + bonus;
    }

    public void parsToJSON() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(surface.getContext().openFileOutput("map.json", Context.MODE_PRIVATE));
            outputStreamWriter.write("{\"cellSize\" : " + cellSize + ", \"name\" : \"test2\", \"chr_row\": 0, \"chr_col\":0, \"blocks\" : \n[");
            boolean first = true;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (blocks[i][j].getType() != MapBlock.TYPE_BACKGROUND) {
                        if(first){
                            first = false;
                        }else if (i != rows - 1 && j != columns - 1)
                            outputStreamWriter.append(",\n");
                        outputStreamWriter.append("{\"row\":" + i + ",\"col\":" + j + ",\"type\":" + blocks[i][j].getType() + ",\"bonus\":" + blocks[i][j].getTypeBonus() + "}");

                    }
                }
            }
            outputStreamWriter.append("]}");
            outputStreamWriter.close();
            Log.d("Editor", "Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                blocks[i][j].draw(canvas);
        for (int i = 0; i <= rows; i++) {
            canvas.drawLine(0, i * cellSize, width, i * cellSize, linePaint);
        }
        for (int i = 0; i <= columns; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, height, linePaint);
        }
        canvas.drawText(text, columns / 2 * cellSize - text.length(), cellSize, textFont);
        canvas.drawRect(width - 4 * cellSize, height - 4 * cellSize, width, height, butt);
    }
}
