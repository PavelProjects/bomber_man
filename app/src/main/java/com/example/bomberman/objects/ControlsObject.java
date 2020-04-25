package com.example.bomberman.objects;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.example.bomberman.R;
import com.example.bomberman.map.LevelMap;
import com.example.bomberman.surfaces.GameSurface;

public class ControlsObject {
    private int x, y, cellSize, radius;
    private final int height, width;
    private Paint buttBack = new Paint();
    private GameSurface surface;

    public ControlsObject(GameSurface surface, int x, int y, int color_butt, int radius, int cellSize){
        this.surface = surface;
        this.height = surface.getHeight();
        this.width = surface.getWidth();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.cellSize = cellSize;
        buttBack.setColor(color_butt);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(x, y - 2*cellSize, radius, buttBack);
        canvas.drawCircle(x, y + 2*cellSize, radius, buttBack);
        canvas.drawCircle(x + 2*cellSize, y, radius, buttBack);
        canvas.drawCircle(x - 2*cellSize, y, radius, buttBack);
        canvas.drawCircle(width - x, y, 3*radius, buttBack);
    }

    public void pressEvent(MotionEvent event, CharacterObject character, GameSurface surface){
        //Log.d("Controls", event.findPointerIndex(event.getPointerId(event.getActionIndex())) + " ");
        if(event.getActionIndex() >= 0 && event.getActionIndex() <= 1) {
            int press_x = (int) event.getX(event.findPointerIndex(event.getPointerId(event.getActionIndex())));
            int press_y = (int) event.getY(event.findPointerIndex(event.getPointerId(event.getActionIndex())));

            if (press_x > 0 && press_x <= this.x + 3 * cellSize && press_y >= press_y - 3 * cellSize && press_y <= height) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (press_x >= this.x - radius && press_x <= this.x + radius) {
                        if (press_y >= this.y + radius && press_y <= this.y + 3 * radius) {
                            character.setDirection(0, 1);
                            //Log.d("Controls", "MOVE DOWN");
                        } else if (press_y <= this.y - radius && press_y >= this.y - 3 * radius) {
                            character.setDirection(0, -1);
                            //Log.d("Controls", "MOVE UP");
                        }
                    } else if (press_y >= this.y - radius && press_y <= this.y + radius) {
                        if (press_x >= this.x + radius && press_x <= this.x + 3 * radius) {
                            character.setDirection(1, 0);
                            // Log.d("Controls", "MOVE RIGHT");
                        } else if (press_x <= this.x - radius && press_x >= this.x - 3 * radius) {
                            character.setDirection(-1, 0);
                            //Log.d("Controls", "MOVE LEFT");
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    character.stay();
                }
            } else if (press_x >= width - 6 * radius && press_x <= width && press_y >= height - 6 * radius && press_y <= height) {
                character.plantBomb(BitmapFactory.decodeResource(surface.getResources(), R.raw.bomb));
            } else {
                character.stay();
            }
        }
    }
}
