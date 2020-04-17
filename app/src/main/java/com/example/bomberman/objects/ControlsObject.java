package com.example.bomberman.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.surfaces.GameSurface;

import java.util.List;

public class ControlsObject {
    private int padding = 150;
    private final int height, width;
    private Paint buttBack = new Paint();
    private Paint buttOutline = new Paint();
    private GameSurface surface;

    public ControlsObject(GameSurface surface, int h, int w, int color_butt, int color_outline){
        this.surface = surface;
        this.height = h;
        this.width = w;
        buttBack.setColor(color_butt);
        buttOutline.setColor(color_outline);
        buttOutline.setStrokeWidth(5);
    }

    public void draw(Canvas canvas){
        canvas.drawRect(0, height-2*padding,  padding, height-padding, buttBack);
        canvas.drawRect(2*padding, height-2*padding,  3*padding, height-padding, buttBack);
        canvas.drawRect(padding, height-3*padding, 2*padding, height-2*padding, buttBack);
        canvas.drawRect(padding, height-padding, 2*padding, height, buttBack);
        canvas.drawLine(0, height-3*padding, 3*padding, height-3*padding, buttOutline);
        canvas.drawLine(3*padding, height-3*padding, 3*padding, height, buttOutline);
        canvas.drawRect(width - 3*padding, height - 3*padding, width, height, buttBack);
    }

    public void pressEvent(CharacterObject character, int x, int y){
        if(x > 0 && x <= 3*padding && y >= height - 3*padding && y <= height) {
            if (y >= height - 3 * padding && y <= height - 2 * padding) {
                if (x >= 0 && x < padding) {
                    character.setX_dir(-1);
                    character.setY_dir(-1);
                } else if (x >= padding && x < 2 * padding) {
                    character.setX_dir(0);
                    character.setY_dir(-1);
                } else if (x >= 2 * padding && x <= 3 * padding) {
                    character.setX_dir(1);
                    character.setY_dir(-1);
                }
            } else if (y >= height - 2 * padding && y < height - padding) {
                if (x >= 0 && x <= padding) {
                    character.setX_dir(-1);
                    character.setY_dir(0);
                } else if (x >= 2 * padding && x <= 3 * padding) {
                    character.setX_dir(1);
                    character.setY_dir(0);
                }
            } else if (y >= height - padding && y < height) {
                if (x >= 0 && x <= padding) {
                    character.setX_dir(-1);
                    character.setY_dir(1);
                } else if (x > padding && x <= 2 * padding) {
                    character.setX_dir(0);
                    character.setY_dir(1);
                } else if (x >= 2 * padding && x <= 3 * padding) {
                    character.setX_dir(1);
                    character.setY_dir(1);
                }
            }
            Log.d("Controls", "MOVE");
        }else if(x >= width - 3*padding && x <= width && y >= height - 3*padding && y <= height){
            Log.d("Controls", "BOMB");
            character.addBomb();
        }else{
            character.setX_dir(0);
            character.setX_dir(0);
        }
    }
}
