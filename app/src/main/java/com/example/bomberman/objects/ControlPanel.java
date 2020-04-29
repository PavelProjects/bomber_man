package com.example.bomberman.objects;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.bomberman.R;
import com.example.bomberman.surfaces.GameSurface;

public class ControlPanel {
    private int x, y, cellSize, radius;
    private final int height, width;
    private Paint buttColor = new Paint();
    private Paint fontPaint = new Paint();
    private GameSurface surface;
    private CharacterObject character;

    public ControlPanel(GameSurface surface, int x, int y, int color_butt, int radius, int cellSize){
        this.surface = surface;
        this.height = surface.getHeight();
        this.width = surface.getWidth();
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.cellSize = cellSize;
        this.character = surface.getLevelMap().getCharacter();
        buttColor.setColor(color_butt);
        fontPaint.setTextSize(cellSize);
        fontPaint.setColor(Color.RED);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(x, y - 2*cellSize, radius, buttColor);
        canvas.drawCircle(x, y + 2*cellSize, radius, buttColor);
        canvas.drawCircle(x + 2*cellSize, y, radius, buttColor);
        canvas.drawCircle(x - 2*cellSize, y, radius, buttColor);
        canvas.drawCircle(width - x, y, 3*radius, buttColor);

        canvas.drawText("speed: " + character.getSpeed(), 0, cellSize, fontPaint);
        canvas.drawText("power: " + character.getBomb_power(), width/2 - "power".length()*fontPaint.getTextSize(), cellSize, fontPaint);
        canvas.drawText("bombs: " + character.getBomb_count(), width - 10*fontPaint.getTextSize(), cellSize, fontPaint);

    }

    public void pressEvent(MotionEvent event, CharacterObject character){
        //Log.d("Controls", event.findPointerIndex(event.getPointerId(event.getActionIndex())) + " ");
        if(event.getActionIndex() >= 0 && event.getActionIndex() <= 1) {
            int press_x = (int) event.getX(event.findPointerIndex(event.getPointerId(event.getActionIndex())));
            int press_y = (int) event.getY(event.findPointerIndex(event.getPointerId(event.getActionIndex())));

            if (press_x > 0 && press_x <= this.x + 3 * cellSize && press_y >= press_y - 3 * cellSize && press_y <= height) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
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
            }
        }
    }
}
