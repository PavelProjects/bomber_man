package com.example.bomberman.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ControlsObject {
    private int padding = 100;
    private final int height, width;
    private Paint buttBack = new Paint();
    private Paint buttOutline = new Paint();

    public ControlsObject(int h, int w, int color_butt, int color_outline){
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
    }

    public void pressEvent(CharacterObject character, int x, int y){
        //if(x > 0 && x <= 3*padding && y >= )
        if(y >= height - 3*padding && y <= height - 2*padding){
            if(x >= 0 && x <= padding){
                character.setX_dir(-1);
                character.setY_dir(-1);
            }else if(x >= padding && x <= 2*padding){
                character.setX_dir(0);
                character.setY_dir(-1);
            }else if(x >= 2*padding && x <= 3*padding){
                character.setX_dir(1);
                character.setY_dir(-1);
            }
        }else if(y >= height - 2*padding && y <= height - padding){
            if(x >= 0 && x <= padding){
                character.setX_dir(-1);
                character.setY_dir(0);
            }else if(x >= 2*padding && x <= 3*padding){
                character.setX_dir(1);
                character.setY_dir(0);
            }
        }else if(y >= height - padding && y < height){
            if(x >= 0 && x <= padding){
                character.setX_dir(-1);
                character.setY_dir(1);
            }else if(x >= padding && x <= 2*padding){
                character.setX_dir(0);
                character.setY_dir(1);
            }else if(x >= 2*padding && x <= 3*padding){
                character.setX_dir(1);
                character.setY_dir(1);
            }
        }
    }
}
