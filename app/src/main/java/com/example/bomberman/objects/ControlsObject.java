package com.example.bomberman.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.surfaces.GameSurface;

public class ControlsObject {
    private int x, y, cellSize;
    private final int height, width;
    private Paint buttBack = new Paint();
    private GameSurface surface;

    public ControlsObject(GameSurface surface, int x, int y, int color_butt){
        this.surface = surface;
        this.height = surface.getHeight();
        this.width = surface.getWidth();
        this.x = 3*surface.getGameCubeHeight();
        this.y = height - 3*surface.getGameCubeHeight();
        cellSize = surface.getGameCubeHeight();
        buttBack.setColor(color_butt);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(x, y - 2*cellSize, cellSize, buttBack);
        canvas.drawCircle(x, y + 2*cellSize, cellSize, buttBack);
        canvas.drawCircle(x + 2*cellSize, y, cellSize, buttBack);
        canvas.drawCircle(x - 2*cellSize, y, cellSize, buttBack);
        canvas.drawCircle(width - x, y, 3*cellSize, buttBack);
    }

    public void pressEvent(CharacterObject character, int x, int y){
        if(x > 0 && x <= this.x + 3*cellSize && y >= y - 3*cellSize && y <= height) {
            //Log.d("Controls", x+"::" + y + " || " + this.x + "::" + this.y + "||" + (this.y - cellSize) + " :: " + (this.y + cellSize));
            if(x >= this.x - cellSize && x <= this.x + cellSize ){
                if(y >= this.y + cellSize && y <= this.y + 3*cellSize) {
                    character.setX_dir(0);
                    character.setY_dir(1);
                    Log.d("Controls", "MOVE DOWN");
                }else if(y <= this.y - cellSize && y >= this.y - 3*cellSize) {
                    character.setX_dir(0);
                    character.setY_dir(-1);
                    Log.d("Controls", "MOVE UP");
                }
            }else if(y >= this.y - cellSize && y <= this.y + cellSize){
                if(x >= this.x + cellSize && x <= this.x + 3*cellSize) {
                    character.setX_dir(1);
                    character.setY_dir(0);
                    Log.d("Controls", "MOVE RIGHT");
                }else if(x <= this.x - cellSize && x >= this.x - 3*cellSize) {
                    character.setX_dir(-1);
                    character.setY_dir(0);
                    Log.d("Controls", "MOVE LEFT");
                }
            }
        }else{
            character.stay();
        }
        if(x >= width - 6*cellSize && x <= width && y >= height - 6*cellSize && y <= height){
            Log.d("Controls", "BOMB");
            character.addBomb();
        }
    }
}
