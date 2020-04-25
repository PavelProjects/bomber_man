package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MapBlock extends GameObject {
    private boolean destroyed = false;
    private final int typeSolid = 0, typeBonus = 1, typeBackGround = 2;
    private int type;
    Paint solidColor = new Paint();
    Paint backGroundColor = new Paint();
    Paint bonusColor = new Paint();

    public MapBlock(Bitmap image, int row, int col, int cellSize, int type) {
        super(image, 1, 1, col*cellSize, row*cellSize, cellSize);
        if(type > 2){
            this.type = 2;
        }else{
            this.type = type;
        }

        solidColor.setColor(Color.BLACK);
        solidColor.setStrokeWidth(5);
        backGroundColor.setColor(Color.WHITE);
        bonusColor.setColor(Color.GREEN);
    }

    @Override
    public void draw(Canvas canvas){
        //canvas.drawBitmap(image, x, y, null);
        switch(type){
            case typeSolid:
                canvas.drawRect(x, y, x + cellSize, y + cellSize, solidColor);
                break;
            case typeBonus:
                canvas.drawRect(x, y, x + cellSize, y + cellSize, bonusColor);
                break;
            case typeBackGround:
                canvas.drawRect(x, y, x + cellSize, y + cellSize, backGroundColor);
                break;
        }
    }

    public void destroy(){
        destroyed = true;
        bonusColor.setColor(Color.RED);
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public int getType(){
        return type;
    }

    public boolean isPassable(){
        switch(type){
            case typeBonus:
                if(destroyed)
                    return true;
                else
                    return false;
            case typeBackGround: return true;
            default:
                return false;
        }
    }

    public void setType(int type){
        this.type = type;
    }
}
